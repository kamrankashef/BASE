package base.parsergen.csv;

import base.files.FileI;
import base.gen.SourceBuilder;
import base.model.AbstractModel;

public class CSVParserGenerator {

    public String createParser(
            final String parserName,
            final AbstractModel elemModel) {
        return createParser(parserName, elemModel, "");
    }

    public String createParser(
            final String parserName,
            final AbstractModel elemModel,
            final String parentPackage) {
        final SourceBuilder bldr = new SourceBuilder();
        bldr.append("package main;\n\n");

        bldr
                .append("import ").append(FileI.COMMON_PKG).appendln(".FileUtil;\n")
                .appendln("import java.io.IOException;")
                .appendln("import java.io.FileReader;")
                .appendln("import java.io.Reader;")
                .appendln("import java.sql.Connection;")
                .appendln("import java.sql.SQLException;")
                .appendln("import java.util.HashMap;")
                .appendln("import java.util.Map;")
                .appendln("import java.util.Set;")
                .appendln("import org.apache.commons.csv.CSVFormat;")
                .appendln("import org.apache.commons.csv.CSVParser;")
                .appendln("import org.apache.commons.csv.CSVRecord;")
                .append("import ").append(FileI.COMMON_PKG).appendln(".InitDatabase;");

        bldr
                .append("import ")
                .append(elemModel.getCanonicalName(parentPackage))
                .appendlnln(";");

        bldr.append("public class ").append(parserName).appendlnln(" {");
        bldr.appendlnln(1, "public static void main(final String ... args) throws Exception {")
                // Set up parser
                .appendln(2, "final String fileName = args[0];")
                .appendln(2, "final Reader in = new FileReader(fileName);")
                .appendln(2, "final CSVParser records = CSVFormat.EXCEL.withHeader().parse(in);")
                .appendlnln(2, "final Set<String> headers = records.getHeaderMap().keySet();");

        bldr.appendlnln(2, "try (Connection conn = InitDatabase.getConnection(false)) {");

        bldr.appendln(3, "for (CSVRecord record : records) {");
        bldr.append(4, elemModel.toJavaDeclaration())
                .append(" = ")
                .append(elemModel.getJavaClassName())
                .appendln(".fromRow(record);");
                // TODO Keep a link between the Elem models and the derived Models so the master parsers can be auto-gened
                // Including the persist call can't be done cleanly at the moment
                //.append(4, elemModel.dlName())
                //.appendln(".insert(").append(elemModel.toJavaVariableName()).append(");");
        bldr.appendln(3, "}")
                .appendln(3, "conn.commit();")
                .appendln(2, "}")
                .appendlnln(1, "}");
        bldr.appendln("}");
        return bldr.toString();

    }
}
