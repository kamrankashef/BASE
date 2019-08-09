package base.discovery;

import kamserverutils.common.util.FileUtil;
import java.io.IOException;
import static java.util.stream.IntStream.range;
import static java.lang.System.out;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class XSDSummerize {

    public static void main(final String... args) throws IOException {

        final String fileName = "/Users/alpha/mnt_points/personal_dev"
                + "/src/infra-and-minis/alphas/Base/java/test/com/base"
                + "/parsegen/xml/nba/pcms/contract/pcms_0.0.7.xsd";
        //args[0];

        final String xml = FileUtil.fileToString(fileName);
        final Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
        final int indent = 0;
        summerize(indent, doc, doc.select("xs|element[name=xml-extract]").first());

    }

    private static void print(final int indent, final String msg) {

        range(0, indent).forEach((value) -> {
            out.print(" ");
        });
        out.println("<" + msg + ">");
    }

    public static void summerize(final int indent,
            final Element root,
            final Element extract) {
        if (extract.hasAttr("type")) {
            return;
        }
        if (extract.hasAttr("ref")) {
            final String refName = extract.attr("ref");
            print(indent, refName);
            final Elements refs
                    = root.select("xs|element[name="
                            + refName
                            + "]");
            if (refs.size() > 1) {
                System.err.println("Warning: " + refs.size()
                        + " matches on " + refName);
            }
            summerize(indent + 1, root, refs.first());
        }
//        System.out.println(extract.tag().getName());
        for (final Element child : extract.children()) {
            summerize(indent + 1, root, child);
        }
//            throw new RuntimeException(extract.tag().getName());
    }

}
