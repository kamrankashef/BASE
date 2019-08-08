package com.base.parsergen.rules.training;

import com.google.gson.annotations.SerializedName;

import java.io.File;
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
