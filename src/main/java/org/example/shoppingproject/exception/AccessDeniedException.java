package org.example.shoppingproject.exception;

public class AccessDeniedException extends Throwable {
    public AccessDeniedException(String accessDenied) {
        System.out.println("Acess denied: " + accessDenied);
    }
}
