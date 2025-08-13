package org.example.shoppingproject.exception;

public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException() {
    super("Incorrect credentials, please try again.");
  }
}