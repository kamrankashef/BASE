package base.parsergen.csv;

import base.files.FileI;
import base.gen.SourceBuilder;
import base.model.AbstractModel;

public abstract class DelimitedParserGenerator {
    
//    public static final String ESCAPED_CSV_SPLIT_EXP = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

    public static final String ESCAPED_CSV_SPLIT_EXP = ",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)"; // Maybe no comma to start?
    public static final String ESCAPED_TAB_SPLIT_EXP = "\\t";

    final private String splitExpression;

    protected DelimitedParserGenerator(final String splitExpression) {
        this.splitExpression = splitExpression;
    }

    public String getSplitExpression() {
        return this.splitExpression;
    }

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
                .append("import java.io.IOException;\n")
                .append("import java.sql.Connection;\n")
                .append("import java.sql.SQLException;\n")
                .append("import java.util.HashMap;\n")
                .append("import java.util.Map;\n")
                .append("import ").append(FileI.COMMON_PKG).append(".InitDatabase;\n")
                .append("\n");

        bldr
                .append("import ")
                .append(elemModel.getCanonicalName(parentPackage))
                .append(";\n");

        bldr.append("\npublic class ").append(parserName).append(" {\n\n");
        bldr.append(1, "public static void main(final String ... args) throws Exception {\n\n");
        bldr.append(2, "final String fileName = args[0];\n");
        bldr.append(2, "final String csv = FileUtil.fileToString(fileName);\n")
                .appendln("");
        bldr.append(2, "try (Connection conn = InitDatabase.getConnection(false)) {\n")
                .appendln("");
        // Get columns by heading

        bldr.append(3, "final String [] rows = csv.split(\"\\n\");\n");
        bldr.append(3, "final String [] colNames = rows[0].split(\"" + splitExpression + "\");\n");
        // Create a mapping for heading name to index
        bldr.append(3, "final Map<String, Integer> colMapping = new HashMap<>();\n\n");
        bldr.append(3, "for(int i = 0; i < colNames.length; i++) {\n");
        bldr.append(4, "final String colName = colNames[i].trim();\n")
                .appendln("");
        bldr.append(4, "if(colName.isEmpty()) {\n");
        bldr.append(5, "throw new RuntimeException(\"Empty column name at index: \" + i);\n");
        bldr.append(4, "}\n");
        bldr.append("");
        bldr.append(4, "if(colMapping.containsKey(colName)) {\n");
        bldr.append(5, "throw new RuntimeException(\"Duplicate column name for: \" + colName);\n");
        bldr.append(4, "}\n")
                .appendln("");
        bldr.append("");
        bldr.append(4, "colMapping.put(colName, i);\n");
        bldr.append(3, "}\n")
                .appendln("");

        bldr.append(3, "for(int i = 1; i < rows.length; i++) {\n");
        bldr.append(4, elemModel.toJavaDeclaration())
                .append(" = ")
                .append(elemModel.getJavaClassName())
                .append(".fromCSVRow(colMapping, rows[i]);\n"); // TODO define "Undelimit method"
        bldr.append(3, "}\n");
        bldr.append(3, "conn.commit();\n");
        bldr.append(2, "}\n");
        bldr.append(1, "}\n\n");
        bldr.append("}\n");
        return bldr.toString();

    }
}
