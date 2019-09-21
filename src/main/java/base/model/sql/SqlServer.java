package base.model.sql;

import base.model.PrimitiveType;

public class SqlServer implements DBVendorI {

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

        return "";
    }
}
