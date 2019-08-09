package kamserverutils.common.util;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.Service;

final public class IOUtil {

    private IOUtil() {
    }
    
    public static void inputStreamToFile(final InputStream input,
            final String fileName) throws IOException, FileNotFoundException {
        try(final FileOutputStream fos = new FileOutputStream(fileName)) {
            copyStream(input, fos);
        }
    }
    
    public static void fileToOutputStream(final String path,
            final OutputStream ops) throws IOException, FileNotFoundException {
        try(final FileInputStream fos = new FileInputStream(path)) {
            copyStream(fos, ops);
        }
    }
    public static void copyStream(final InputStream input,
            final OutputStream output)
            throws IOException {
        byte[] buffer = new byte[1024]; // Adjust if you want
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        output.flush();
    }
    private static final Logger LOGGER = Logger.getLogger(IOUtil.class.getName());

    public static void attemptClose(final Socket socket) {
        if (socket == null) {
            return;
        }
        try {
            socket.close();
        } catch (final IOException e) {
            LOGGER.log(Level.FINEST, "Exception closing socket", e);
        }
    }

    public static void attemptClose(final BufferedReader reader) {
        if (reader == null) {
            return;
        }
        try {
            reader.close();
        } catch (final IOException e) {
            LOGGER.log(Level.FINEST, "Exception closing reader", e);
        }
    }

    public static void attemptClose(final InputStream is) {
        if (is == null) {
            return;
        }
        try {
            is.close();
        } catch (final IOException e) {
            LOGGER.log(Level.FINEST, "Exception closing InputStream", e);
        }
    }

    public static void attemptClose(final OutputStream os) {
        if (os == null) {
            return;
        }
        try {
            os.close();
        } catch (final IOException e) {
            LOGGER.log(Level.FINEST, "Exception closing OutputStream", e);
        }
    }

    // TODO Move to EmailUtil
    public static void attemptClose(final Service t) {
        if (t == null) {
            return;
        }
        try {
            t.close();
        } catch (final MessagingException e) {
            LOGGER.log(Level.FINEST, "Exception closing Service", e);
        }
    }

    public static String inputStreamToString(final InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.attemptClose(br);
        }

        return sb.toString();
    }

//	public static void drainIOStream(final InputStream is, final OutputStream os)  {
//		
//	}
}
