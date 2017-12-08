/***
 * Compiler main method.
 * Eliminated deprecated references used by ANTLR book.
 * K. Weber weberk@mountunion.edu 3-nov-2017
 *
***/
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.*;

public class Compiler {
    public static void main(String[] args) throws Exception {
        CharStream in = (args.length > 0)
            ? CharStreams.fromFileName(args[0])
            : CharStreams.fromStream(System.in);
        new ParseTreeWalker().walk(
            new CompilerListener(),
            new MountCParser(new CommonTokenStream(new MountCLexer(in))).program()
        );
    }
}
