// A Token is ONE piece of the input.
// For example, "x + 12" is split into three Tokens: [x] [+] [12].
public class Token {
    // Instance variables: every Token object has its own copy of these.
    private TokenType type;   // what kind of piece it is (NUMBER, OPERATOR, ...)
    private String text;      // the actual characters, like "12" or "+"

    // Constructor: runs when you write new Token(TokenType.NUMBER, "12").
    public Token(TokenType type, String text) {
        this.type = type;     // "this.type" is the instance variable; "type" is the parameter
        this.text = text;     // same idea for text
    }

    // Getter: lets other classes READ the type (it's private, so they can't touch it directly).
    public TokenType getType() {
        return type;
    }

    // Getter for the text.
    public String getText() {
        return text;
    }

    // toString() is called automatically when you print a Token.
    // It makes System.out.println(token) show [12] instead of something like Token@1b6d3586.
    public String toString() {
        return "[" + text + "]";
    }
}
