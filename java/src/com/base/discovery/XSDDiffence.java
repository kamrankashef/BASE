package com.base.discovery;

import com.kamserverutils.common.util.FileUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class XSDDiffence {

    final String path;

    final Map<String, XSDDiffence> childDifferences = new HashMap<>();

    final XSDElem fromElem, toElem;

    final Set<XSDElem> missingInFrom = new TreeSet<>();
    final Set<XSDElem> missingInTo = new TreeSet<>();

    final String toName;
    final String fromName;

    public XSDDiffence(
            final String path,
            final XSDElem fromElem,
            final String fromName,
            final XSDElem toElem,
            final String toName) {
        this.path = path;
        this.fromElem = fromElem;
        this.fromName = fromName;
        this.toElem = toElem;
        this.toName = toName;

        for (final XSDElem inFrom : fromElem.children()) {
            if (!toElem.hasChild(inFrom)) {
                missingInTo.add(inFrom);
                continue;
            }
            // Look at shared items for changes
            final XSDElem inTo = toElem.getChild(inFrom);

            final XSDDiffence childDifferences = new XSDDiffence(
                    this.path + "/" + inFrom.renderName(),
                    inFrom,
                    fromName,
                    inTo,
                    toName);

            this.childDifferences.put(this.path + "/" + inFrom.renderName(), childDifferences);

        }
        for (final XSDElem inTo : toElem.children()) {
            if (!fromElem.hasChild(inTo)) {
                missingInFrom.add(inTo);
            }
        }

    }

    public boolean hasDifferences() {
        return !missingInFrom.isEmpty() || !missingInTo.isEmpty();
    }

    private static XSDElem fromPath(final String fileName) throws IOException {
        final String xml = FileUtil.fileToString(fileName);
        final Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
        return XSDUtil.parse(doc);
    }

    public static void main(final String[] args) throws IOException {
        final String fileName1 = "/Users/alpha/mnt_points/personal_dev"
                + "/src/infra-and-minis/alphas/Base/java/test/com/base"
                + "/parsegen/xml/knicks/hanamigration/pcms_trade/pcms.xsd";

        final String fileName2 = "/Users/alpha/mnt_points/personal_dev"
                + "/src/infra-and-minis/alphas/Base/java/test/com/base"
                + "/parsegen/xml/nba/pcms/contract/pcms_0.0.7.xsd";

        final XSDDiffence diff = new XSDDiffence(
                "",
                fromPath(fileName1),
                "ver 0.0.6",
                fromPath(fileName2),
                "ver 0.0.7"
        );

        diff.echoDiff(0);
    }

    public void printSpaces(final int i) {
        IntStream.rangeClosed(0, i).forEach((value) -> {
            System.out.print("-");
        });
    }

    public void echoDiff(int indent) {
        if (!missingInFrom.isEmpty()) {
            System.out.println(fromName + path + " missing:");
            for (final XSDElem missingFrom : missingInFrom) {
                System.out.println(" > " + missingFrom.renderName());
            }
        }
        if (!missingInTo.isEmpty()) {

            System.out.println(toName + path +" missing");
            for (final XSDElem missingTo : missingInTo) {
                System.out.println(" < " + missingTo.renderName());
            }
        }

        for (final Map.Entry<String, XSDDiffence> diff : childDifferences.entrySet()) {
            if (diff.getValue().childDifferences.isEmpty()) {
                return;
            }
            diff.getValue().echoDiff(indent + 1);
        }
    }
}
