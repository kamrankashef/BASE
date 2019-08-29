package base.gen;

import base.model.AbstractField;
import base.model.AbstractModel;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SchemaGen {

    final private DBVendor dbVendor;

    public SchemaGen(final DBVendor dbVendor) {
        this.dbVendor = dbVendor;
    }

    public enum DBVendor {
        MYSQL,
        POSTGRES,
        SQLSERVER
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

        if (dbVendor.equals(DBVendor.POSTGRES)) {
            bldr.append("DROP TABLE IF EXISTS ").append(model.toDBName()).appendln(";")
                    .appendln();
        }

        bldr.append("CREATE TABLE ")
                .append(model.toDBName()).appendln(" (");
        bldr.append(" ")
                .append(model.getGuidField().toDBRow())
                .appendln(" PRIMARY KEY");
//                .appendln();

        for (final AbstractField field : model.getFkFields()) {
            bldr.append(",").append(field.toDBRow()).appendln();
        }
        for (final AbstractField field : model.getPrimitiveFieldsWithLinked()) {
            bldr.append(",").append(field.toDBRow()).appendln();
        }
        for (final AbstractField field : model.getAugmentedFields().keySet()) {
            bldr.append(",").append(field.toDBRow()).appendln();
        }

        bldr.appendln(
                ",created_at TIMESTAMP NULL DEFAULT NULL")
                .appendln(",modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
                .appendln(",deleted_at TIMESTAMP NULL DEFAULT NULL")
                .append(")").append(dbVendor.equals(DBVendor.MYSQL) ? " ENGINE=InnoDB" : "").appendln(";");

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
