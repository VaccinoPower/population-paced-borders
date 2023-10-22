package com.example.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

class Validator<T, U> {
    private final List<ValidationRule<T, U>> rules = new ArrayList<>();
    private String errorMessage = "";

    public boolean validate(T t, U u) {
        for (ValidationRule<T, U> rule : rules) {
            if (!rule.validate(t, u)) {
                errorMessage = rule.getErrorMessage();
                return false;
            }
        }
        return true;
    }

    public void addRule(BiPredicate<T, U> validationFunction, String errorMessage) {
        rules.add(new ValidationRule<>(validationFunction, errorMessage));
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    private static class ValidationRule<T, U> {
        private final BiPredicate<T, U> validationFunction;
        private final String errorMessage;

        public ValidationRule(BiPredicate<T, U> validationFunction, String errorMessage) {
            this.validationFunction = validationFunction;
            this.errorMessage = errorMessage;
        }

        public boolean validate(T t, U u) {
            return validationFunction.test(t, u);
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
