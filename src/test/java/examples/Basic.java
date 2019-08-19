package examples;

import base.dl.methodgenerators.InsertObject;
import base.dl.methodgenerators.InsertRaw;
import base.model.methodgenerators.AttributeBasedFromElemMethodGenerator;
import base.model.methodgenerators.ConstructorGenerator;
import base.model.methodgenerators.DerivedModelConstructorGenerator;
import base.parsergen.rules.SourceFiles;
import base.v3.AbstractApplicationBuilder;
import base.v3.XMLApplicationBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Take an XML document and build application
 */
public class Basic {


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


        // Auto-generate methods for the generated classes
        appBuilder
                // Generate method to extract ElemModel from XML block
                .addElemModelMethods(new AttributeBasedFromElemMethodGenerator())
                // Simple constructor that maps to the parsed elements
                .addElemModelMethods(new ConstructorGenerator())
                // Merged Model Constructor that takes an argument for each of its fields
                .addMergedModelMethod(new ConstructorGenerator())
                // Merged Model Constructor that take ElemModels as its arguments
                .addMergedModelMethod(new DerivedModelConstructorGenerator())
                // Data access layer insert method that takes an argument for each field
                .addMergedDLModelMethod(new InsertRaw())
                // Data access layer insert method that takes a Merged Model as its argument
                .addMergedDLModelMethod(new InsertObject());

        // Export Maven project with Elem, Merged Model and data layer classes along with supporting class and SQL
        // schema file defining the backing tables for the Merged Models
        appBuilder.build();
        System.out.println("In '" + exportDir + "' perform mvn compile");
    }
}
