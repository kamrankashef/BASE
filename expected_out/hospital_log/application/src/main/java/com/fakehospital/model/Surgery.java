package com.fakehospital.model;

import common.ServiceUtil;
import common.TypeExtract;
import java.util.Set;
import org.jsoup.nodes.Element;

final public class Surgery {

    public final String surgeryUuid;
    public final Integer floor;
    public final String roomNumber;

    // AttributeBasedFromElemMethodGenerator
    public static Surgery fromElem(final Element elem) throws Exception {

        final Set<String> attributeTracker = ServiceUtil.extractAttributeName(elem.attributes());
        final String surgeryUuid = ServiceUtil.getUUID();

        final Integer floor = TypeExtract.getInteger((String) elem.attr("Floor"));
        if (floor == null) {
            throw new java.lang.RuntimeException("Got null value for floor");
        }
        attributeTracker.remove("Floor");

        final String roomNumber = TypeExtract.getString((String) elem.attr("RoomNumber"));
        if (roomNumber == null) {
            throw new java.lang.RuntimeException("Got null value for roomNumber");
        }
        attributeTracker.remove("RoomNumber");

        if (!attributeTracker.isEmpty()) {
            throw new java.lang.RuntimeException("Unaccounted for attributes: " + attributeTracker);
        }

        return new Surgery(
                surgeryUuid,
                floor,
                roomNumber);

    }

    // ConstructorGenerator
    public Surgery(
            final String surgeryUuid,
            final Integer floor,
            final String roomNumber) {
        this.surgeryUuid = surgeryUuid;
        this.floor = floor;
        this.roomNumber = roomNumber;
    }


}
