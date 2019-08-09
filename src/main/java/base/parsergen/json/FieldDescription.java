package base.parsergen.json;

import base.model.PrimitiveType;
import java.sql.Array;
import java.util.Map;
import java.util.regex.Pattern;
import org.jsoup.helper.StringUtil;

public class FieldDescription {

    public final String name;
    public PrimitiveType type = null;
    public boolean nullable = false;

    public FieldDescription(final String name) {
        this.name = name;
        if (name.endsWith("Id")) {
            this.type = PrimitiveType.LONG;
        }
    }

    private static final String DOUBLE_EXP = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";

    private final Pattern DOUBLE_PATTERN = Pattern.compile(DOUBLE_EXP);

    public void applyValue(final Object value) {
        if (value == null) {
            this.nullable = true;
            return;
        }
        final PrimitiveType tempType;
        
        final Class clazz = value.getClass();
        if (clazz.equals(Boolean.class)) {
            tempType = PrimitiveType.BOOLEAN;
        } else if (clazz.equals(String.class)) {
            if (StringUtil.isNumeric(value.toString())) {
                tempType = PrimitiveType.INT;
            } else if (DOUBLE_PATTERN.matcher(value.toString()).matches()) {
                tempType = PrimitiveType.DOUBLE;
            } else {
                tempType = PrimitiveType.TINY_TEXT;
            }
        } else if (value.getClass().equals(Double.class)) {
            final Double asDouble = (Double) value;
            if (asDouble.longValue() != asDouble) {
                tempType = PrimitiveType.DOUBLE;
            } else {
                tempType = PrimitiveType.LONG;
            }
        } else if (value instanceof Map) {
            // TODO need a FK type
            return;
        } else if (value instanceof Array) {
            // TODO need a FK type
            return;
        } else {
            System.out.println("Unknown class type " + clazz);
            return;
        }
        
        if(type == null) {
            type = tempType;
        } else if(tempType == PrimitiveType.DOUBLE && (type == PrimitiveType.LONG
                ||type == PrimitiveType.INT)) {
            type = tempType;
        } else if(tempType == PrimitiveType.TINY_TEXT) {
            type = tempType;
        }

        // TODO Why is this here?
//        if (value.getClass().equals(String.class)) {
//            this.type = PrimitiveType.NTINY_TEXT;
//        } else if (value.getClass().equals(Double.class)) {
//            final Double asDouble = (Double) value;
//            if (asDouble.longValue() != asDouble) {
//                type = PrimitiveType.DOUBLE;
//            }
//        }
    }

    public void setNullable() {
        this.nullable = true;
    }

    @Override
    public String toString() {
        return "EntryDescription{" + "name=" + name + ", type=" + type + ", nullable=" + nullable + '}';
    }

}
