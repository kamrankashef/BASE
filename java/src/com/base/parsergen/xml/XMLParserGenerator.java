package com.base.parsergen.xml;

import com.base.files.FileI;
import com.base.gen.SourceBuilder;
import com.base.model.AbstractModel;
import com.base.model.PrimitiveField;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class XMLParserGenerator {

    public static String createNoOpParser(
            final ModelHierarchy nesting,
            final String parserName,
            final Collection<AbstractModel> models) {
        final SourceBuilder bldr = new SourceBuilder();
        bldr.append("package main;\n\n");
        bldr.append("import org.jsoup.Jsoup;\n");
        bldr.append("import org.jsoup.nodes.Element;\n");
        bldr.append("import org.jsoup.nodes.Document;\n");
        bldr.append("import org.jsoup.parser.Parser;\n\n");

        bldr.append("import ").append(FileI.COMMON_PKG).appendln(".FileUtil;")
                .append("import ").append(FileI.COMMON_PKG).appendln(".InitDatabase;")
                .appendln()
                .appendln("import java.io.IOException;")
                .appendln("import java.sql.Connection;")
                .appendln("import java.sql.SQLException;")
                .appendln("import java.text.ParseException;")
                .appendln();

        for (final AbstractModel m : models) {
            bldr.append("import ").append(m.getModelPackage()).append(".").append(m.getJavaClassName()).append(";\n");
        }

        bldr.append("\npublic class ").append(parserName).append(" {\n\n");
        bldr.append(1, "public static void main(final String ... args) throws Exception {\n\n");
        bldr.append(2, "final String fileName = args[0];\n");
        bldr.append(2, "final String xml = FileUtil.fileToString(fileName);\n");
        final String docVarName = "doc";
        bldr.append(2, "final Document " + docVarName + " = Jsoup.parse(xml, \"\", Parser.xmlParser());\n\n");
        bldr.append(2, "try (Connection conn = InitDatabase.getConnection(false)) {\n");
        final Map<String, Integer> suffixTracker = new HashMap<>();
        getVarSuffix(docVarName, suffixTracker);
        for (final ModelHierarchy subNesting : nesting.subNestings.values()) {
            bldr.append(createNoOpParser(subNesting, "doc", 3, suffixTracker));
        }
        bldr.append(3, "conn.commit();\n");
        bldr.append(2, "}\n");
        bldr.append(1, "}\n\n");
        bldr.append("}\n");
        return bldr.toString();
    }

    private static String createNoOpParser(final ModelHierarchy nesting, final String prevElem, int tabCount, final Map<String, Integer> suffixTracker) {

        final SourceBuilder bldr = new SourceBuilder();
        final AbstractModel nestedModel = nesting.model;
        final String javaVarName = nestedModel.toJavaVariableName();
        final String varSuffix = getVarSuffix(javaVarName, suffixTracker);

        final String currElem = javaVarName + "Elem" + varSuffix;
        final String localJavaVarName = javaVarName + varSuffix;

        bldr.append("\n").append(tabCount, "for(final Element ").append(currElem).append(":").append(prevElem).append(".select(\"> ").append(nesting.elementName).append("\")) {\n\n");

        bldr.append(tabCount + 1, "final ")
                .append(nestedModel.getJavaClassName()).append(" ")
                .append(localJavaVarName).append(" = ")
                .append(nestedModel.getJavaClassName())
                .append(".fromElem(")
                .append(currElem).append(");\n");

        for (final ModelHierarchy subNesting : nesting.subNestings.values()) {
            bldr.append(createNoOpParser(subNesting, currElem, tabCount + 1, suffixTracker));
        }
        bldr.append(tabCount, "}").append("\n");

        decrement(javaVarName, suffixTracker);

        return bldr.toString();
    }

    public static String prettyPrint(final ModelHierarchy nesting, final int tab) {

        final StringBuilder bldr = new StringBuilder();
        if (nesting.model != null) {
            for (int i = 0; i < tab; i++) {
                bldr.append("   ");
            }
            bldr.append(nesting.model.getJavaClassName()).append(": ");
            boolean isFirst = true;
            for (PrimitiveField field : nesting.model.getPrimitiveFields()) {
                if (!isFirst) {
                    bldr.append(", ");
                }
                bldr.append(field.getName());
                isFirst = false;
            }
            bldr.append("\n");
        }

        for (final ModelHierarchy xsdNesting : nesting.subNestings.values()) {
            bldr.append(prettyPrint(xsdNesting, tab + 1));
        }

        return bldr.toString();
    }

    private static String getVarSuffix(final String baseName, final Map<String, Integer> map) {
        if (!map.containsKey(baseName)) {
            map.put(baseName, 1);
            return "";
        }
        int next = map.get(baseName) + 1;
        map.put(baseName, next);
        return next + "";
    }

    private static void decrement(final String baseName, final Map<String, Integer> map) {
        if (!map.containsKey(baseName)) {
            return;
        }
        int nextVal = map.get(baseName) - 1;
        map.put(baseName, nextVal);
        if (nextVal == 0) {
            map.remove(baseName);
        }

    }

}
