import java.util.ArrayList;
import java.util.Stack;   // Java's built-in stack: push() adds to the top, pop() removes from the top,
                          // peek() looks at the top without removing it

// STEPS 2 and 3: turn the list of Tokens into an expression tree.
public class Parser {
    // ---------------------------------------------------------------
    // STEP 2: infix -> postfix (the "shunting-yard" algorithm)
    //
    // Infix is how people write math:   x + 2 * 3
    // Postfix puts each operator AFTER the two things it works on:   x 2 3 * +
    // Postfix has no brackets and no "order of operations" to worry about,
    // because the order of the tokens already IS the order to do things in.
    // ---------------------------------------------------------------
    public static ArrayList<Token> toPostfix(ArrayList<Token> tokens) throws MathSyntaxException {
        ArrayList<Token> output = new ArrayList<Token>();   // the postfix result
        Stack<Token> stack = new Stack<Token>();            // holds operators, functions and "(" while we wait

        // "for each Token t in tokens"
        for (Token t : tokens) {
            TokenType type = t.getType();

            if (type == TokenType.NUMBER || type == TokenType.VARIABLE) {
                // Numbers and variables go straight to the output.
                output.add(t);
            }
            else if (type == TokenType.FUNCTION) {
                // sqrt waits on the stack until its closing bracket is found.
                stack.push(t);
            }
            else if (type == TokenType.OPERATOR) {
                // Before pushing this operator, move any operators on the stack that
                // should happen FIRST to the output. Example: in "2 * 3 + 1", when we
                // reach +, the * on the stack must happen before +, so it's output first.
                while (!stack.isEmpty()
                       && stack.peek().getType() == TokenType.OPERATOR
                       && shouldPopFirst(stack.peek(), t)) {
                    output.add(stack.pop());
                }
                stack.push(t);
            }
            else if (type == TokenType.LEFT_PAREN) {
                // "(" marks where a bracketed group starts.
                stack.push(t);
            }
            else if (type == TokenType.RIGHT_PAREN) {
                // Found ")": everything back to the matching "(" goes to the output.
                while (!stack.isEmpty() && stack.peek().getType() != TokenType.LEFT_PAREN) {
                    output.add(stack.pop());
                }

                // If the stack ran out without finding "(", there's an extra ")".
                if (stack.isEmpty()) {
                    throw new MathSyntaxException("Too many closing brackets");
                }

                stack.pop();   // throw away the "(" itself; brackets don't appear in postfix

                // If a function was waiting just before the "(", like sqrt( ... ),
                // its group is now complete, so output the function.
                if (!stack.isEmpty() && stack.peek().getType() == TokenType.FUNCTION) {
                    output.add(stack.pop());
                }
            }
        }

        // Input is finished: move whatever is left on the stack to the output.
        while (!stack.isEmpty()) {
            Token t = stack.pop();
            if (t.getType() == TokenType.LEFT_PAREN) {
                // A "(" that was never closed.
                throw new MathSyntaxException("Missing closing bracket");
            }
            output.add(t);
        }

        return output;
    }

    // Priority of each operator. A higher number means "do it first".
    // private: only this class needs it.
    private static int precedence(String op) {
        if (op.equals("^")) {
            return 3;
        }
        if (op.equals("*") || op.equals("/")) {
            return 2;
        }
        return 1;   // + and -
    }

    // Should the operator on top of the stack be output before we push the current one?
    private static boolean shouldPopFirst(Token top, Token current) {
        int topPrecedence = precedence(top.getText());
        int currentPrecedence = precedence(current.getText());

        if (current.getText().equals("^")) {
            // ^ is special: 2^3^2 means 2^(3^2), so we work right-to-left.
            // Only pop if the top is STRICTLY higher priority.
            return topPrecedence > currentPrecedence;
        }

        // Everything else works left-to-right: 8 - 3 - 1 means (8 - 3) - 1.
        // So pop if the top is higher OR EQUAL priority.
        return topPrecedence >= currentPrecedence;
    }

    // ---------------------------------------------------------------
    // STEP 3: postfix -> expression tree, using a stack of NODES
    //
    // Read the postfix left to right:
    //   - a number or variable: make a node and push it
    //   - an operator: pop two nodes, make them its children, push it back
    //   - sqrt: pop one node, make it the child, push it back
    // At the end, the one node left on the stack is the root of the tree.
    // ---------------------------------------------------------------
    public static ExprNode buildTree(ArrayList<Token> postfix) throws MathSyntaxException {
        Stack<ExprNode> stack = new Stack<ExprNode>();

        for (Token t : postfix) {
            ExprNode node = new ExprNode(t);   // every token becomes a node
            TokenType type = t.getType();

            if (type == TokenType.NUMBER || type == TokenType.VARIABLE) {
                stack.push(node);
            }
            else if (type == TokenType.FUNCTION) {
                if (stack.isEmpty()) {
                    throw new MathSyntaxException("sqrt needs something inside the brackets");
                }
                node.setLeft(stack.pop());   // sqrt has one child; we store it on the left
                stack.push(node);
            }
            else {
                // An operator needs TWO things to work on.
                if (stack.size() < 2) {
                    throw new MathSyntaxException("The operator " + t.getText() + " is missing a value on one side");
                }
                ExprNode right = stack.pop();   // popped FIRST, because it was pushed LAST
                ExprNode left = stack.pop();
                node.setLeft(left);
                node.setRight(right);
                stack.push(node);
            }
        }

        if (stack.isEmpty()) {
            throw new MathSyntaxException("Nothing to convert");
        }
        if (stack.size() > 1) {
            // Two values with nothing joining them, like "2 3" or "2x".
            throw new MathSyntaxException("Missing an operator between two values (write 2*x, not 2x)");
        }

        return stack.pop();   // the root of the whole tree
    }
}
