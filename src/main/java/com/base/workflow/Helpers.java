package com.base.workflow;

import com.base.parsergen.rules.SourceFiles;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Helpers {

    public static String absolutePathToPackage(final Class clazz, final String file) {
        final String pathToPackage = clazz.getPackage().getName().replace(".", "/");
        System.out.println(pathToPackage);
        final String pathToFile = clazz.getClassLoader().getResource(pathToPackage + "/" + file).getFile();
        return pathToFile.substring(0, pathToFile.length() - file.length() - 1);
    }

    public static String classToTestPath(final Class clazz, final String fileName) {
        final String pathToPackage = clazz.getPackage().getName().replace(".", "/");
        System.out.println(pathToPackage);
        final String path = "target/test-classes/" + pathToPackage + "/" + fileName;
        System.out.println(path);
        final String pathToFile = clazz.getClassLoader().getResource(fileName).getFile();
        return pathToFile;
    }

    public static SourceFiles classToSrcDir(final Class c, final String fileName) throws FileNotFoundException {

        final String pathToPackage = c.getPackage().getName().replace(".", "/");
        final String pathToYml = c.getClassLoader().getResource(pathToPackage + "/" + fileName).getFile();
        File file = new File(pathToYml);

        return getSourceFilesCode(file.getAbsolutePath());
    }

    public static SourceFiles classToTestDir(final Class c, final String fileName) throws FileNotFoundException {
        return Helpers.classToDir("test", c, fileName);
    }

    public static SourceFiles classToDir(final String parent, final Class c, final String fileName) throws FileNotFoundException {
        final String path = parent + "/" + c.getPackage().getName().replace(".", "/") + "/" + fileName;
        return Helpers.getSourceFilesCode(path);
    }

    private static SourceFiles getSourceFilesCode(final String path) throws FileNotFoundException {
        final InputStream inputStream = new FileInputStream(path);
        SourceFiles sourceFiles = new Yaml().loadAs(inputStream, SourceFiles.class);
        return sourceFiles;
    }

    private static SourceFiles getSourceFilesCode(final String path, final boolean inCode) throws FileNotFoundException {
        final InputStream inputStream = new FileInputStream(path);
        SourceFiles sourceFiles = new Yaml().loadAs(inputStream, SourceFiles.class);
        return sourceFiles;
    }
}
