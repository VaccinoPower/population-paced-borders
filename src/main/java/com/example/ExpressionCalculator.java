package com.example;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class ExpressionCalculator {
    private ExpressionCalculator() {

    }

    public static double evaluateExpression(String expression, double x) {
        Expression exp = new ExpressionBuilder(expression)
                .variables("x")
                .build()
                .setVariable("x", x);
        return exp.evaluate();
    }
}
