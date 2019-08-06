package com.base.model.methodgenerators;

import com.base.gen.SourceBuilder;
import com.base.model.AbstractModel;

public class CommonCodeBlocks {

    public static SourceBuilder initUUIDField(final SourceBuilder bldr, 
            final int index,
            final AbstractModel model) {
        bldr.indent(index)
                .append(model.getGuidField().toJavaDeclaration())
                .append(" = ").append("ServiceUtil.getUUID();");
        return bldr;
    }
}
