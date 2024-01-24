import java.util.EmptyStackException;
import java.util.Stack;

public class Postfix {
    private Stack<Integer> operandStack;
    private static final String OPERATORS="+-*/";

    public int eval(String expression) throws SyntaxErrorException {
        operandStack = new Stack<>();

        String[] tokens = expression.split("\\s+");

        try {
            for (String nextToken : tokens) {
                if (Character.isDigit(nextToken.charAt(0))) {
                    operandStack.push(Integer.parseInt(nextToken));
                } else if (isOperator(nextToken.charAt(0))) {
                    int result = evalOp(nextToken.charAt(0));
                    operandStack.push(result);
                }
            }

            if (operandStack.size() == 1) {
                return operandStack.pop();
            } else {
                throw new SyntaxErrorException("Invalid expression");
            }
        } catch (EmptyStackException | NumberFormatException e) {
            throw new SyntaxErrorException("Invalid expression");
        }
    }

    private boolean isOperator(char ch) {
        return OPERATORS.indexOf(ch)!=-1;
    }

    private int evalOp(char operator) {
        int rightOperand = operandStack.pop();
        int leftOperand = operandStack.pop();

        return switch (operator) {
            case '+' -> leftOperand + rightOperand;
            case '-' -> leftOperand - rightOperand;
            case '*' -> leftOperand * rightOperand;
            case '/' -> leftOperand / rightOperand;
            default -> 0;
        };
    }

    public static class SyntaxErrorException extends Exception {
        public SyntaxErrorException(String message) {
            super(message);
        }
    }
}