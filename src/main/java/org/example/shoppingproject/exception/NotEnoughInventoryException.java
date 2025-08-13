package org.example.shoppingproject.exception;

public class NotEnoughInventoryException extends Throwable {
    public NotEnoughInventoryException(String s) {
        System.out.println("Not enough inventory!");
    }
}
