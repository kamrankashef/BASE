package com.base.model.methodgenerators;

import com.base.files.FileI;
import com.base.gen.ModelGen;
import com.base.gen.SourceBuilder;
import com.base.model.ForiegnKeyField;
import com.base.model.AbstractModel;
import com.base.model.PrimitiveField;
import com.base.model.methodgenerators.partials.AugmentedPartials;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ModelGen.ModelMethodGenerator.class)
public class FromThirdPartyGenerator implements ModelGen.ModelMethodGenerator {

    @Override
    public String genMethod(final AbstractModel model, final String parentPackage) {
        final SourceBuilder bldr = new SourceBuilder();

        bldr.append(1, "public static ").append(model.getJavaClassName()).append(" fromThirdPartyMap(final Map<String, Object> map");
        for (final ForiegnKeyField field : model.getFkFields()) {
            bldr.append(",\n").append(3, field.toJavaDeclaration());
        }
        bldr.append(") {\n\n");

        bldr.append(2, model.getGuidField().toJavaDeclaration())
                .append(" = ServiceUtil.getGuid();\n");

        for (final PrimitiveField field : model.getPrimitiveFields()) {
            final String sourceName;

            if (model.getThirdPartyMapping().containsKey(field.getName())) {
                sourceName = model.getThirdPartyMapping().get(field.getName());
            } else {
                sourceName = field.getName();
            }

            bldr.append(2, field.toJavaDeclaration())
                    .append(" = TypeExtract." + field.getPrimitiveType().extractMethod + "((String) map.get(\"")
                    .append(sourceName)
                    .append("\"));\n");
        }

        AugmentedPartials.genAugmented(2, bldr, model);

        bldr.append("\n").append(2, "return ").append(model.calledConstructor(2));
        bldr.append(";\n").append(1, "}");

        return bldr.toString();
    }

    @Override
    public Set<String> requiredImports() {
        final Set<String> imports = new TreeSet<>();
        imports.add(FileI.COMMON_PKG + ".ServiceUtil");
        imports.add(FileI.COMMON_PKG + ".TypeExtract");
        imports.add(Map.class.getCanonicalName());

        return imports;
    }

}
