package examples;

import base.parsergen.rules.SourceFiles;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MostBasic {



    // User prrovide path to source file
    public static void main(String[] args) throws FileNotFoundException {

        // 1. Get sample data file
        final SourceFiles sourceFiles = new SourceFiles();

        {
            final String pathToInputFile = args[0];
            SourceFiles.StreamGetter streamGetter = () -> new FileInputStream(pathToInputFile);
            sourceFiles.addSourceFile("foo", streamGetter);
        }

        // 2. Decide
    }
}
