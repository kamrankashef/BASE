package base.files;

import kamserverutils.common.util.IOUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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

    // Common files are now handled via copy out of resources/mvn_scaffolding
    // The model inference layer needs an overhaul before getting rid of this.
    private static Map<String, String> allCommonFile() throws IOException {
        return new HashMap<>();
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
