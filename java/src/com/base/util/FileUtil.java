package com.base.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class FileUtil {

    public static boolean deleteDir(final File dir) {
        if (dir.isDirectory()) {
            for (final File f : dir.listFiles()) {
                deleteDir(f);
            }
        }
        return dir.delete();
    }

    public static void deleteDir(final String dir) {
        deleteDir(new File(dir));
    }

    public static Collection<String> getResourceFromPackage(
            final Class clazz, final String fileType) throws IOException {
        return getResourceFromPackage(clazz, null, fileType);
    }

    public static Collection<String> getResourceFromPackage(
            final Class clazz,
            final String subPackage,
            final String fileType) throws IOException {
        final String pkgAsDir = clazz
                .getPackage()
                .getName()
                .replace(".", "/")
                + (subPackage != null ? ("/" + subPackage) : "");

        final InputStream is = clazz
                .getClassLoader()
                .getResourceAsStream(pkgAsDir);

        byte[] buffer = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (is.available() > 0) {
            final int read = is.read(buffer, 0, 1024);
            sb.append(new String(buffer, 0, read, Charset.defaultCharset()));
        }

        return Arrays
                .asList(sb.toString().split("\n")) // Convert StringBuilder to individual lines
                .stream() // Stream the list
                .filter(line -> line.trim().length() > 0) // Filter out empty lines
                .filter(line -> line.endsWith(fileType))
                .map(line -> pkgAsDir + "/" + line)
                .collect(Collectors.toList());
    }

    final public static String fileToString(final String fileName)
            throws
            FileNotFoundException, IOException {
        final Reader fr;
        if (fileName.endsWith("gz")) {
            final InputStream fileStream = new FileInputStream(fileName);
            final InputStream gzipStream = new GZIPInputStream(fileStream);
            fr = new InputStreamReader(gzipStream);
        } else {
            fr = new FileReader(fileName);
        }

        try (final BufferedReader br = new BufferedReader(fr)) {
            final StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line
                    != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            return sb.toString();
        }

    }

    public static void stringToFile(final String fileName,
            final String fileContents) throws IOException {

        try (BufferedWriter bw
                = new BufferedWriter(new FileWriter(fileName))) {
            bw.write(fileContents);
        }
    }

}
