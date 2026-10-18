// One node of the EXPRESSION TREE.
//
// It's just like LinearNode from the linked list lecture, except it has
// TWO links (left and right) instead of one (next).
//
// For "x + 1", the tree is:     +
//                              / \
//                             x   1
// The + node's left child is x and its right child is 1.
public class ExprNode {
    private Token token;      // the data: a number, variable, operator or function
    private ExprNode left;    // link to the left child (null if there isn't one)
    private ExprNode right;   // link to the right child (null if there isn't one)

    // A new node starts with no children, just like a new LinearNode starts with next = null.
    public ExprNode(Token token) {
        this.token = token;
        this.left = null;
        this.right = null;
    }

    public Token getToken() {
        return token;
    }

    public ExprNode getLeft() {
        return left;
    }

    public void setLeft(ExprNode node) {
        left = node;
    }

    public ExprNode getRight() {
        return right;
    }

    public void setRight(ExprNode node) {
        right = node;
    }
}
