package com.fakehospital.model;

import common.ServiceUtil;
import common.TypeExtract;
import java.util.Set;
import org.jsoup.nodes.Element;

final public class Event {

    public final String eventUuid;
    public final String easternTimeZoneTime;
    public final String id;
    public final Integer localDate;
    public final String localTime;
    public final String name;
    public final Integer number;

    // AttributeBasedFromElemMethodGenerator
    public static Event fromElem(final Element elem) throws Exception {

        final Set<String> attributeTracker = ServiceUtil.extractAttributeName(elem.attributes());
        final String eventUuid = ServiceUtil.getUUID();

        final String easternTimeZoneTime = TypeExtract.getString((String) elem.attr("EasternTimeZoneTime"));
        if (easternTimeZoneTime == null) {
            throw new java.lang.RuntimeException("Got null value for easternTimeZoneTime");
        }
        attributeTracker.remove("EasternTimeZoneTime");

        final String id = TypeExtract.getString((String) elem.attr("ID"));
        if (id == null) {
            throw new java.lang.RuntimeException("Got null value for id");
        }
        attributeTracker.remove("ID");

        final Integer localDate = TypeExtract.getInteger((String) elem.attr("LocalDate"));
        if (localDate == null) {
            throw new java.lang.RuntimeException("Got null value for localDate");
        }
        attributeTracker.remove("LocalDate");

        final String localTime = TypeExtract.getString((String) elem.attr("LocalTime"));
        if (localTime == null) {
            throw new java.lang.RuntimeException("Got null value for localTime");
        }
        attributeTracker.remove("LocalTime");

        final String name = TypeExtract.getString((String) elem.attr("Name"));
        if (name == null) {
            throw new java.lang.RuntimeException("Got null value for name");
        }
        attributeTracker.remove("Name");

        final Integer number = TypeExtract.getInteger((String) elem.attr("Number"));
        if (number == null) {
            throw new java.lang.RuntimeException("Got null value for number");
        }
        attributeTracker.remove("Number");

        if (!attributeTracker.isEmpty()) {
            throw new java.lang.RuntimeException("Unaccounted for attributes: " + attributeTracker);
        }

        return new Event(
                eventUuid,
                easternTimeZoneTime,
                id,
                localDate,
                localTime,
                name,
                number);

    }

    // ConstructorGenerator
    public Event(
            final String eventUuid,
            final String easternTimeZoneTime,
            final String id,
            final Integer localDate,
            final String localTime,
            final String name,
            final Integer number) {
        this.eventUuid = eventUuid;
        this.easternTimeZoneTime = easternTimeZoneTime;
        this.id = id;
        this.localDate = localDate;
        this.localTime = localTime;
        this.name = name;
        this.number = number;
    }


}
