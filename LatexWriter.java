// STEP 4: turn the expression tree into a LaTeX String, using RECURSION.
//
// Recursion means the method calls itself. To convert a node, we first
// convert its left child and right child (by calling toLatex on them),
// then glue the results together.
//
// Note: in a Java String, "\\" is how you write ONE backslash,
// because a single \ has a special meaning (like \n for a new line).
// So "\\frac" in the code prints as \frac.
public class LatexWriter {
    public static String toLatex(ExprNode node) {
        Token token = node.getToken();
        TokenType type = token.getType();

        // BASE CASE: a number or variable has no children, so just return its text.
        // Every recursive method needs a base case, or it would call itself forever.
        if (type == TokenType.NUMBER || type == TokenType.VARIABLE) {
            return token.getText();
        }

        // sqrt: convert what's inside, then wrap it in \sqrt{ }.
        if (type == TokenType.FUNCTION) {
            return "\\sqrt{" + toLatex(node.getLeft()) + "}";
        }

        // Otherwise it's an operator with a left and a right child.
        String op = token.getText();
        ExprNode leftNode = node.getLeft();
        ExprNode rightNode = node.getRight();
        String left = toLatex(leftNode);     // RECURSIVE CALL on the left child
        String right = toLatex(rightNode);   // RECURSIVE CALL on the right child

        if (op.equals("/")) {
            // Division becomes a fraction: left on top, right on the bottom.
            // No brackets needed, because the fraction bar already groups things.
            return "\\frac{" + left + "}{" + right + "}";
        }

        if (op.equals("^")) {
            // If the base is itself a calculation, like (x+1)^2, it needs brackets.
            if (isOperator(leftNode)) {
                left = wrap(left);
            }
            // The exponent goes in { } so multi-character exponents stay together: x^{10}
            return left + "^{" + right + "}";
        }

        if (op.equals("*")) {
            // (a+b)*c needs brackets around a+b, or it would read as a + b*c.
            if (isPlusOrMinus(leftNode)) {
                left = wrap(left);
            }
            if (isPlusOrMinus(rightNode)) {
                right = wrap(right);
            }
            return left + " \\cdot " + right;   // \cdot is a centred multiplication dot
        }

        if (op.equals("-")) {
            // a-(b+c) needs brackets on the right side, or it would read as a - b + c.
            if (isPlusOrMinus(rightNode)) {
                right = wrap(right);
            }
            return left + " - " + right;
        }

        // The only operator left is +, which never needs brackets.
        return left + " + " + right;
    }

    // Is this node an operator (+ - * / ^)?
    private static boolean isOperator(ExprNode node) {
        return node.getToken().getType() == TokenType.OPERATOR;
    }

    // Is this node a + or a - ?
    private static boolean isPlusOrMinus(ExprNode node) {
        if (!isOperator(node)) {
            return false;
        }
        String op = node.getToken().getText();
        return op.equals("+") || op.equals("-");
    }

    // Put LaTeX brackets around a String. \left( and \right) grow to fit what's inside.
    private static String wrap(String s) {
        return "\\left( " + s + " \\right)";
    }
}
