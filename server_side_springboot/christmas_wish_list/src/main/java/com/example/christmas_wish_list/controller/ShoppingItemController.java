package com.example.christmas_wish_list.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.christmas_wish_list.model.ShoppingItem;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/shoppingItems")
@Tag(name = "Christmas Shopping Items API", description = "Manage your shopping list")
public class ShoppingItemController {
    private List<ShoppingItem> shoppingItems = new ArrayList<>();

    // Read all items
    @Operation(summary = "Get all shopping items", description = "Retrieve a list of all shopping items in the collection.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of items")
    })
    @GetMapping
    public List<ShoppingItem> getAllItems() {
        return shoppingItems;
    }

    // Add item
    @Operation(summary = "Add a shopping item", description = "Add a new item to the shopping list.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Item successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    @PostMapping
    public ResponseEntity<ShoppingItem> addItem(@RequestBody(description = "Details of the item to add") ShoppingItem shoppingItem) {
        shoppingItems.add(shoppingItem);
        return new ResponseEntity<>(shoppingItem, HttpStatus.CREATED);
    }

    // Read single item by name
    @Operation(summary = "Get a single shopping item", description = "Retrieve details of a single shopping item by its name.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the item"),
        @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @GetMapping("/{shoppingItemName}")
    public ResponseEntity<ShoppingItem> getSingleItem(@PathVariable String shoppingItemName) {
        return shoppingItems.stream()
            .filter(item -> item.getName().equals(shoppingItemName))
            .findFirst()
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
    public ResponseEntity<ShoppingItem> updateSingleItem(@PathVariable String shoppingItemName, @RequestBody(description = "Updated details of the item") ShoppingItem updatedItem) {
        for (ShoppingItem item : shoppingItems) {
            if (item.getName().equalsIgnoreCase(shoppingItemName)) {
                item.setAmount(updatedItem.getAmount());
                return ResponseEntity.ok(item);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Delete single item by name
    @Operation(summary = "Delete a shopping item", description = "Delete a specific shopping item from the list by its name.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Item successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @DeleteMapping("/{shoppingItemName}")
    public ResponseEntity<Void> deleteSingleItem(@PathVariable String shoppingItemName) {
        boolean isRemoved = shoppingItems.removeIf(item -> item.getName().equalsIgnoreCase(shoppingItemName));

        if (isRemoved) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
