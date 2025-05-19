package com.kid.generator;

public class Main {
    public static void main(String[] args) {
        IdGenerator generator = new IdGenerator(1); // Node ID = 1 (example)

        // Generate and print some IDs
        for (int i = 0; i < 5; i++) {
            long id = generator.generateId();
            System.out.println("Generated ID: " + id);
        }
    }
}
