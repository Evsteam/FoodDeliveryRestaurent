package com.evs.foodexp.cart;

public interface CartItemClick<T> {
    void onItemClick(T objecct);
    void update(String id,String quntity);
}
