package com.example.util;

import com.example.exeption.InvalidFormulaException;
import com.example.util.ExpressionCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class ExpressionCalculatorTest {
    @ParameterizedTest
    @MethodSource("formulaProvider")
    @DisplayName("Test barrier formula")
    public void testEvaluateCorrectExpression(String formula, int x, double expected) {
        Assertions.assertDoesNotThrow(() -> {
            double result = ExpressionCalculator.evaluateExpression(formula, x);
            Assertions.assertEquals(expected, result, "Result is different from expected.");
        }, "Unexpected exception.");
    }

    public static Stream<Arguments> formulaProvider() {
        final int x = 10;
        return Stream.of(
                Arguments.of("1", x, 1),
                Arguments.of("1/3 + 1/3 + 1/3", x, 1),
                Arguments.of("1.25 * x", x, 1.25 * x),
                Arguments.of("x * 2", x, x * 2),
                Arguments.of("x + 5", x, x + 5),
                Arguments.of("x - 1", x, x - 1),
                Arguments.of("x / 2", x, x / 2),
                Arguments.of("x ^ 2", x, Math.pow(x, 2)),
                Arguments.of("sqrt(x)", x, Math.sqrt(x)),
                Arguments.of("x * sin(x) * sin(x)", x, x * Math.sin(x) * Math.sin(x)),
                Arguments.of("x * cos(x) * cos(x)", x, x * Math.cos(x) * Math.cos(x)),
                Arguments.of("tan(x)", x, Math.tan(x)),
                Arguments.of("log(x)", x, Math.log(x)),
                Arguments.of("log10(x)", x, Math.log10(x)),
                Arguments.of("exp(x)", x, Math.exp(x)),
                Arguments.of("abs(x)", x, Math.abs(-x)),
                Arguments.of("10 * x * abs(sin(x) + cos(x))", x, 10 * x * Math.abs(Math.sin(x) + Math.cos(x)))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidFormulaProvider")
    public void testEvaluateInvalidExpression(String invalidFormula) {
        Assertions.assertThrows(
                InvalidFormulaException.class,
                () -> ExpressionCalculator.evaluateExpression(invalidFormula, 1),
                "Expression must be invalid, but no exception is thrown");
    }

    public static Stream<Arguments> invalidFormulaProvider() {
        return Stream.of(
                Arguments.of("invalid"),
                Arguments.of("x * 2 +"),
                Arguments.of(""),
                Arguments.of("\"\""),
                Arguments.of(")("),
                Arguments.of("4)(4"),
                Arguments.of("1/0")
        );
    }
}
