package com.fakehospital.model;

import common.ServiceUtil;
import common.TypeExtract;
import java.util.Set;
import org.jsoup.nodes.Element;

final public class ShiftEnd {

    public final String shiftEndUuid;
    public final String summary;

    // AttributeBasedFromElemMethodGenerator
    public static ShiftEnd fromElem(final Element elem) throws Exception {

        final Set<String> attributeTracker = ServiceUtil.extractAttributeName(elem.attributes());
        final String shiftEndUuid = ServiceUtil.getUUID();

        final String summary = TypeExtract.getString((String) elem.attr("Summary"));
        if (summary == null) {
            throw new java.lang.RuntimeException("Got null value for summary");
        }
        attributeTracker.remove("Summary");

        if (!attributeTracker.isEmpty()) {
            throw new java.lang.RuntimeException("Unaccounted for attributes: " + attributeTracker);
        }

        return new ShiftEnd(
                shiftEndUuid,
                summary);

    }

    // ConstructorGenerator
    public ShiftEnd(
            final String shiftEndUuid,
            final String summary) {
        this.shiftEndUuid = shiftEndUuid;
        this.summary = summary;
    }


}
