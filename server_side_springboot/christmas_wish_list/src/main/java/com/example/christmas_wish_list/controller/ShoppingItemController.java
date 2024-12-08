package com.example.christmas_wish_list.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.christmas_wish_list.model.ShoppingItem;
import com.example.christmas_wish_list.repository.ShoppingItemRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/shoppingItems")
@Tag(name = "Christmas Shopping Items API", description = "Manage your shopping list")
public class ShoppingItemController {

    @Autowired
    private ShoppingItemRepository shoppingItemRepository;

    // Read all items
    @Operation(summary = "Get all shopping items", description = "Retrieve a list of all shopping items in the collection.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of items")
    })
    @GetMapping
    public List<ShoppingItem> getAllItems() {
        return shoppingItemRepository.findAll();
    }

    // Add item
    @Operation(summary = "Add a shopping item", description = "Add a new item to the shopping list.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Item successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    @PostMapping
    public ResponseEntity<ShoppingItem> addItem(@RequestBody ShoppingItem shoppingItem) {
        ShoppingItem savedItem = shoppingItemRepository.findByName(shoppingItem.getName())
            .map(existingItem -> {
                existingItem.setAmount(shoppingItem.getAmount());
                return shoppingItemRepository.save(existingItem);
            })
            .orElseGet(() -> shoppingItemRepository.save(shoppingItem));

        HttpStatus status = savedItem.equals(shoppingItem) ? HttpStatus.CREATED : HttpStatus.OK;
        return new ResponseEntity<>(savedItem, status);
    }

    // Read single item by name
    @Operation(summary = "Get a single shopping item", description = "Retrieve details of a single shopping item by its name.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the item"),
        @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @GetMapping("/{shoppingItemName}")
    public ResponseEntity<ShoppingItem> getSingleItem(@PathVariable String shoppingItemName) {
        return shoppingItemRepository.findByName(shoppingItemName)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Update single item by name
    @Operation(summary = "Update a shopping item", description = "Update the details of a specific shopping item by its name.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated the item"),
        @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @PutMapping("/{shoppingItemName}")
    public ResponseEntity<ShoppingItem> updateSingleItem(@PathVariable String shoppingItemName, @RequestBody ShoppingItem updatedItem) {
        return shoppingItemRepository.findByName(shoppingItemName)
            .map(existingItem -> {
                existingItem.setAmount(updatedItem.getAmount());
                shoppingItemRepository.save(existingItem);
                return ResponseEntity.ok(existingItem);
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Delete single item by name
    @Operation(summary = "Delete a shopping item", description = "Delete a specific shopping item from the list by its name.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Item successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @DeleteMapping("/{shoppingItemName}")
    public ResponseEntity<Void> deleteSingleItem(@PathVariable String shoppingItemName) {
        shoppingItemRepository.findByName(shoppingItemName)
            .ifPresentOrElse(
                existingItem -> shoppingItemRepository.delete(existingItem),
                () -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND); }
            );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
