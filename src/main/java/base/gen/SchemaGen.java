package base.gen;

import base.model.AbstractField;
import base.model.AbstractModel;
import base.model.PrimitiveType;
import base.model.sql.DBVendorI;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SchemaGen {

    final private DBVendorI dbVendor;

    public SchemaGen(final DBVendorI dbVendor) {
        this.dbVendor = dbVendor;
    }

    final public String buildSchema(final String databaseName,
                                    final Collection<AbstractModel> models) {
        String schema = "";
        schema += "-- DROP DATABASE IF EXISTS " + databaseName + ";\n"
                + "-- CREATE DATABASE " + databaseName + ";\n"
                + "\n"
                + "-- use " + databaseName + ";\n"
                + "\n";

        schema += "-- GRANT ALL PRIVILEGES ON "
                + databaseName + ".* TO '"
                + databaseName + "'@'localhost'"
                + " IDENTIFIED BY 'password';\n\n";

        for (final AbstractModel m : models) {
            schema += "-- DROP TABLE " + m.toDBName() + ";\n";
        }

        schema += "\n";

        for (final AbstractModel m : models) {
            schema += toSQLDef(m) + "\n\n";
        }
        return schema;
    }

    final private String toSQLDef(final AbstractModel model) {
        final SourceBuilder bldr = new SourceBuilder();

        bldr.append(dbVendor.dropIfExists(model.toDBName()));


        bldr.append("CREATE TABLE ")
                .append(model.toDBName()).appendln(" (");
        bldr.append(" ")
                .append(model.getGuidField().toDBRow(dbVendor))
                .appendln(" PRIMARY KEY");

        for (final AbstractField field : model.getFkFields()) {
            bldr.append(",").append(field.toDBRow(dbVendor)).appendln();
        }
        for (final AbstractField field : model.getPrimitiveFieldsWithLinked()) {
            bldr.append(",").append(field.toDBRow(dbVendor)).appendln();
        }
        for (final AbstractField field : model.getAugmentedFields().keySet()) {
            bldr.append(",").append(field.toDBRow(dbVendor)).appendln();
        }

        bldr.append(",")
                .appendln(dbVendor.toDBRow("created_at", PrimitiveType.TIMESTAMP, true))
                .append(",")
                .appendln(dbVendor.toDBRow("modified_at", PrimitiveType.TIMESTAMP, true))
                .append(",")
                .appendln(dbVendor.toDBRow("deleted_at", PrimitiveType.TIMESTAMP, true))
                .append(")")
                .append(dbVendor.tableSuffix()).appendln(";");

        bldr.appendln("-- Indexes");
        for (final List<AbstractField> key : model.getKeys()) {
            final Supplier<Stream<String>> fieldNames
                    = () -> key.stream()
                    .map(AbstractField::toDBName);

            final String indexName = fieldNames.get().collect(Collectors.joining("_", "", "_idx"));
            final String fieldList = fieldNames.get().collect(Collectors.joining(", "));
            bldr.appendln("CREATE NONCLUSTERED INDEX ")
                    .appendln(indexName)
                    .appendln("ON")
                    .appendln(model.toDBName())
                    .append("(")
                    .append(fieldList)
                    .appendln(");");

        }

        return bldr.toString();
    }

}
