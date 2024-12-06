package com.example.christmas_wish_list.model;

public class ShoppingItem {
    private String name;
    private int amount;

    public ShoppingItem(){}
    // public ShoppingItem(String name, int amount) {
    //     this.name = name;
    //     this.amount = amount;
    // }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
