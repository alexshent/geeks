package alexshent.assignment;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {

    @Test
    void bracketsValidForValidExpression() {
        // given
        String expression = "(((4*(-7+2)*(-3.123/-4.56))))";
        boolean expected = true;
        // when
        boolean actual = Tokenizer.bracketsValid(expression);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void bracketsValidForInvalidExpression() {
        // given
        String expression = "(((4*(-7+2)*(-3.123/-4.56)))))";
        boolean expected = false;
        // when
        boolean actual = Tokenizer.bracketsValid(expression);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void operatorsValidForValidExpression() {
        // given
        String expression = "1.2+-2.3--4.5*-5.55/-9.99*(1+2)";
        boolean expected = true;
        // when
        boolean actual = Tokenizer.operatorsValid(expression);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void operatorsValidForInvalidExpression() {
        // given
        String expression = "1.2+-2.3--4.5*+-5.55/-9.99*(1+2)";
        boolean expected = false;
        // when
        boolean actual = Tokenizer.operatorsValid(expression);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void operatorsValidForMultipleNegativeNumbersWithMinusOperatorExpression() {
        // given
        String expression = "-1--2--3";
        boolean expected = true;
        // when
        boolean actual = Tokenizer.operatorsValid(expression);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void operatorsValidForMultipleNegativeNumbersWithAllOperatorsExpression() {
        // given
        String expression = "-1--2+-3*-4/-5";
        boolean expected = true;
        // when
        boolean actual = Tokenizer.operatorsValid(expression);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void operatorsValidForSingleNegativeNumberExpression() {
        // given
        String expression = "-1";
        boolean expected = true;
        // when
        boolean actual = Tokenizer.operatorsValid(expression);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void tokenizeValidExpression() {
        // given
        String expression = "(123.45 + -2.34 * -3.14) / (-33.44 - 0.01)";
        List<String> expected = new ArrayList<>();
        expected.add("(");
        expected.add("123.45");
        expected.add("+");
        expected.add("-2.34");
        expected.add("*");
        expected.add("-3.14");
        expected.add(")");
        expected.add("/");
        expected.add("(");
        expected.add("-33.44");
        expected.add("-");
        expected.add("0.01");
        expected.add(")");
        // when
        List<String> actual = Tokenizer.tokenize(expression);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void tokenizeEmptyExpression() {
        // given
        String expression = "";
        List<String> expected = new ArrayList<>();
        // when
        List<String> actual = Tokenizer.tokenize(expression);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void tokenizeInvalidExpression() {
        assertThrows(IllegalArgumentException.class, () -> {
            String expression = "(123.45 + 2.34 * pi) / (33.44 - 0.01)";
            List<String> actual = Tokenizer.tokenize(expression);
        });
    }

    @Test
    void charactersValidForValidExpression() {
        // given
        String expression = "(1.5+-2)*4/3";
        boolean expected = true;
        // when
        boolean actual = Tokenizer.charactersValid(expression);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void charactersValidForInvalidExpression() {
        // given
        String expression = "(1.5+-2)*a4/3";
        boolean expected = false;
        // when
        boolean actual = Tokenizer.charactersValid(expression);
        // then
        assertEquals(expected, actual);
    }
}