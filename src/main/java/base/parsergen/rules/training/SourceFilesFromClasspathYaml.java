package base.parsergen.rules.training;

import base.parsergen.rules.SourceFiles;
import base.workflow.Helpers;
import com.google.gson.annotations.SerializedName;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;

public class SourceFilesFromClasspathYaml implements SourceFilesI {

    public static SourceFilesFromClasspathYaml constructFromCode(final Class clazz, final String fileName) throws FileNotFoundException {
        final String pathToPackage = Helpers.absolutePathToPackage(clazz, fileName);
        SourceFiles sf = new Yaml().loadAs(new FileInputStream(pathToPackage + "/" + fileName), SourceFiles.class);
        SourceFilesFromClasspathYaml sourceFiles = new Yaml().loadAs(new FileInputStream(pathToPackage + "/" + fileName), SourceFilesFromClasspathYaml.class);
        sourceFiles.setAbsolutePathPrefix(pathToPackage);
        return sourceFiles;
    }
    public SourceFilesFromClasspathYaml() {
        sourceFiles = null;
        rootDir = null;
    }

    public static class SourceFileFromClasspathYaml implements SourceFileI {

        private String absolutePathPrefix = null;

        public SourceFileFromClasspathYaml(String fileName, String type) {
            this.fileName = fileName;
            this.type = type;
        }

        public SourceFileFromClasspathYaml() {
            this(null, null);
        }

        @SerializedName("fileName")
        public final String fileName;
        @SerializedName("type")
        public final String type;

        @Override
        public InputStream getSourceFile() throws FileNotFoundException {
            final String path = absolutePathPrefix
                    + "/" + fileName;

            System.out.println("path is " + path);
            return new FileInputStream(path);
        }

        @Override
        public String getType() {
            return type;
        }
    }

    @SerializedName("source_files")
    public Collection<SourceFileFromClasspathYaml> sourceFiles;
    @SerializedName("root_dir")
    public String rootDir;

    String absolutePathPrefix = "";

    private void setAbsolutePathPrefix(final String prefix) {
        this.absolutePathPrefix = prefix;
    }

    @Override
    public Collection<SourceFileI> getFiles() {
        final Collection<SourceFileI> sourceFiles = new LinkedList<>();
        for(final SourceFileFromClasspathYaml sourceFile : this.sourceFiles) {
            sourceFile.absolutePathPrefix = this.absolutePathPrefix + "/" + rootDir;
            sourceFiles.add(sourceFile);
        }
        return sourceFiles;
    }
}
