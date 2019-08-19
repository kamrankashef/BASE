package examples;

import base.parsergen.rules.SourceFiles;
import base.v3.AbstractApplicationBuilder;
import base.v3.XMLApplicationBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Not too interesting, because we are doing the very minimum and not specifying model and datalayer methods to generate
 */
public class MostBasic {


    public static void main(String[] args) throws IOException, InterruptedException {

        // The only three required properties
        // 1. org - package for generated classes
        // 2. exportDir - where project should be generated - careful, this will wipe out the directory, so don't use $HOME
        // 3. sourceFiles - StreamGetters that provide InputStreams to the sample datafiles
        final String org = "com.example";
        final String exportDir = System.getProperty("user.home") + "/sample_base_project";
        final SourceFiles sourceFiles = new SourceFiles();

        {
            final String pathToInputFile = args[0];
            SourceFiles.StreamGetter streamGetter = () -> new FileInputStream(pathToInputFile);
            sourceFiles.addSourceFile("ExampleType", streamGetter);
        }

        // Create an XML builder
        final AbstractApplicationBuilder appBuilder = new XMLApplicationBuilder("com.example", sourceFiles, exportDir);

        appBuilder.build();
        System.out.println("In '" + exportDir + "' perform mvn compile");
    }
}
