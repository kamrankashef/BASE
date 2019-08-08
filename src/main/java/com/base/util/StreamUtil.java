package com.base.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public interface StreamUtil {

    public static String getProjectFile(final String path) throws IOException {
        final InputStream is = Thread.currentThread().
                getContextClassLoader().
                getResourceAsStream(path);
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }

}
