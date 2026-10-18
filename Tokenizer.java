import java.util.ArrayList;   // ArrayList is a list that grows automatically as you add things

// STEP 1: the Tokenizer splits the input String into a list of Tokens.
// Example: "(x^2 + 1)"  becomes  [(] [x] [^] [2] [+] [1] [)]
public class Tokenizer {
    // static: you call it as Tokenizer.tokenize(...) without making a Tokenizer object.
    // throws: it can throw our checked exception if it finds a character it doesn't know.
    public static ArrayList<Token> tokenize(String input) throws MathSyntaxException {
        ArrayList<Token> tokens = new ArrayList<Token>();   // the list we'll fill and return
        int i = 0;                                         // i is our position in the String

        // Keep going until we've looked at every character.
        while (i < input.length()) {
            char c = input.charAt(i);   // the character at position i

            if (c == ' ') {
                // Spaces don't mean anything, so skip them.
                i++;
            }
            else if (Character.isDigit(c) || c == '.') {
                // A number can be more than one character, like 125 or 2.5,
                // so keep reading while we still see digits or a dot.
                int start = i;   // remember where the number started
                while (i < input.length() && (Character.isDigit(input.charAt(i)) || input.charAt(i) == '.')) {
                    i++;
                }
                // substring(start, i) gives the characters from start up to (not including) i.
                tokens.add(new Token(TokenType.NUMBER, input.substring(start, i)));
            }
            else if (Character.isLetter(c)) {
                // Read the whole word, like "x" or "sqrt".
                int start = i;
                while (i < input.length() && Character.isLetter(input.charAt(i))) {
                    i++;
                }
                String word = input.substring(start, i);

                if (word.equals("sqrt")) {
                    // sqrt is a function.
                    tokens.add(new Token(TokenType.FUNCTION, word));
                }
                else if (word.length() == 1) {
                    // A single letter is a variable.
                    tokens.add(new Token(TokenType.VARIABLE, word));
                }
                else {
                    // Any other word (like "hello") isn't supported yet.
                    throw new MathSyntaxException("Unknown word: " + word);
                }
            }
            else if ("+-*/^".indexOf(c) != -1) {
                // indexOf returns -1 if c is NOT in the String "+-*/^".
                // So this branch runs when c is one of our five operators.
                // String.valueOf(c) turns the char into a String.
                tokens.add(new Token(TokenType.OPERATOR, String.valueOf(c)));
                i++;
            }
            else if (c == '(') {
                tokens.add(new Token(TokenType.LEFT_PAREN, "("));
                i++;
            }
            else if (c == ')') {
                tokens.add(new Token(TokenType.RIGHT_PAREN, ")"));
                i++;
            }
            else {
                // Anything else, like @ or #, is an error.
                throw new MathSyntaxException("Unexpected character: " + c);
            }
        }

        return tokens;
    }
}
