package base.model;

import com.google.gson.annotations.SerializedName;
import kamserverutils.common.util.StringUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum PrimitiveType {

    // TODO Should sqlType be eliminated an completely owned by DBVendorI?
    // Todo use a convention instread of all these method names
    LONG_TEXT(null, "setNullableString", "getNullableString", "getString", "VARCHAR(1000000)", "String", new String[0]),
    MEDIUM_TEXT(LONG_TEXT, "setNullableString", "getNullableString", "getString", "VARCHAR(40000)", "String", new String[0]),
    TEXT_2048(MEDIUM_TEXT, "setNullableString", "getNullableString", "getString", "VARCHAR(2048)", "String", new String[0]),
    TEXT(TEXT_2048, "setNullableString", "getNullableString", "getString", "VARCHAR(1024)", "String", new String[0]),
    SMALL_TEXT(MEDIUM_TEXT, "setNullableString", "getNullableString", "getString", "VARCHAR(512)", "String", new String[0]),
    TINY_TEXT(SMALL_TEXT, "setNullableString", "getNullableString", "getString", "VARCHAR(128)", "String", new String[0]),
    CHAR_36(TINY_TEXT, "setNullableString", "getNullableString", "getString", "CHAR(36)", "String", new String[0]),
    CHAR_10(CHAR_36, "setNullableString", "getNullableString", "getString", "CHAR(10)", "String", new String[0]),
    //
    // Boolean types
    BOOLEAN(TINY_TEXT, "setNullableBoolean", "getNullableBoolean", "getBoolean", "BOOLEAN", "Boolean", new String[0]),
    //
    // NVARCHAR types
    // Removing use of NVARCHARs as not ANSI SQL
//    NLONG_TEXT(null, "setNullableString", "getNullableString", "getString", "NVARCHAR(1000000)", "String", new String[0]),
//    NMEDIUM_TEXT(NLONG_TEXT, "setNullableString", "getNullableString", "getString", "NVARCHAR(40000)", "String", new String[0]),
//    NTEXT_2048(NMEDIUM_TEXT, "setNullableString", "getNullableString", "getString", "NVARCHAR(2048)", "String", new String[0]),
//    NTEXT(NTEXT_2048, "setNullableString", "getNullableString", "getString", "NVARCHAR(1024)", "String", new String[0]),
//    NTINY_TEXT(NTEXT, "setNullableString", "getNullableString", "getString", "NVARCHAR(128)", "String", new String[0]),
    //
    // Numeric types
    DOUBLE(TINY_TEXT, "setNullableDouble", "getNullableDouble", "getDouble", "DOUBLE", "Double", new String[0]), // DOUBLE PRECISION
    LONG(DOUBLE, "setNullableLong", "getNullableLong", "getLong", "LONG", "Long", new String[0]),
    VARIABLE_LONG(LONG, "setNullableLong", "getNullableLong", "getVariableLong", "LONG", "Long", new String[0]),
    INT(LONG, "setNullableInt", "getNullableInt", "getInteger", "INT", "Integer", new String[0]),
    SMALLINT(INT, "setNullableInt", "getNullableInt", "getInteger", "SMALLINT", "Integer", new String[0]),
    TINYINT(SMALLINT, "setNullableInt", "getNullableInt", "getInteger", "TINYINT", "Integer", new String[0]),
    //
    // Date Types
    TIMESTAMP_FRACTION_3(TINY_TEXT, "setNullableTimestamp", "getNullableDate", "getDate", "TIMESTAMP(3) NULL DEFAULT NULL", "Date", new String[]{"java.util.Date"}),
    TIMESTAMP(TIMESTAMP_FRACTION_3, "setNullableTimestamp", "getNullableDate", "getDate", "TIMESTAMP NULL DEFAULT NULL", "Date", new String[]{"java.util.Date"}),
    DATE(TIMESTAMP, "setNullableTimestamp", "getNullableDate", "getDate", "DATE NULL DEFAULT NULL", "Date", new String[]{"java.util.Date"}),
    //
    // Array types
    TINY_TEXT_ARRAY(null, "setNullableString", "getNullableString", "getJson", "VARCHAR(1024)", "String", new String[0]),
    DOUBLE_ARRAY(TINY_TEXT_ARRAY, "setNullableString", "getNullableString", "getJson", "VARCHAR(1024)", "String", new String[0]);

    @SerializedName("parent_type")
    final public PrimitiveType parentType;
    @SerializedName("java_type_name")
    final public String javaTypeName;
    @SerializedName("required_imports")
    final private String[] requiredImports;
    @SerializedName("sql_type")
    final String sqlType;
    @SerializedName("set_nullable_method")
    public final String setNullableMethod;
    @SerializedName("get_nullable_method")
    public final String getNullableMethod;
    @SerializedName("extract_method")
    public final String extractMethod;

    PrimitiveType(
            final PrimitiveType parentType,
            final String setNullableMethod,
            final String getNullableMethod,
            final String extractMethod,
            final String sqlType,
            final String javaTypeName,
            final String[] requiredImports) {
        this.parentType = parentType;
        this.sqlType = sqlType;
        this.setNullableMethod = setNullableMethod;
        this.getNullableMethod = getNullableMethod;
        this.extractMethod = extractMethod;
        this.javaTypeName = javaTypeName;
        this.requiredImports = requiredImports;
    }

    public String getSqlType() {
        return sqlType;
    }

    final private static Pattern TYPE_PATTERN = Pattern.compile("([a-zA-Z]+)\\(([0-9]+)\\)");

    private static PrimitiveType varcharParamsToType(final String str, final int len) {
        if ("VARCHAR".equalsIgnoreCase(str)) {
            if (len <= 128) {
                return TINY_TEXT;
            }
            if (len <= 1024) {
                return TEXT;
            }
            if (len <= 40000) {
                return MEDIUM_TEXT;
            }
            if (len <= 1000000) {
                return LONG_TEXT;
            } else {
                throw new RuntimeException();
            }
        }
        if ("NVARCHAR".equalsIgnoreCase(str)) {
            throw new RuntimeException("No longer supported data type NVARCHAR");
//            if (len <= 128) {
//                return NTINY_TEXT;
//            }
//            if (len <= 1024) {
//                return NTEXT;
//            }
//            if (len <= 40000) {
//                return NMEDIUM_TEXT;
//            }
//            if (len <= 1000000) {
//                return NLONG_TEXT;
//            } else {
//                throw new RuntimeException();
//            }
        }
        return null;
    }

    public static PrimitiveType fromString(final String typeStr) {
        if (StringUtil.isNullWhiteSpace(typeStr)) {
            return null;
        }
        final Matcher m = TYPE_PATTERN.matcher(typeStr);

        if (m.matches()) {
            final String t = m.group(1);
            final int len = Integer.parseInt(m.group(2));
            final PrimitiveType type = varcharParamsToType(t, len);
            if (type != null) {
                return type;
            }
        }

        for (final PrimitiveType type : PrimitiveType.values()) {
            if (type.name().equalsIgnoreCase(typeStr)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown type: " + typeStr);
    }

    public String toJavaTypeName() {
        return this.javaTypeName;
    }

    public String[] requiredImports() {
        return this.requiredImports;
    }

    public String setNullableMethod() {
        return this.setNullableMethod;
    }

    public String getNullableMethod() {
        return this.getNullableMethod;
    }
}
