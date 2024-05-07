
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static jdk.internal.org.jline.utils.Log.error;

public class MiniCompilerMain {
    public static void main(String[] args) {

        try {

            File f = new File("src/main/resources/testFile1.c");
            //File f = new File(fileName);
            Scanner s = new Scanner(f);
            String source = " ";
            while (s.hasNext()) {
                source += s.nextLine() + "\n";
            }
            String result = Lexer.LexerParser(source);
            //outputToFile(result);

            System.out.println("Lexical Output");
            System.out.println(result);

            String finalResult = Parser.ParseLexicalOutput(result);

            System.out.println("Parser Output");
            System.out.print(finalResult);

        } catch(FileNotFoundException e) {
            error(-1, -1, "Exception: " + e.getMessage());
        }

    }
}
