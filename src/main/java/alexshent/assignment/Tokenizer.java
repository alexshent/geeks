package alexshent.assignment;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Arithmetic expression tokenizer
 */
public class Tokenizer {

    private static final Character[] numbers = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.' };
    private static final Character[] operators = { '+', '-', '*', '/' };
    private static final Character[] brackets = { '(', ')' };

    /**
     * Break arithmetic expression into tokens
     * @param expression arithmetic expression
     * @return list of tokens
     */
    public static List<String> tokenize(String expression) {
        List<String> result = new ArrayList<>();
        StringBuilder number = new StringBuilder();
        boolean isLastCharOperator = true;
        for (char c : expression.toCharArray()) {
            if (Arrays.asList(numbers).contains(c) || (c == '-' && isLastCharOperator)) {
                number.append(c);
                isLastCharOperator = false;
            } else if (Arrays.asList(operators).contains(c) || Arrays.asList(brackets).contains(c)) {
                if (!number.isEmpty()) {
                    result.add(number.toString());
                    number.setLength(0);
                }
                result.add(String.valueOf(c));
                isLastCharOperator = true;
            } else if (c != ' ') {
                throw new IllegalArgumentException("invalid token character: " + c);
            }
        }
        if (!number.isEmpty()) {
            result.add(number.toString());
        }
        return result;
    }

    /**
     * Check if the expression contains correct number of brackets
     * @param expression arithmetic expression
     * @return true if valid
     */
    public static boolean bracketsValid(String expression) {
        int counter = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                counter++;
            } else if (c == ')') {
                counter--;
            }
        }
        return counter == 0;
    }

    /**
     * Check if the expression contains valid sequence of operators
     * @param expression arithmetic expression
     * @return true if valid
     */
    public static boolean operatorsValid(String expression) {
        boolean isLastCharOperator = false;
        List<String> tokens = tokenize(expression);
        for (String token : tokens) {
            if (token.length() == 1 && Arrays.asList(operators).contains(token.charAt(0))) {
                if (isLastCharOperator) {
                    return false;
                }
                isLastCharOperator = true;
            } else {
                isLastCharOperator = false;
            }
        }
        return true;
    }

    /**
     * Check if the expression contains valid set of characters
     * @param expression arithmetic expression
     * @return true if valid
     */
    public static boolean charactersValid(String expression) {
        Pattern pattern = Pattern.compile("^[\\d\\-.+/*() ]*[^\\d\\-.+/*() ]+.*$");
        Matcher matcher = pattern.matcher(expression);
        return !matcher.matches();
    }

    /**
     * Total check
     * @param expression arithmetic expression
     * @return true if valid
     */
    public static boolean isValid(String expression) {
        return charactersValid(expression) && bracketsValid(expression) && operatorsValid(expression);
    }
}
