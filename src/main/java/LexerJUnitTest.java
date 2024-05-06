import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test for lexer
 * @author emiguel
 */
public class LexerJUnitTest {
    @Test
    void char_lit() {
        Lexer lexerTest = new Lexer ("\'\n\'");
        Lexer.Token t=lexerTest.char_lit(1, 1);
        assertEquals("10", t.value);

        lexerTest = new Lexer ("\'\\\'");
        t=lexerTest.char_lit(1, 1);
        assertEquals("92", t.value);

        lexerTest = new Lexer ("\'b\'");
        t=lexerTest.char_lit(1, 1);
        assertEquals("98", t.value);
    }
    @Test
    void string_lit() {
        Lexer lexerTest = new Lexer ("\"Anime Rocks\n\"");
        Lexer.Token t=lexerTest.string_lit('\"', 1, 1);
        assertEquals(("Anime Rocks\n"), t.value);

        lexerTest = new Lexer ("\"Carpe Diem!\n\"");
        t=lexerTest.string_lit('\"', 1, 1);
        assertEquals(("Carpe Diem!\n"), t.value);
    }


    @Test
    void getToken() {
        Lexer lexerTest = new Lexer ("=");
        Lexer.Token t=lexerTest.getToken();
        assertEquals("", t.value);
        lexerTest = new Lexer (">");
        t=lexerTest.getToken();
        assertEquals("", t.value);
        lexerTest = new Lexer ("!");
        t=lexerTest.getToken();
        assertEquals("", t.value);
        lexerTest = new Lexer ("*");
        t=lexerTest.getToken();
        assertEquals("", t.value);
    }
}


