// Our own exception, thrown when the user types math we can't understand,
// like "(x + 1" (missing a bracket) or "3 + * 4" (two operators in a row).
//
// It extends Exception (NOT RuntimeException), so it is a CHECKED exception.
// That means every method that can throw it must say "throws MathSyntaxException"
// in its header, and Main must catch it. The compiler checks this for us.
public class MathSyntaxException extends Exception {
    // Constructor: takes the error message, like "Missing closing bracket".
    public MathSyntaxException(String message) {
        super(message);   // pass the message up to Exception, so e.getMessage() returns it
    }
}
