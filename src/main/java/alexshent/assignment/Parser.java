package alexshent.assignment;

import java.util.*;

/**
 *  Parser using the Shunting-yard algorithm
 */

public class Parser {

    private record Operator(String token, int precedence, int associativity) {
        // Associativity constants for operators
        public static final int LEFT_ASSOC = 0;
        public static final int RIGHT_ASSOC = 1;
    }

    private static final Map<String, Operator> OPERATORS = new HashMap<>();

    static {
        OPERATORS.put("+", new Operator("+", 0, Operator.LEFT_ASSOC));
        OPERATORS.put("-", new Operator("-", 0, Operator.LEFT_ASSOC));
        OPERATORS.put("*", new Operator("*", 5, Operator.LEFT_ASSOC));
        OPERATORS.put("/", new Operator("/", 5, Operator.LEFT_ASSOC));
    }

    public static double evaluate(String expression) {
        if (!Tokenizer.bracketsValid(expression)) {
            throw new IllegalArgumentException("invalid number of brackets");
        }
        if (!Tokenizer.operatorsValid(expression)) {
            throw new IllegalArgumentException("invalid number of operators");
        }
        if (!Tokenizer.charactersValid(expression)) {
            throw new IllegalArgumentException("invalid characters");
        }
        List<String> tokens = Tokenizer.tokenize(expression);
        List<String> rpn = infixToRPN(tokens);
        return RPNtoDouble(rpn);
    }

    public static double RPNtoDouble(List<String> tokens) {
        if (tokens.size() == 0) {
            return 0.0;
        }
        Stack<String> stack = new Stack<>();
        for (String token : tokens) {
            // If the token is a value push it onto the stack
            if (!isOperator(token)) {
                stack.push(token);
            } else {
                // Token is an operator: pop top two entries
                Double d2 = Double.valueOf(stack.pop());
                Double d1 = Double.valueOf(stack.pop());

                Double result = switch (token) {
                    case "+" -> d1 + d2;
                    case "-" -> d1 - d2;
                    case "*" -> d1 * d2;
                    case "/" -> d1 / d2;
                    default -> 0.0;
                };

                stack.push(String.valueOf(result));
            }
        }
        return Double.parseDouble(stack.pop());
    }

    /**
     * Convert infix expression format into reverse Polish notation
     *
     * @param inputTokens list of expression tokens
     * @return list of tokens in RPN
     */
    public static List<String> infixToRPN(List<String> inputTokens) {
        List<String> result = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        for (String token : inputTokens) {
            // If token is an operator
            if (isOperator(token)) {
                // While stack not empty AND stack top element is an operator
                while (!stack.empty() && isOperator(stack.peek())) {
                    if ((isAssociative(token, Operator.LEFT_ASSOC) &&
                            cmpPrecedence(token, stack.peek()) <= 0) ||
                            (isAssociative(token, Operator.RIGHT_ASSOC) &&
                                    cmpPrecedence(token, stack.peek()) < 0)) {
                        result.add(stack.pop());
                        continue;
                    }
                    break;
                }
                // Push the new operator on the stack
                stack.push(token);
            }
            // If token is a left bracket '('
            else if (token.equals("(")) {
                stack.push(token);
            }
            // If token is a right bracket ')'
            else if (token.equals(")")) {
                while (!stack.empty() && !stack.peek().equals("(")) {
                    result.add(stack.pop());
                }
                stack.pop();
            }
            // If token is a number
            else {
                result.add(token);
            }
        }
        while (!stack.empty()) {
            result.add(stack.pop());
        }
        return result;
    }

    /**
     * Compare precedence of operators.
     *
     * @param token1 first operator token
     * @param token2 second operator token
     * @return precedence difference
     */
    private static int cmpPrecedence(String token1, String token2) {
        if (!isOperator(token1) || !isOperator(token2)) {
            throw new IllegalArgumentException("Invalid tokens: " + token1 + " " + token2);
        }
        return OPERATORS.get(token1).precedence() - OPERATORS.get(token2).precedence();
    }

    /**
     * Test if token is an operator
     *
     * @param token checked token
     * @return true if operator
     */
    private static boolean isOperator(String token) {
        return OPERATORS.containsKey(token);
    }

    /**
     * Test associativity of operator token
     *
     * @param token tested token
     * @param type associativity type
     * @return true if equal
     */
    private static boolean isAssociative(String token, int type) {
        if (!isOperator(token)) {
            throw new IllegalArgumentException("Invalid token: " + token);
        }
        return OPERATORS.get(token).associativity() == type;
    }
}
