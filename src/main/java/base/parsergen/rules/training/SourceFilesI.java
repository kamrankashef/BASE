package base.parsergen.rules.training;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;

public interface SourceFilesI {

    public static interface SourceFileI {
        public InputStream getSourceFile() throws FileNotFoundException;
        public String getType();
    }

    public Collection<SourceFileI> getFiles();
}
