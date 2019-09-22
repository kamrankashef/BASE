package base.model.sql;

import base.model.PrimitiveType;

public interface DBVendorI {

    String toPropType(PrimitiveType primitiveType);

    String tableSuffix();

    String dropIfExists(String tableName);

    default String toDBRow(String fieldName, PrimitiveType primitiveType, boolean nullable) {
        return fieldName + " " + toPropType(primitiveType) + (nullable ? "" : " NOT NULL");
    }

    default String fkField(final String fkRefName,
                           final String fkTable,
                           final String fkRefName1,
                           final boolean nullable,
                           final boolean onDeleteCascade) {
        String returnStr
                = fkRefName + " " + PrimitiveType.CHAR_36.getSqlType() + (nullable ? "" : " NOT NULL") +"\n"
                + ",FOREIGN KEY (" + fkRefName + ")"
                + " REFERENCES " + fkTable + "(" + fkRefName +")";

        if (onDeleteCascade) {
            returnStr += " ON DELETE CASCADE";
        }
        return returnStr;
    }
}
