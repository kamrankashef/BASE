package com.fakehospital.model;

import common.ServiceUtil;
import common.TypeExtract;
import java.util.Set;
import org.jsoup.nodes.Element;

final public class Group {

    public final String groupUuid;
    public final String code;
    public final Long id;
    public final String name;

    // AttributeBasedFromElemMethodGenerator
    public static Group fromElem(final Element elem) throws Exception {

        final Set<String> attributeTracker = ServiceUtil.extractAttributeName(elem.attributes());
        final String groupUuid = ServiceUtil.getUUID();

        final String code = TypeExtract.getString((String) elem.attr("Code"));
        if (code == null) {
            throw new java.lang.RuntimeException("Got null value for code");
        }
        attributeTracker.remove("Code");

        final Long id = TypeExtract.getLong((String) elem.attr("ID"));
        if (id == null) {
            throw new java.lang.RuntimeException("Got null value for id");
        }
        attributeTracker.remove("ID");

        final String name = TypeExtract.getString((String) elem.attr("Name"));
        if (name == null) {
            throw new java.lang.RuntimeException("Got null value for name");
        }
        attributeTracker.remove("Name");

        if (!attributeTracker.isEmpty()) {
            throw new java.lang.RuntimeException("Unaccounted for attributes: " + attributeTracker);
        }

        return new Group(
                groupUuid,
                code,
                id,
                name);

    }

    // ConstructorGenerator
    public Group(
            final String groupUuid,
            final String code,
            final Long id,
            final String name) {
        this.groupUuid = groupUuid;
        this.code = code;
        this.id = id;
        this.name = name;
    }


}
