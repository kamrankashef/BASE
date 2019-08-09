package base.files.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.zip.GZIPInputStream;

final public class FileUtil {

	private FileUtil() {
	}

	public static boolean attemptDeleteFile(final File file) {
		if (file == null) {
			return true;
		}
		try {
			file.delete();
			return true;
		} catch (final Exception e) {
			return false;
		}
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
