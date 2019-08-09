package base.dl.methodgenerators;

import base.files.FileI;
import base.gen.DLGen.DLMethodGenerator;
import base.model.AbstractField;
import base.model.AbstractModel;
import base.model.PrimitiveField;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DeleteByKeys implements DLMethodGenerator {

    @Override
    public String genMethod(final AbstractModel m) {
        final StringBuilder bldr = new StringBuilder();
        for (final List<AbstractField> key : m.getKeys()) {
            final DLMethodGenerator gen
                    = new DeleteById(key.toArray(new PrimitiveField[key.size()]));
            bldr.append(gen.genMethod(m));
        }
        return bldr.toString();
    }

    @Override
    public Set< String> requiredImports(final AbstractModel m, final String parentPackage) {
        final Set<String> imports = new TreeSet<>();
        imports.add("java.sql.Connection");
        imports.add("java.sql.PreparedStatement");
        imports.add("java.sql.SQLException");

        imports.add(FileI.COMMON_PKG + ".DBUtil");

        return imports;
    }

}
