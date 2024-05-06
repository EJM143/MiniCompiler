import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Lexer class provides functionality to tokenize source code
 * It reads input from a file, tokenizes it, and writes the
 * resulting tokens to another file.
 */
public class Lexer {
    private int line;
    private int pos;
    private int position;
    private char chr;
    private String s;

    private TokenType prevToken;

    // Mapping of keywords to their corresponding token types
    Map<String, TokenType> keywords = new HashMap<>();

    /**
     * Represents a token with its type, value, line, and position.
     */
    static class Token {
        public TokenType tokentype;
        public String value;
        public int line;
        public int pos;

        /**
         * Constructs a new Token Object.
         * @param token     Type of Token
         * @param value     Value of Token
         * @param line      Line number where the token is
         * @param pos       Position where token is
         */
        Token(TokenType token, String value, int line, int pos) {
            this.tokentype = token; this.value = value; this.line = line; this.pos = pos;
        }

        /**
         * Converts the Token Object to string
         * @return      String of the Token Object
         */
        @Override
        public String toString() {
            String result = String.format("%5d  %5d %-15s", this.line, this.pos, this.tokentype);
            switch (this.tokentype) {
                case Integer:
                    result += String.format("  %4s", value);
                    break;
                case Identifier:
                    result += String.format(" %s", value);
                    break;
                case String:
                    result += String.format(" \"%s\"", value);
                    break;
            }
            return result;
        }
    }
    // Enum for different types of tokens
    static enum TokenType {
        End_of_input, Op_multiply,  Op_divide, Op_mod, Op_add, Op_subtract,
        Op_negate, Op_not, Op_less, Op_lessequal, Op_greater, Op_greaterequal,
        Op_equal, Op_notequal, Op_assign, Op_and, Op_or, Keyword_if,
        Keyword_else, Keyword_while, Keyword_print, Keyword_putc, LeftParen, RightParen,
        LeftBrace, RightBrace, Semicolon, Comma, Identifier, Integer, String
    }

    /**
     * Handles errors during tokenization
     * @param line      Line number where the error is
     * @param pos       Position where the error is
     * @param msg       Error message
     */
    static void error(int line, int pos, String msg) {
        if (line > 0 && pos > 0) {
            System.out.printf("%s in line %d, pos %d\n", msg, line, pos);
        } else {
            System.out.println(msg);
        }
        System.exit(1);
    }

    /**
     * Constructs a Lexer Object with the given source code
     * @param source    Source code string to tokenize
     */
    Lexer(String source) {
        this.line = 1;
        this.pos = 0;
        this.position = 0;
        this.s = source;
        this.chr = this.s.charAt(0);
        this.keywords.put("if", TokenType.Keyword_if);
        this.keywords.put("else", TokenType.Keyword_else);
        this.keywords.put("print", TokenType.Keyword_print);
        this.keywords.put("putc", TokenType.Keyword_putc);
        this.keywords.put("while", TokenType.Keyword_while);

    }

    /**
     * Follows a character in the input stream and returns the token
     * @param expect    Expected character
     * @param ifyes     If character matches
     * @param ifno      If character doesnt match
     * @param line      Line number where the token is
     * @param pos       Position where the token is
     * @return          The token to the character
     */
    Token follow(char expect, TokenType ifyes, TokenType ifno, int line, int pos) {
        if (getNextChar() == expect) {
            getNextChar();
            return new Token(ifyes, "", line, pos);
        }
        if (ifno == TokenType.End_of_input) {
            error(line, pos, String.format("follow: unrecognized character: (%d) '%c'", (int)this.chr, this.chr));
        }
        return new Token(ifno, "", line, pos);
    }

    /**
     * Handles character literals
     * @param line      Line number where the character literal is
     * @param pos       Position where the character literal is
     * @return          Token representing the character literal
     */
    Token char_lit(int line, int pos) { // handle character literals
        char c = getNextChar(); // skip opening quote
        int n = (int)c;
        // code here
        if(c == '\\'){
            c = getNextChar();
            switch (c) {
                case 'n' : n = 10;
                            break;
                case '\\': n = 92;
                    break;
            }
        }
        getNextChar();
        getNextChar();
        return new Token(TokenType.Integer, "" + n, line, pos);
    }

    /**
     * Handles string literals
     * @param start     Starting quote character of the string literal
     * @param line      Line number where the string literal is
     * @param pos       Position where the string literal is
     * @return          Token representing the string literal
     */
    Token string_lit(char start, int line, int pos) { // handle string literals
        String result = "";
        // code here
        getNextChar();
        while (this.chr != '\"' ) {
            result += this.chr;
            getNextChar();
        }

        getNextChar();
        return new Token(TokenType.String, result, line, pos);
    }
    // example
    /* example 2 */

    /**
     * Handles division or comments
     * @param line  Line number where the division or comment is
     * @param pos   Position where the division or comment is
     * @return      Token representing the division or comment
     */
    Token div_or_comment(int line, int pos) { // handle division or comments
        // code here
        getNextChar();
        if(this.chr == '/'){
            while (this.chr != '\n')
                getNextChar();
        }else if(this.chr == '*'){
            getNextChar();
            while(true)
            {
                if (this.chr == '*' ) {
                    getNextChar();
                    if(this.chr == '/') {
                        getNextChar();
                        break;
                    }
                }
                getNextChar();
            }
        }else{
            return new Token(TokenType.Op_divide, "", line, pos);
        }
        return getToken();
    }

    /**
     * Handles identifiers and integers
     * @param line  Line number where the identifier and integer is
     * @param pos   Position where the identifier and integer is
     * @return      Token representing the identifier and integer
     */
    Token identifier_or_integer(int line, int pos) { // handle identifiers and integers
        boolean is_number = true;
        String text = "";
        // code here

        while (Character.isLetterOrDigit(this.chr)   ||  this.chr == '_' ) {
            text += this.chr;
            getNextChar();
        }
        if(text.matches("[0-9]+")){
            return new Token(TokenType.Integer, text, line, pos);
        }else if(this.keywords.containsKey(text)){
            return new Token(this.keywords.get(text), text, line, pos);
        }
        else{
            return new Token(TokenType.Identifier, text, line, pos);
        }
    }

    /**
     * Gets the next token from the source code
     * @return      The next token in the source code
     */
    Token getToken() {
        int line, pos;
        while (Character.isWhitespace(this.chr)) {
            getNextChar();
        }
        line = this.line;
        pos = this.pos;
        Token t = null;
        // switch statement on character for all forms of tokens with return to follow.... one example left for you

        switch (this.chr) {
            case '\u0000': return new Token(TokenType.End_of_input, "", this.line, this.pos);
            // remaining case statements
            case '=' :
                t = follow('=', TokenType.Op_equal, TokenType.Op_assign, line, pos);
                getNextChar();
                return t;
            case '<':
                t = follow('=', TokenType.Op_lessequal, TokenType.Op_less, line, pos);
                getNextChar();
                return t;
            case '>':
                t = follow('=', TokenType.Op_greaterequal, TokenType.Op_greater, line, pos);
                getNextChar();
                return t;
            case '!':
                t = follow('=', TokenType.Op_notequal, TokenType.Op_not, line, pos);
                getNextChar();
                return t;
            case '*' :
                t =new Token(TokenType.Op_multiply, "", line, pos);
                getNextChar();
                return t;
            case ';':
                t = new Token(TokenType.Semicolon, "", line, pos);
                getNextChar();
                return t;
            case '(':
                t = new Token(TokenType.LeftParen, "", line, pos);
                getNextChar();
                return t;
            case '-': //
                if(this.prevToken == TokenType.Integer || this.prevToken == TokenType.Identifier)
                    t = new Token(TokenType.Op_subtract, "", line, pos);
                else
                    t = new Token(TokenType.Op_negate, "", line, pos);
                getNextChar();
                return t;
            case ')':
                t = new Token(TokenType.RightParen, "", line, pos);
                getNextChar();
                return t;
                // TODO:complete '(' if needed.
            case '{':
                t = new Token(TokenType.LeftBrace, "", line, pos);
                getNextChar();
                return t;
            case '}':
                t = new Token(TokenType.RightBrace, "", line, pos);
                getNextChar();
                return t;
            case ',':
                t = new Token(TokenType.Comma, "", line, pos);
                getNextChar();
                return t;
            case '+':
                t = new Token(TokenType.Op_add, "", line, pos);
                getNextChar();
                return t;
            case '%':
                t = new Token(TokenType.Op_mod, "", line, pos);
                getNextChar();
                return t;
            case '&':
                t = new Token(TokenType.Op_and, "", line, pos);
                getNextChar();
                getNextChar();
                return t;
            case '|':
                t = new Token(TokenType.Op_or, "", line, pos);
                getNextChar();
                getNextChar();
                return t;
            case '\'': // For '
                return char_lit(line, pos);
            case '\"':  // for "
                return string_lit(this.chr, line,pos);
            case '/': // For /
                return div_or_comment(line, pos);
            default:
                return identifier_or_integer(line, pos);
        }
    }

    /**
     * Gets the next character from the input stream
     * @return      The next character in the input stream
     */
    char getNextChar() {
        this.pos++;
        this.position++;
        if (this.position >= this.s.length()) {
            this.chr = '\u0000';
            return this.chr;
        }
        this.chr = this.s.charAt(this.position);
        if (this.chr == '\n') {
            this.line++;
            this.pos = 0;
        }
        return this.chr;
    }

    /**
     * Prints tokens found in the source code
     * @return      String containing all the tokens
     */
    String printTokens() {
        Token t;
        StringBuilder sb = new StringBuilder();
        while ((t = getToken()).tokentype != TokenType.End_of_input) {
            sb.append(t);
            sb.append("\n");
            System.out.println(t);
            this.prevToken = t.tokentype;
        }
        sb.append(t);
        System.out.println(t);
        return sb.toString();
    }

    /**
     * Outputs the result to a file
     * @param result    The result to be written
     */
    static void outputToFile(String result) {
        try {
            FileWriter myWriter = new FileWriter("src/main/resources/hello1.lex");
            myWriter.write(result);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        if (1==1) {
            try {

                File f = new File("src/main/resources/count.c");
                Scanner s = new Scanner(f);
                String source = " ";
                String result = " ";
                while (s.hasNext()) {
                    source += s.nextLine() + "\n";
                }
                Lexer l = new Lexer(source);
                result = l.printTokens();

                outputToFile(result);

            } catch(FileNotFoundException e) {
                error(-1, -1, "Exception: " + e.getMessage());
            }
        } else {
            error(-1, -1, "No args");
        }
    }
}

