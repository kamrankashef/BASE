package com.base.parsergen.rules;

import com.base.model.PrimitiveType;

public interface TypeSetsI {

//    public PrimitiveType nameToType(final String elemName, final String attrName);
    public static TypeSetsI ALL_STRING_TYPE_SET = (
            final String modelName,
            final String originalColName,
            final Object... sampleValues) -> {
        return PrimitiveType.TEXT;
    };

    public PrimitiveType nameToType(final String modelName,
            final String originalColName,
            final Object... sampleValue);

    default boolean lastAllowedNull() {
        return false;
    }
}
