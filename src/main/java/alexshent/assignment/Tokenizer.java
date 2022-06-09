package alexshent.assignment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Arithmetic expression tokenizer
 */
public class Tokenizer {
    private static final Set<Character> numberSet = new HashSet<>();
    private static final Set<Character> operatorsSet = new HashSet<>();
    private static final Set<Character> bracketsSet = new HashSet<>();

    static {
        numberSet.add('0');
        numberSet.add('1');
        numberSet.add('2');
        numberSet.add('3');
        numberSet.add('4');
        numberSet.add('5');
        numberSet.add('6');
        numberSet.add('7');
        numberSet.add('8');
        numberSet.add('9');
        numberSet.add('.');

        operatorsSet.add('+');
        operatorsSet.add('-');
        operatorsSet.add('/');
        operatorsSet.add('*');

        bracketsSet.add('(');
        bracketsSet.add(')');
    }

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
            if (numberSet.contains(c) || (c == '-' && isLastCharOperator)) {
                number.append(c);
                isLastCharOperator = false;
            } else if (operatorsSet.contains(c) || bracketsSet.contains(c)) {
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
            if (token.length() == 1 && operatorsSet.contains(token.charAt(0))) {
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
