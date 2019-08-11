package base.workflow;

import base.parsergen.rules.SourceFiles;
import kamserverutils.common.util.IOUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

/**
 * TODO This needs to be refactored and deep cleaned.
 */
public class Helpers {

    public static String absolutePathToPackage(final Class clazz, final String fileName) {
        final String pathToPackage = clazz.getPackage().getName().replace(".", "/");
        final String path =  pathToPackage + "/" + fileName;
        return new File(clazz.getClassLoader().getResource(path).getFile()).getParent();
    }


    // Why "test" in prefix
    public static String resourceAsString(final Class clazz, final String fileName) throws IOException {
        return IOUtil.inputStreamToString(clazz.getClassLoader().getResourceAsStream(fileName));
    }

    public static String classToTestPath(final Class clazz, final String fileName) {
        final String pathToPackage = clazz.getPackage().getName().replace(".", "/");
        final String path = "target/test-classes/" + pathToPackage + "/" + fileName;
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
