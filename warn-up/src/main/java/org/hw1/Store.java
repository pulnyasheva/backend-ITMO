package org.hw1;

import java.util.LinkedList;
import java.util.Queue;

public class Store {
    private static final int MAX_PRODUCTS = 100;
    private final Queue<String> products = new LinkedList<>();

    public synchronized void addProducts(String newProduct) {
        while (products.size() + 1 > MAX_PRODUCTS) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        products.add(newProduct);
        System.out.println("Add " + newProduct);
        notifyAll();
    }

    public synchronized String getProduct() {
        while (products.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String product = products.remove();
        System.out.println("Get " + product);
        notifyAll();
        return product;
    }

    public static void main(String[] args) {
        Store store = new Store();

        Thread supplier = new Thread(() -> {
            int countProducts = 10;
            for (int i = 1; i <= countProducts; i++) {
                String newProduct = "Product" + i;
                store.addProducts(newProduct);
                try {
                    Thread.sleep(0, 5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        supplier.start();

        for (int i = 1; i <= 10; i++) {
            Thread consumer = new Thread(store::getProduct);
            consumer.start();
        }
    }
}
