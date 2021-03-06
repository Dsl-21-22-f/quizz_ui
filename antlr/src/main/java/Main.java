import io.github.dsl.teamf.antlr.ModelBuilder;
import io.github.dsl.teamf.antlr.StopErrorListener;
import io.github.dsl.teamf.antlr.grammar.*;
import io.github.dsl.teamf.kernel.App;
import io.github.dsl.teamf.kernel.generator.ToWiring;
import io.github.dsl.teamf.kernel.generator.Visitor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main (String[] args) throws Exception {
        System.out.println("\n\nRunning the ANTLR compiler for Quizz UI");

        CharStream stream = getCharStream(args);
        App theApp = buildModel(stream);
        //exportToCode(theApp);
        exportToFile(theApp);
    }

    private static CharStream getCharStream(String[] args) throws IOException {
        if (args.length < 1)
            throw new RuntimeException("no input file");
        Path input = Paths.get(new File(args[0]).toURI());
        System.out.println("Using input file: " + input);
        return CharStreams.fromPath(input);
    }

    private static App buildModel(CharStream stream) {
        QuizzLexer lexer   = new QuizzLexer(stream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new StopErrorListener());

        QuizzParser parser  = new QuizzParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(new StopErrorListener());

        ParseTreeWalker   walker  = new ParseTreeWalker();
        ModelBuilder      builder = new ModelBuilder();

        walker.walk(builder, parser.root()); // parser.root() is the entry point of the grammar

        return builder.retrieve();
    }

    private static void exportToCode(App theApp) {
        Visitor codeGenerator = new ToWiring();
        theApp.accept(codeGenerator);
        System.out.println(codeGenerator.getResult());
    }

    private static void exportToFile(App theApp) throws IOException {
        Visitor<StringBuffer> codeGenerator = new ToWiring();
        theApp.accept(codeGenerator);

        BufferedWriter writer = new BufferedWriter(new FileWriter("output/src/App.js"));
        StringBuffer buffer = codeGenerator.getResult();
        writer.write(buffer.toString());
        System.out.println(buffer);
        writer.close();
    }

}
