package base.model.sql;

import base.model.PrimitiveType;

import static base.model.PrimitiveType.*;

public class SqlServer implements DBVendorI {

    @Override
    public String toPropType(final PrimitiveType primitiveType) {

        switch (primitiveType) {
            case TIMESTAMP_FRACTION_3:
                // TODO Fix - This is mixing type and field declaration logic.
                return "DATETIMEOFFSET(3)";
            case TIMESTAMP:
                // TODO Fix - This is mixing type and field declaration logic.
                return "DATETIMEOFFSET";
            case LONG:
                return "BIGINT";
            case SMALL_TEXT:
            case TINY_TEXT:
            case MEDIUM_TEXT:
            case LONG_TEXT:
            case TEXT:
                if(primitiveType.size > 10000) {
                    // Legacy for unit tests
                    return "NVARCHAR(MAX)";
                }
                return "NVARCHAR(" + primitiveType.size + ")";
        }

        return primitiveType.getSqlType();
    }

    @Override
    public String toDBRow(String fieldName, PrimitiveType primitiveType, boolean nullable) {
        final String sqlType = toPropType(primitiveType);

        switch (primitiveType) {
            case TIMESTAMP_FRACTION_3:
            case TIMESTAMP:
                if(nullable) {
                    return fieldName + " " + sqlType + " NULL DEFAULT NULL";
                }
                return fieldName + " " + sqlType;

        }
        return fieldName + " " + toPropType(primitiveType) + (nullable ? "" : " NOT NULL");
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
