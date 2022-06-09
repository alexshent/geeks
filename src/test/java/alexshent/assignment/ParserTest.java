package alexshent.assignment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    public void evaluateForValidExpression() {
        // given
        String expression = "(4*(-7+2)*(-3.123/-4.56))";
        int expected = -13;
        // when
        double result = Parser.evaluate(expression);
        int actual = (int) result;
        // then
        assertEquals(expected, actual);
    }

    @Test
    public void evaluateForInvalidExpressionBrackets() {
        assertThrows(IllegalArgumentException.class, () -> {
            String expression = "(4*((-7+2)*(-3.123/-4.56))";
            Parser.evaluate(expression);
        });
    }

    @Test
    public void evaluateForInvalidExpressionOperators() {
        assertThrows(IllegalArgumentException.class, () -> {
            String expression = "(4*/(-7+2)*(-3.123/-4.56))";
            Parser.evaluate(expression);
        });
    }

    @Test
    public void evaluateForEmptyExpression() {
        // given
        String expression = "";
        double expected = 0.0;
        // when
        double actual = Parser.evaluate(expression);
        // then
        assertEquals(expected, actual);
    }

    @Test
    public void evaluateForOneDigitPositiveExpression() {
        // given
        String expression = "7";
        double expected = 7.0;
        // when
        double actual = Parser.evaluate(expression);
        // then
        assertEquals(expected, actual);
    }

    @Test
    public void evaluateForOneDigitNegativeExpression() {
        // given
        String expression = "-7";
        double expected = -7.0;
        // when
        double actual = Parser.evaluate(expression);
        // then
        assertEquals(expected, actual);
    }
}