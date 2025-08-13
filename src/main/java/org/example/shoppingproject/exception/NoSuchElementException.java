package org.example.shoppingproject.exception;

public class NoSuchElementException extends Exception {
    public NoSuchElementException(String userNotFound) {
        System.out.println("user not found: " + userNotFound);
    }
}
