package base.model.sql;

import base.model.PrimitiveType;

public class MySql implements DBVendorI {

    @Override
    public String toPropType(final PrimitiveType primitiveType) {

        return primitiveType.getSqlType();
    }

    @Override
    public String tableSuffix() {

        return " ENGINE=InnoDB";
    }

    @Override
    public String dropIfExists(String tableName) {

        return "";
    }
}
