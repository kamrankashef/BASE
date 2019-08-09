package base.parsergen.rules;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SourceFiles {

    public static class SourceFile {

        @SerializedName("file_name")
        public final String fileName;
        @SerializedName("type")
        public final String type;


        public SourceFile() {
            this(null, null);
        }

        public SourceFile(final String path,
                final String type) {
            this.fileName = path;
            this.type = type;
        }

    }

    @SerializedName("source_files")
    public final List<SourceFile> sourceFiles;
    @SerializedName("root_dir")
    public final String rootDir;

    public SourceFiles() {
        this.rootDir = null;
        this.sourceFiles = null;
    }

    public SourceFiles(final String rootDir) {
        this(null, null);
    }

    public SourceFiles(final String rootDir, final List<SourceFile> sourceFiles) {
        this.rootDir = rootDir;
        this.sourceFiles = sourceFiles;
    }

    public SourceFiles addSourceFile(final String path, final String type) {
        this.sourceFiles.add(new SourceFile(path, type));
        return this;
    }

    @Override
    public String toString() {
        return "SourceFiles{" + "sourceFiles=" + sourceFiles + ", rootDir=" + rootDir + '}';
    }

}
