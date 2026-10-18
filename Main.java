2 +import java.util.ArrayList;
import java.util.Scanner;   // Scanner reads what the user types

// The program starts here. It reads an expression, runs all four steps,
// and prints the result of each step so you can see how it works.
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);   // System.in is the keyboard

        System.out.println("Math to LaTeX");
        System.out.println("Type an expression like (x^2 + 1) / sqrt(x), or type quit to stop.");

        // Keep asking until the user types quit.
        while (true) {
            System.out.println();
            System.out.print("> ");

            // If there's no more input (e.g. the input was closed), stop.
            if (!scanner.hasNextLine()) {
                break;
            }

            String line = scanner.nextLine().trim();   // trim() removes spaces at the start and end

            if (line.equalsIgnoreCase("quit")) {
                break;   // leave the while loop
            }

            // MathSyntaxException is CHECKED, so the compiler forces us to catch it here.
            try {
                ArrayList<Token> tokens = Tokenizer.tokenize(line);     // Step 1
                System.out.println("Tokens:  " + tokens);

                ArrayList<Token> postfix = Parser.toPostfix(tokens);   // Step 2
                System.out.println("Postfix: " + postfix);

                ExprNode tree = Parser.buildTree(postfix);             // Step 3
                String latex = LatexWriter.toLatex(tree);              // Step 4
                System.out.println("LaTeX:   " + latex);
            }
            catch (MathSyntaxException e) {
                // If any step throws, Java jumps straight here and skips the remaining steps.
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
        System.out.println("Bye!");
    }
}
