import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;

public class InFix {
    private Stack<Character> operatorStack;
    private static final String OPERATORS = "+-*/()";
    private static final int[] precedence = {1, 1, 2, 2, -1, -1};
    private StringBuilder postfix;

    public String convert(String infix) throws SyntaxErrorException {
        operatorStack = new Stack<Character>();
        postfix = new StringBuilder();
        try {
            String nextToken;
            Scanner scan = new Scanner(infix);
            //kör när scannern hittar någonting innanför unicode alfabetet som är
            // förjlt av siffror som är föjlt av en operator
            while ((nextToken = scan.findInLine("\\p{L}+|\\d+|[-+/\\*()]")) != null) {
                char firstChar = nextToken.charAt(0);
                if (Character.isJavaIdentifierStart(firstChar) || Character.isDigit(firstChar)) {
                    postfix.append(nextToken);
                    postfix.append(' ');
                } else if (isOperator(firstChar)) {
                    processOperator(firstChar);
                } else {
                    throw new SyntaxErrorException("Unexpected Character Encountered: " + firstChar);
                }
            }
            while (!operatorStack.empty()) {
                char op = operatorStack.pop();
                if (op == '(') {
                    throw new SyntaxErrorException("Unmatched opening parenthesis");
                }
                postfix.append(op);
                postfix.append(' ');
            }
            return postfix.toString();
        } catch (EmptyStackException ex) {
            throw new SyntaxErrorException("Syntax Error: The stack is Empty");
        }
    }

    private void processOperator(char op) throws SyntaxErrorException {
        if (operatorStack.empty() || op == '(') {
            operatorStack.push(op);
        } else {
            char topOp = operatorStack.peek();
            if (op == ')') {
                while (!operatorStack.empty() && topOp != '(') {
                    operatorStack.pop();
                    postfix.append(topOp);
                    postfix.append(' ');
                    if (!operatorStack.empty()) {
                        topOp = operatorStack.peek();
                    }
                }
                if (!operatorStack.empty() && topOp == '(') {
                    operatorStack.pop(); // Pop '('
                } else {
                    throw new SyntaxErrorException("Unmatched closing parenthesis");
                }
            } else {
                while (!operatorStack.empty() && precedence(op) <= precedence(topOp) && topOp != '(') {
                    operatorStack.pop();
                    postfix.append(topOp);
                    postfix.append(' ');
                    if (!operatorStack.empty()) {
                        topOp = operatorStack.peek();
                    }
                }
                operatorStack.push(op);
            }
        }
    }

    private boolean isOperator(char ch) {
        return OPERATORS.indexOf(ch) != -1;
    }

    private int precedence(char op) {
        return OPERATORS.indexOf(op);
    }

    public static class SyntaxErrorException extends Exception {
        public SyntaxErrorException(String message) {
            super(message);
        }
    }
}