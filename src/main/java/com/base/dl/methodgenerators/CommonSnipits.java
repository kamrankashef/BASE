package com.base.dl.methodgenerators;

import com.base.gen.SourceBuilder;
import com.base.model.AbstractModel;

public class CommonSnipits {

    static SourceBuilder collectResults(SourceBuilder bldr,
            AbstractModel m,
            int baseIndent) {

        bldr.appendln(baseIndent + 2, "try(final ResultSet rs = ps.executeQuery()){");
        bldr.append(baseIndent + 3, "final java.util.Collection<").append(m.getJavaClassName())
                .appendln("> objects = new java.util.LinkedList<>();");

        bldr.appendln(baseIndent + 3, "while(rs.next()) {");
        bldr.append(baseIndent + 4, "objects.add(").append(m.dlName()).appendln(".fromRS(rs));");
        bldr.appendln(baseIndent + 3, "}");
        bldr.appendln(baseIndent + 3, "return objects;");
        bldr.appendln(baseIndent + 2, "}");
        return bldr;
    }

    static SourceBuilder makeInsertString(SourceBuilder bldr, AbstractModel m) {

        bldr.append(2, "final String INSERT\n")
                .append(4, "= \"INSERT INTO \"\n")
                .append(4, "+ tableName()\n")
                .append(4, "+ \"(\" + FIELDS + \")\"\n")
                .append(4, "+ \" VALUES \"\n")
                .append(4, "+ \"(\" + DBUtil.questionMarks(")
                .append("" + (3 + m.allOriginalFields().size())).appendlnln(") + \")\";");
        
        return bldr;
    }

}
