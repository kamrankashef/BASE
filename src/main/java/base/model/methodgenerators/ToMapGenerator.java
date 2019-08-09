package base.model.methodgenerators;

import base.gen.ModelGen;
import base.gen.SourceBuilder;
import base.model.AbstractField;
import base.model.AbstractModel;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ToMapGenerator implements ModelGen.ModelMethodGenerator {

    @Override
    public String genMethod(final AbstractModel model, final String parentPackage) {
        final SourceBuilder bldr = new SourceBuilder();

//        bldr.append("\t@Override\n");
        bldr.append(1, "public Map<String, Object> toMap() {\n");
        bldr.append(2, "final Map<String, Object> retMap = new HashMap<>();\n\n");

        for (final AbstractField field : model.allOriginalFields()) {
            if (field.shareWithClient) {
                bldr.append(2, "retMap.put(\"").append(field.toDBName())
                        .append("\", ").append(field.toJavaVariableName())
                        .append(");\n");
            }
        }
        bldr.append(2, "return retMap;\n\n");
        // END toMap()
        bldr.append(1, "}");

        return bldr.toString();
    }

    @Override
    public Set<String> requiredImports() {
        final Set<String> imports = new TreeSet<>();
        imports.add(HashMap.class.getCanonicalName());
        imports.add(Map.class.getCanonicalName());
        return imports;
    }

}
