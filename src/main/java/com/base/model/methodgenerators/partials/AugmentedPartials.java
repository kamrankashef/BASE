package com.base.model.methodgenerators.partials;

import com.base.gen.SourceBuilder;
import com.base.model.AbstractModel;
import com.base.model.PrimitiveField;
import java.util.Map;

public class AugmentedPartials {

    public static void genAugmented(final int indent, final SourceBuilder bldr, final AbstractModel model) {
        for (final Map.Entry<PrimitiveField, String> field : model.getAugmentedFields().entrySet()) {
            bldr.append(indent, field.getKey().toJavaDeclaration())
                    .append(" = ")
                    .append(field.getValue()).
                    appendln(";");
        }
    }
}
