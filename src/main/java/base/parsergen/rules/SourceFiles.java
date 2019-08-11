package base.parsergen.rules;

import com.google.gson.annotations.SerializedName;

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
        public InputStream getStream();
    }

    // TODO Simplify this
    public static class SourceFile implements Comparable<SourceFile> {

        @SerializedName("file_name")
        public final String fileName;
        @SerializedName("type")
        public final String type;
        @SerializedName("file_type")
        public final FileType fileType;


        private final StreamGetter streamGetter;

        public SourceFile(final String path) {
            this(path, FileType.fromFileName(path), "NoType", null);
        }

        public SourceFile() {
            this(null, null, null);
        }

        public SourceFile(final String path,
                          final String type,
                          final StreamGetter streamGetter) {
            this(path, FileType.fromFileName(path), type, streamGetter);
        }

        public SourceFile(final String path,
                          final FileType fileType,
                          final String type,
                          final StreamGetter streamGetter) {

            this.fileName = path;
            this.fileType = fileType;
            this.type = type;
            this.streamGetter = streamGetter;
        }

        @Override
        public String toString() {
            return "SourceFile{" + "fileName=" + fileName + ", type=" + type + '}';
        }

        @Override
        public int compareTo(final SourceFile o) {
            if (o == null) {
                return -1;
            }
            return o.fileName.compareTo(fileName);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 53 * hash + Objects.hashCode(this.fileName);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final SourceFile other = (SourceFile) obj;
            if (!Objects.equals(this.fileName, other.fileName)) {
                return false;
            }
            return true;
        }

        public InputStream getInputStream() {
            return streamGetter.getStream();
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

    public SourceFiles addSourceFile(final String path, final String type, final StreamGetter streamGetter) {
        this.sourceFiles.add(new SourceFile(path, type, streamGetter));
        return this;
    }

    @Override
    public String toString() {
        return "SourceFiles{" + "sourceFiles=" + sourceFiles + ", rootDir=" + rootDir + '}';
    }

}
