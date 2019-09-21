package base.model.sql;

import base.model.PrimitiveType;

public class Postgres implements DBVendorI {

    @Override
    public String toPropType(final PrimitiveType primitiveType) {

        return primitiveType.getSqlType();
    }

    @Override
    public String tableSuffix() {

        return "";
    }

    @Override
    public String dropIfExists(String tableName) {

        return "DROP TABLE IF EXISTS " + tableName + ";\n\n";
    }
}
