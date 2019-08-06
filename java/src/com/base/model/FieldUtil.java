package com.base.model;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class FieldUtil {

    public static Map<String, PrimitiveField> fieldsToMap(final Collection<PrimitiveField> fields) {
        return fields.stream().collect(Collectors.toMap(
                t -> t.getName(),
                t -> t
        ));
    }
}
