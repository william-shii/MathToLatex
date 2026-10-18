// An "enum" is a type that can only be one value from a fixed list.
// Every Token in this project has exactly one of these six types.
public enum TokenType {
    NUMBER,       // a number, like 3 or 2.5
    VARIABLE,     // a single letter, like x
    OPERATOR,     // one of + - * / ^
    FUNCTION,     // a named function; right now only sqrt
    LEFT_PAREN,   // an opening bracket (
    RIGHT_PAREN   // a closing bracket )
}
