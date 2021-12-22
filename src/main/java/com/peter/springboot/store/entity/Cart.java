package com.peter.springboot.store.entity;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<Integer, Product> cart;

    public Cart() {
    }

    public Cart(Map<Integer, Product> cart) {
        this.cart = cart;
    }

    public Map<Integer, Product> getCart() {
        return cart;
    }

    public void setCart(Map<Integer, Product> cart) {
        this.cart = cart;
    }

    public void addProduct(int id, Product pro) {
        if (this.cart == null) {
            cart = new HashMap<>();
            System.out.println("Created cart successfully!");
        }
        if (this.cart.containsKey(pro.getId())) {
            int quantity = this.cart.get(pro.getId()).getQuantityInStock();
            pro.setQuantityInStock(quantity + pro.getQuantityInStock());
        }
        this.cart.put(pro.getId(), pro);
    }

    public Product deleteProduct(int id) {
        return (this.cart != null && this.cart.containsKey(id)) ? this.cart.remove(id) : null;
    }

    public void updateProduct(int id, Product pro) {
        if (this.cart != null && this.cart.containsKey(id)) {
            this.cart.replace(id, pro);
        }
    }

    public int deleteAll() {
        int count = 0;
        for (Product pro : this.cart.values()) {
            if (this.cart.remove(pro.getId(), pro)) {
                ++count;
            }
        }
        return count;
    }

    public double getTotal() {
        double total = 0;
        for (Product pro : this.cart.values()) {
            total += pro.getQuantityInStock() * pro.getExportPrice();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cart=" + cart +
                '}';
    }
}
