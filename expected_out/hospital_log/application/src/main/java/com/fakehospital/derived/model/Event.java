package com.fakehospital.derived.model;


final public class Event {

    public final String eventUuid;
    public final String customAttribute;
    public final String eventEasternTimeZoneTime;
    public final String eventId;
    public final Integer eventLocalDate;
    public final String eventLocalTime;
    public final String eventName;
    public final Integer eventNumber;
    public final String meetingDescription;
    public final Long meetingEmployeeId;
    public final String shiftEndSummary;
    public final Long surgeryEmployeeId;
    public final Integer surgeryFloor;
    public final Long surgeryGroupId;
    public final String surgeryGroupRole;
    public final String surgeryRoomNumber;

    // ConstructorGenerator
    public Event(
            final String eventUuid,
            final String customAttribute,
            final String eventEasternTimeZoneTime,
            final String eventId,
            final Integer eventLocalDate,
            final String eventLocalTime,
            final String eventName,
            final Integer eventNumber,
            final String meetingDescription,
            final Long meetingEmployeeId,
            final String shiftEndSummary,
            final Long surgeryEmployeeId,
            final Integer surgeryFloor,
            final Long surgeryGroupId,
            final String surgeryGroupRole,
            final String surgeryRoomNumber) {
        this.eventUuid = eventUuid;
        this.customAttribute = customAttribute;
        this.eventEasternTimeZoneTime = eventEasternTimeZoneTime;
        this.eventId = eventId;
        this.eventLocalDate = eventLocalDate;
        this.eventLocalTime = eventLocalTime;
        this.eventName = eventName;
        this.eventNumber = eventNumber;
        this.meetingDescription = meetingDescription;
        this.meetingEmployeeId = meetingEmployeeId;
        this.shiftEndSummary = shiftEndSummary;
        this.surgeryEmployeeId = surgeryEmployeeId;
        this.surgeryFloor = surgeryFloor;
        this.surgeryGroupId = surgeryGroupId;
        this.surgeryGroupRole = surgeryGroupRole;
        this.surgeryRoomNumber = surgeryRoomNumber;
    }

    // DerivedModelConstructorGenerator
    public Event(
            final String customAttribute,
            final com.fakehospital.model.Event event,
            final com.fakehospital.model.Meeting meeting,
            final com.fakehospital.model.EmployeeMeeting employeeMeeting,
            final com.fakehospital.model.ShiftEnd shiftEnd,
            final com.fakehospital.model.Surgery surgery,
            final com.fakehospital.model.EmployeeGroupSurgery employeeGroupSurgery,
            final com.fakehospital.model.GroupSurgery groupSurgery) {
        this.eventUuid = common.ServiceUtil.getUUID();
        this.customAttribute = customAttribute;
        this.eventEasternTimeZoneTime = event.easternTimeZoneTime;
        this.eventId = event.id;
        this.eventLocalDate = event.localDate;
        this.eventLocalTime = event.localTime;
        this.eventName = event.name;
        this.eventNumber = event.number;
        this.meetingDescription = meeting.description;
        this.meetingEmployeeId = employeeMeeting.id;
        this.shiftEndSummary = shiftEnd.summary;
        this.surgeryFloor = surgery.floor;
        this.surgeryRoomNumber = surgery.roomNumber;
        this.surgeryEmployeeId = employeeGroupSurgery.id;
        this.surgeryGroupId = groupSurgery.id;
        this.surgeryGroupRole = groupSurgery.role;
    }


}
