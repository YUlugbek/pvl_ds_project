package com.example.christmas_wish_list.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.christmas_wish_list.model.ShoppingItem;

public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, Long>{
    Optional<ShoppingItem> findByName(String name);
}
