package com.base.normal.scratchspace;

import com.base.gen.SourceBuilder;
import com.base.model.AbstractModel;
import com.base.model.Model;
import com.base.model.PrimitiveField;
import com.base.model.PrimitiveType;
import com.base.normal.Node;
import com.base.normal.NodeType;
import com.base.parsergen.rules.TypeRenamerI;
import com.base.util.CaseConversion;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NodeToModelsAndPaser {

    final Node rootNode;
    final Map<String, AbstractModel> models = new HashMap<>();
    final String parser;
    final String _package;
    final TypeRenamerI renamer;

    public NodeToModelsAndPaser(
            final Node node,
            final String _package,
            final TypeRenamerI renamer,
            final String parserName) {
        this.rootNode = node;
        this._package = _package;
        this.renamer = renamer;
        parser = gen(parserName);
    }

    public String gen(final String parserName) {
        final SourceBuilder bldr = new SourceBuilder();
        bldr.append("package ")
                .append(_package)
                .appendln(".mains;")
                .appendln();

        bldr.appendln("import common.InitDatabase;");
        bldr.appendln("import java.io.FileNotFoundException;");
        bldr.appendln("import java.io.IOException;");
        bldr.appendln("import java.sql.SQLException;");
        bldr.appendln("import java.sql.Connection;");
        bldr.appendln("import java.util.List;");
        bldr.appendln("import java.util.Map;")
                .appendln();

        bldr.append("public class ").append(parserName).appendln(" {").appendln();

        bldr.appendln(1, "private final Map<String, Object> map;")
                .appendln();
        getCurrentName(rootNode.getName());
        bldr.append(1, "public ").append(parserName).appendln("(final Map<String, Object> map) {");
        bldr.appendln(2, "this.map = map;");
        bldr.appendlnln(1, "}");

        bldr.appendln(1, "public static void main(final String ...args) throws FileNotFoundException, SQLException, IOException {");

        bldr.appendln(2, "final String fileName = args[0];");
        bldr.appendln(2, "final String fileAsStr = common.FileUtil.fileToString(fileName);");
        bldr.appendln(2, "final Map<String, Object> map = new com.google.gson.Gson().fromJson(fileAsStr, Map.class);");
        bldr.appendlnln(2, "final " + parserName + " parser = new " + parserName + "(map);");

        bldr.appendlnln(2, "try (Connection conn = InitDatabase.getConnection(false)) {");
        bldr.appendln(3, "parser.parse(conn);");
        bldr.appendlnln(3, "conn.commit();");
        bldr.appendlnln(2, "}");

        bldr.appendlnln(1, "}");

        bldr.appendln(1, "public void parse(final Connection conn) throws FileNotFoundException, SQLException, IOException {")
                .appendln();
        for (final String childName : rootNode.childrenNames()) {
            this.gen(rootNode.getSubNode(childName), bldr, 2, "map");
        }

        bldr.appendln(1, "}");
        bldr.appendln("}");
        return bldr.toString();
    }

    public void gen(
            Node node,
            final SourceBuilder bldr,
            final int indent,
            final String referenceObjectName) {

        node = node.hackedNode();
        final AbstractModel model = new Model(getCurrentName(node.getName()), _package);
        if (models.containsKey(model.getJavaClassName())) {
            System.out.println("Adding: " + model.getJavaClassName());
            throw new RuntimeException("Key already exists: '" + model.getJavaClassName() + "'");
        }
        models.put(model.getJavaClassName(), model);

        for (final String childName : node.childrenNames()) {

            final Node childNode = node.getSubNode(childName).hackedNode();
            final NodeType nodeType = childNode.getNodeType();
            if (nodeType == null) {
                System.err.println("Null node type on " + childName + ". Treating as TINY_TEXT.");
                node.setUnderlyingType(PrimitiveType.TINY_TEXT);
                continue;
            }
            switch (nodeType) {
                case LITERAL:
                    // 1 - Collect primitives into model
                    // 2 - Parsing to be handled in the Elem object
                    // Duplication from LiteralList below
                    final PrimitiveField field = new PrimitiveField(
                            renamer.rename(childName),
                            childName,
                            childNode.underlyingType());
                    field.setNullable(childNode.nullable());
                    model.addPrimitiveField(field);
                    break;
                case OBJECT_SLASH_MAP:
                    // Get the map and send it to Elem.fromMap();
                    final String mapRefName = handleMapExtract(model, referenceObjectName, indent, bldr, childName);
                    gen(childNode, bldr, indent, mapRefName);
                    break;
                case LITERAL_LIST:
                    final String literalListRefName = childName + "List" + (indent - 2);
                    final String javaType = childNode.getSingleSubNode().underlyingType().javaTypeName;
                    bldr.append(indent, "final List<" + javaType + "> " + literalListRefName)
                            .append(" = (List<" + javaType + ">) ")
                            .append(referenceObjectName).append(".get(\"")
                            .append(childName)
                            .appendln("\");");

                    bldr.appendln(indent, "for (final " + javaType + " item : " + literalListRefName + ") {");

                    bldr.appendln(indent, "}");
                    final AbstractModel literalModel = new Model(getCurrentName(childName), _package);
                    final Node literal = childNode.getSingleSubNode();

                    final PrimitiveField listField = new PrimitiveField(
                            renamer.rename(childName),
                            childName,
                            literal.underlyingType());
                    literalModel.addPrimitiveField(listField);
                    listField.setNullable(childNode.nullable());
                    if (models.containsKey(literalModel.getJavaClassName())) {
                        throw new RuntimeException("Key already exists: '" + literalModel.getJavaClassName() + "'");
                    }
                    models.put(literalModel.getJavaClassName(), literalModel);
                    popModelName();
                    break;
                case COMPLEX_LIST:
                    final String listRefName = childName + "List" + indent;
                    bldr.append(indent, "final List<Map<String, Object>> " + listRefName)
                            .append(" = (List<Map<String, Object>>) ")
                            .append(referenceObjectName).append(".get(\"")
                            .append(childName)
                            .appendln("\");");
                    final String loopVar = "map" + indent;
                    bldr.append(indent, "for(final Map<String, Object> ")
                            .append(loopVar)
                            .append(" : ")
                            .append(listRefName)
                            .appendln(") {");

                    final String childMapRefName
                            = handleMapExtractNoExtract(model, 1 + indent, bldr, loopVar);

                    // Loop on children here, not the childNode
                    for (final String listChildName : node.childrenNames()) {
                        final Node listChildNode = node.getSubNode(listChildName);
                        gen(listChildNode, bldr, indent + 1, childMapRefName);
                    }

                    bldr.append(indent, "}");
                    break;
                case UNKNOWN_LIST:
                    System.err.println("Undefinied list " + childName);
                    break;
            }

        }
        popModelName();

    }

    final List<String> modelNames = new LinkedList<>();

    public String getCurrentName(final String name) {

        modelNames.add(CaseConversion.toJavaClassName(name));
        String possibleName = "";

        for (int i = modelNames.size() - 1; i >= 0; i--) {
            possibleName += modelNames.get(i);
//            System.out.println(possibleName);
            if (!models.containsKey(possibleName)) {

                return possibleName;
            }
        }

        return null;
    }

    public void popModelName() {
        modelNames.remove(modelNames.size() - 1);
    }

    public String getParser() {
        return parser;
    }

    public Map<String, AbstractModel> getModels() {
        return models;
    }

    private String handleMapExtractNoExtract(
            final AbstractModel model,
            final int indent, final SourceBuilder bldr,
            final String mapRefName) {

        bldr.append(indent,
                model.toJavaDeclaration())
                .append(" = ")
                .append(model.getCanonicalName(_package))
                .append(".fromObjectMap(")
                .append(mapRefName)
                .appendln(");");
        return mapRefName;
    }

    private String handleMapExtract(
            final AbstractModel model,
            final String referenceObjectName,
            final int indent, final SourceBuilder bldr, final String childName) {
        final String mapRefName = childName + "Map" + (indent - 2);
        bldr.append(indent, "final Map<String, Object> " + mapRefName)
                .append(" = (Map<String, Object>) ")
                .append(referenceObjectName).append(".get(\"")
                .append(childName)
                .appendln("\");");
        bldr.append(indent,
                model.toJavaDeclaration(true))
                .append(" = ")
                .append(model.getCanonicalName(""))
                .append(".fromObjectMap(")
                .append(mapRefName)
                .appendln(");");
        return mapRefName;
    }

}
