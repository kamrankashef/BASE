package base.parsergen.rules;

import com.google.gson.annotations.SerializedName;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SourceFiles {

    public static enum FileType {

        @SerializedName("xml")
        XML(new String[]{"xml", "xsd"}),
        @SerializedName("csv")
        CSV(new String[]{"csv"}),
        @SerializedName("tab")
        TAB(new String[]{"tab", "txt"}),
        @SerializedName("json")
        JSON(new String[]{"json"});

        transient final String[] matchingExtensions;

        FileType(final String[] matchingExtensions) {
            this.matchingExtensions = matchingExtensions;
        }

        public static FileType fromFileName(final String fileName) {
            if (fileName == null) {
                return null;
            }
            for (final FileType fileType : values()) {
                for (final String extension : fileType.matchingExtensions) {
                    if (fileName.toLowerCase().endsWith(extension.toLowerCase())) {
                        return fileType;
                    }
                }
            }
            return null;
        }
    }

    public interface StreamGetter {
        public InputStream getStream() throws FileNotFoundException;
    }


    // TODO Refactor - this has reduced down to just a wrapper on StreamGetter
    public static class SourceFile {


        private final StreamGetter streamGetter;
        private final String type;

        public SourceFile(final String type, final StreamGetter streamGetter) {
            this.streamGetter = streamGetter;
            this.type = type;
        }

        public InputStream getInputStream() throws FileNotFoundException {
            return streamGetter.getStream();
        }
        public String getType() {
            return type;
        }
    }

    @SerializedName("source_files")
    public final List<SourceFile> sourceFiles;
    @SerializedName("root_dir")
    public final String rootDir;

    public SourceFiles() {
        this(null);
    }

    public SourceFiles(final String rootDir) {
        this.rootDir = rootDir;
        this. sourceFiles = new LinkedList<>();
    }

    /**
     * @deprecated  Use addSourceFile so the StreamGetter gets associated it
     * @param rootDir
     * @param sourceFiles
     */
    @Deprecated
    public SourceFiles(final String rootDir, final List<SourceFile> sourceFiles) {
        this.rootDir = rootDir;
        this.sourceFiles = sourceFiles;
    }

    public SourceFiles addSourceFile(final String type, final StreamGetter streamGetter) {
        this.sourceFiles.add(new SourceFile(type, streamGetter));
        return this;
    }

    @Override
    public String toString() {
        return "SourceFiles{" + "sourceFiles=" + sourceFiles + ", rootDir=" + rootDir + '}';
    }

}
