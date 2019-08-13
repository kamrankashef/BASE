package com.fakehospital.model;

import common.ServiceUtil;
import common.TypeExtract;
import java.util.Set;
import org.jsoup.nodes.Element;

final public class Meeting {

    public final String meetingUuid;
    public final String description;

    // AttributeBasedFromElemMethodGenerator
    public static Meeting fromElem(final Element elem) throws Exception {

        final Set<String> attributeTracker = ServiceUtil.extractAttributeName(elem.attributes());
        final String meetingUuid = ServiceUtil.getUUID();

        final String description = TypeExtract.getString((String) elem.attr("Description"));
        if (description == null) {
            throw new java.lang.RuntimeException("Got null value for description");
        }
        attributeTracker.remove("Description");

        if (!attributeTracker.isEmpty()) {
            throw new java.lang.RuntimeException("Unaccounted for attributes: " + attributeTracker);
        }

        return new Meeting(
                meetingUuid,
                description);

    }

    // ConstructorGenerator
    public Meeting(
            final String meetingUuid,
            final String description) {
        this.meetingUuid = meetingUuid;
        this.description = description;
    }


}
