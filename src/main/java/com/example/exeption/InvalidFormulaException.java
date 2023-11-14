package com.example.exeption;

public class InvalidFormulaException extends RuntimeException {
    public InvalidFormulaException(String message) {
        super(message);
    }
}
