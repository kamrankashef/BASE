package base.files;

import base.files.common.DBUtil;
import kamserverutils.common.util.IOUtil;

import static base.util.StreamUtil.getProjectFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

final public class FileI {

    private static final String LOCAL_COMMON_PATH = "base/files/common/";

    static public final String BUILD_FILE_PATH = "build.xml";
    static public final String BUILD_PROPERTIES_PATH = "build.properties";
    static public final String IVY_PATH = "ivy.xml";
    static public final String IVY_SETTINGS_PATH = "ivysettings.xml";

    static private final String EMPTY_TEST_TXT = "EmptyTest.txt";
    static private final String EMPTY_TEST = "EmptyTest";

    public static String getEmptyTestPackage(final String org) {
        return org + ".test";
    }

    public static String getEmptyTestFileExportPath(final String org) {
        return "test/" + getEmptyTestPackage(org).replaceAll("\\.", "/") + "/" + EMPTY_TEST + ".java";
    }


    private static String pkgReplacement(final String fileAsString, final String packageName) {
        final StringBuilder asBuilder = new StringBuilder();
        final String[] lines = fileAsString.split("\n");
        for (int i = 0; i < lines.length; i++) {

            if (i == 0) {
                asBuilder.append("package ").append(packageName).append(";");
            } else {
                asBuilder.append(lines[i]);
            }
            asBuilder.append("\n");
        }

        return asBuilder.toString();
    }


    public static String COMMON_PKG = "common";

    private static Map<String, String> allCommonFile() throws IOException {
        final Map<String, String> map = new HashMap<>();

        final Enumeration<URL> urls = Thread.currentThread().
                getContextClassLoader().
                getResources(LOCAL_COMMON_PATH);

        urls.hasMoreElements();
        final String path = urls.nextElement().toString();

        final Collection<String> entries = new LinkedList<>();
        if (path.contains("jar:")) {

            final String jarPath = DBUtil.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toString()
                    .replace("!/", "")
                    .replace("jar:file:", "");
            final JarInputStream jis = new JarInputStream(new FileInputStream(jarPath));
            while (true) {
                final ZipEntry e = jis.getNextEntry();
                if (e == null) {
                    break;
                }
                entries.add(e.getName());
            }
        } else {
            final File resourceDir = new File(path.replace("file:", ""));
            for (final File f : resourceDir.listFiles()) {
                entries.add(LOCAL_COMMON_PATH + f.getName());
            }
        }

        for (final String fileName : entries) {
            // TODO exclude directoires
            if (fileName.startsWith(LOCAL_COMMON_PATH)
                    && fileName.endsWith("txt")
                    && !fileName.replace(LOCAL_COMMON_PATH, "").contains("/")) {
                final String fileAsStr = pkgReplacement(getProjectFile(fileName), COMMON_PKG);
                map.put("src/" + COMMON_PKG + "/" + fileName.replaceAll("txt$", "java"), fileAsStr);
            }
        }
        return map;
    }


    /**
     * Collect all of the resources in rootResourceDir and collect them as Strings in pathToFilesAsString.
     * @param pathToFilesAsString
     * @param rootResourceDir
     * @param currentResourceDir
     * @throws IOException
     */
    private static void getResourceFolderFiles(final Map<String, String> pathToFilesAsString,
                                                              String rootResourceDir,
                                                              String currentResourceDir) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(currentResourceDir);
        String path = url.getPath();
        for(final File resourceFile : new File(path).listFiles()) {
            final String childPath = resourceFile.getPath();
            final String addOnPath = childPath.substring(path.length() + 1);
            final String resoucePath = currentResourceDir + "/" + addOnPath;
            if(resourceFile.isDirectory()) {
                getResourceFolderFiles(pathToFilesAsString, rootResourceDir, resoucePath);
            } else {
                final String genedResourcePath = resoucePath.substring(rootResourceDir.length() + 1);
                pathToFilesAsString.put(genedResourcePath, IOUtil.inputStreamToString(FileI.class.getClassLoader().getResourceAsStream(resoucePath)));
            }
        }
    }


    /**
     * Collect the boilerplate build files for the generated Maven project
     *
     * @param org
     * @return
     * @throws IOException
     */
    public static Map<String, String> allBuildFile(final String org) throws IOException {
        final Map<String, String> map = new HashMap<>();

        getResourceFolderFiles(map, "mvn_scaffolding", "mvn_scaffolding");

        map.putAll(allCommonFile());
        return map;
    }
}
