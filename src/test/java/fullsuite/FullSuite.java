package fullsuite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.fail;

import base.parsegen.csv.playerscouting.TestPlayerScoutingCSV;
import fromjson.TestFromJson;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        //base.model.TestAdjoinedModel.class,
        //base.model.TestModel.class,
        //base.model.TestPrimitiveFields.class,
        //base.model.TestSpecialConstructor.class,
        //base.util.CaseConversionTest.class,
        //base.util.StringUtilTest.class,
        TestFromJson.class,
        TestPlayerScoutingCSV.class,
})
public class FullSuite {

    public static String classToPath(final Class c) {
        return c.getPackage().getName().replace(".", "/");
    }

    public static String classToPath(final Class c, final String fileName) {
        return classToPath(c) + "/" + fileName;
    }

    public static String classToPath(final String baseDir, final Class c, final String fileName) {
        return baseDir + "/" + classToPath(c, fileName);
    }

    public static int runCommand(final String command) throws IOException, InterruptedException {
        final Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", command});
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(
                p.getInputStream()));
        String line;
        while ((line = stdInput.readLine()) != null) {
            System.out.println(line);
        }
        p.waitFor(10, TimeUnit.SECONDS);
        return p.exitValue();
    }

    public static void runDiff(final String dir1, final String dir2) throws InterruptedException, IOException {
        final String[] cmdArr = {"diff", "-r", dir1, dir2};

        final Process p = Runtime.getRuntime().exec(cmdArr);

        String line = null;
        String sout = "";
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(
                p.getInputStream()));
        final List<String> replaceCommands = new LinkedList<>();
        while ((line = stdInput.readLine()) != null) {
            sout += ">> " + line + "\n";
            if (line.startsWith("diff -r")) {
                final String[] parts = line.split(" ");
                replaceCommands.add("mv " + parts[3] + " " + parts[2]);
            } else if (line.startsWith("Only in " + dir2)) {
                final String[] parts = line.split(" ");
                final String path = parts[2].replaceAll(":$", "");
                // Only work
                replaceCommands.add("mv " + path + "/" + parts[3]
                        + " " + path.replace(dir2, dir1));
            }
        }
        p.waitFor(5, TimeUnit.SECONDS);

        int exitVal = p.exitValue();

        if (exitVal != 0) {
            System.out.println("Process exitValue: " + exitVal);

            if (exitVal == 2) {
                InputStream stderr = p.getErrorStream();
                InputStreamReader isr = new InputStreamReader(stderr);
                BufferedReader br = new BufferedReader(isr);
                line = null;
                System.out.println("<ERROR>");
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                System.out.println("</ERROR>");
            } else if (exitVal == 1) {
                System.out.println(sout);
                for (final String cmd : replaceCommands) {
                    System.out.println(cmd);
                }
            }
            fail();
        }
    }
}
