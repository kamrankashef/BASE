package com.fakehospital.model;

import common.ServiceUtil;
import common.TypeExtract;
import java.util.Date;
import java.util.Set;
import org.jsoup.nodes.Element;

final public class HospitalEvents {

    public final String hospitalEventsUuid;
    public final String date;
    public final String easternTimeZoneTime;
    public final String filename;
    public final Long id;
    public final String localTime;
    public final String localTimeZone;
    public final Date localTimeAug;

    // AttributeBasedFromElemMethodGenerator
    public static HospitalEvents fromElem(final Element elem) throws Exception {

        final Set<String> attributeTracker = ServiceUtil.extractAttributeName(elem.attributes());
        final String hospitalEventsUuid = ServiceUtil.getUUID();

        final String date = TypeExtract.getString((String) elem.attr("Date"));
        if (date == null) {
            throw new java.lang.RuntimeException("Got null value for date");
        }
        attributeTracker.remove("Date");

        final String easternTimeZoneTime = TypeExtract.getString((String) elem.attr("EasternTimeZoneTime"));
        if (easternTimeZoneTime == null) {
            throw new java.lang.RuntimeException("Got null value for easternTimeZoneTime");
        }
        attributeTracker.remove("EasternTimeZoneTime");

        final String filename = TypeExtract.getString((String) elem.attr("Filename"));
        if (filename == null) {
            throw new java.lang.RuntimeException("Got null value for filename");
        }
        attributeTracker.remove("Filename");

        final Long id = TypeExtract.getLong((String) elem.attr("ID"));
        if (id == null) {
            throw new java.lang.RuntimeException("Got null value for id");
        }
        attributeTracker.remove("ID");

        final String localTime = TypeExtract.getString((String) elem.attr("LocalTime"));
        if (localTime == null) {
            throw new java.lang.RuntimeException("Got null value for localTime");
        }
        attributeTracker.remove("LocalTime");

        final String localTimeZone = TypeExtract.getString((String) elem.attr("LocalTimeZone"));
        if (localTimeZone == null) {
            throw new java.lang.RuntimeException("Got null value for localTimeZone");
        }
        attributeTracker.remove("LocalTimeZone");

        if (!attributeTracker.isEmpty()) {
            throw new java.lang.RuntimeException("Unaccounted for attributes: " + attributeTracker);
        }

        final Date localTimeAug = common.DateUtil.extract("yyyyMMdd hh:mm:ss.S", date + " " + localTime, localTimeZone);
        return new HospitalEvents(
                hospitalEventsUuid,
                date,
                easternTimeZoneTime,
                filename,
                id,
                localTime,
                localTimeZone,
                localTimeAug);

    }

    // ConstructorGenerator
    public HospitalEvents(
            final String hospitalEventsUuid,
            final String date,
            final String easternTimeZoneTime,
            final String filename,
            final Long id,
            final String localTime,
            final String localTimeZone,
            final Date localTimeAug) {
        this.hospitalEventsUuid = hospitalEventsUuid;
        this.date = date;
        this.easternTimeZoneTime = easternTimeZoneTime;
        this.filename = filename;
        this.id = id;
        this.localTime = localTime;
        this.localTimeZone = localTimeZone;
        this.localTimeAug = localTimeAug;
    }


}
