package base.model.methodgenerators;

import base.gen.SourceBuilder;
import base.model.AbstractModel;

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
