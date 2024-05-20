import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;
import src/main/java/Parser.*;
class ParserTest {

    @Test
    pubcli void testIsPrimeLex() throws Exception {
        File ogFile = new File("src/main/resources/isPrime.lex");
        Scanner s = new Scanner(ogFile);
        String lexOut = " ";
        Map<String, Parser.TokenType> tokenMap = Parser.createHashMap();
        List<Parser.Token> tokenList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        Main.updateTokenList(tokenList, tokenMap, s);
        Parser testParser = new Parser(tokenList);
        Parser.Node root = testParser.parse();
        String output = testParser.printAST(root, sb);
        FileWriter fileWrite = new FileWriter("src/main/resources/isPrimeTest.par");
        Parser.outputToFile(output,fileWrite);
        File expectedFile = new File("src/main/resources/testPrimeExpected.par");
        File testFile = new File("src/main/resources/isPrimeTest.par");
        Scanner scan2 = new Scanner(expectedFile);
        StringBuilder correctOutput = new StringBuilder();
        StringBuilder actualOutput = new StringBuilder();
        while(scan2.hasNext()){
            String testString = scan2.nextLine();
            testString.replace("\\s", "");
            correctOutput.append(ts + "\n");
        }
        Scanner scan3 = new Scanner(testFile);
        while(scan3.hasNext()){
            String testString2 = scan3.nextLine();
            testString2.replace("\\s", "");
            actualOutput.append(ts + "\n");
        }
        String correctOut = correctOutput.toString();
        String actualOut = actualOutput.toString();
        assertEquals(correctOut, actualOut);
    }

    @Test
    public void testPrecedence(){
        List<Parser.Token>list = new ArrayList<>();
        list.add(new Parser.Token(Parser.TokenType.Identifier,"Count",1,1));
        list.add(new Parser.Token(Parser.TokenType.Op_assign,"=",1,2));
        list.add(new Parser.Token(Parser.TokenType.Integer,"3",1,3));
        list.add(new Parser.Token(Parser.TokenType.Op_multiply,"*",1,4));
        list.add(new Parser.Token(Parser.TokenType.Integer,"4",1,5));
        list.add(new Parser.Token(Parser.TokenType.Op_add,"+",1,6));
        list.add(new Parser.Token(Parser.TokenType.Integer,"6",1,7));
        list.add(new Parser.Token(Parser.TokenType.Semicolon,";",1,8));
        list.add(new Parser.Token(Parser.TokenType.End_of_input," ",1,9));
        Parser testParser = new Parser(list);
        Parser.Node root = testParser.parse();
        StringBuilder stringBuild = new StringBuilder();
        stringBuild.append("Sequence\n");
        stringBuild.append(";\n");
        stringBuild.append("Assign\n");
        stringBuild.append("Identifier Count\n");
        stringBuild.append("Add\n");
        stringBuild.append("Multiply\n");
        stringBuild.append("Integer 3\n");
        stringBuild.append("Integer 4\n");
        stringBuild.append("Integer 6\n");
        String expectedResult = testParser.toString();
        StringBuilder myStringBuilder = new StringBuilder();
        String result = testParser.printAST(root, myStringBuilder);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIf() {
        List<Parser.Token>list=new ArrayList<>();
        list.add(new Parser.Token(Parser.TokenType.Keyword_if,"Count",1,1));
        list.add(new Parser.Token(Parser.TokenType.LeftParen,"",1,2));
        list.add(new Parser.Token(Parser.TokenType.Identifier,"n",1,3));
        list.add(new Parser.Token(Parser.TokenType.RightParen,"",1,4));
        list.add(new Parser.Token(Parser.TokenType.Keyword_print,"",1,5));
        list.add(new Parser.Token(Parser.TokenType.LeftParen,"",1,6));
        list.add(new Parser.Token(Parser.TokenType.Identifier,"n",1,7));
        list.add(new Parser.Token(Parser.TokenType.Comma," " ,1,8));
        list.add(new Parser.Token(Parser.TokenType.String,"is a prime\n ",1,9));
        list.add(new Parser.Token(Parser.TokenType.RightParen, "", 1, 10));
        list.add(new Parser.Token(Parser.TokenType.Semicolon, ";", 1, 11));
        list.add(new Parser.Token(Parser.TokenType.End_of_input, "", 1, 12));
        StringBuilder testStrBld = new StringBuilder();
        testStrBld.append("Sequence\n");
        testStrBld.append(";\n");
        testStrBld.append("If\n");
        testStrBld.append("Identifier n\n");
        testStrBld.append("If\n");
        testStrBld.append("Sequence\n");
        testStrBld.append("Sequence\n");
        testStrBld.append(";\n");
        testStrBld.append("Prti\n");
        testStrBld.append("Identifier n\n");
        testStrBld.append(";\n");
        testStrBld.append("Prts\n");
        testStrBld.append("String is a prime\n");
        testStrBld.append(" \n");
        testStrBld.append(";\n");
        testStrBld.append(";\n");
        Parser testParser = new Parser(list);
        Parser.Node root = testParser.parse();
        String expectedResult = testStrBld.toString();
        StringBuilder resultSB = new StringBuilder();
        String result = testParser.printAST(root, resultSB);
        assertEquals(expectedResult, result);
    }

}