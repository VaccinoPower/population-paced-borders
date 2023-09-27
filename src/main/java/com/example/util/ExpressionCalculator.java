package com.example.util;

import com.example.exeption.InvalidFormulaException;
import net.objecthunter.exp4j.ExpressionBuilder;

public final class ExpressionCalculator {
    private ExpressionCalculator() {

    }

    public static double evaluateExpression(String expression, int x) throws InvalidFormulaException {
        try {
            return new ExpressionBuilder(expression)
                    .variables("x")
                    .build()
                    .setVariable("x", x)
                    .evaluate();
        } catch (RuntimeException e) {
            throw new InvalidFormulaException("There was an error in the formula: \"" + expression + "\"");
        }
    }
}
