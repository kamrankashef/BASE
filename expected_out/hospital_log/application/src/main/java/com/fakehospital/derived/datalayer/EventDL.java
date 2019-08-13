package com.fakehospital.derived.datalayer;

import com.fakehospital.derived.model.Event;
import common.DBUtil;
import common.ExecutionResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

final public class EventDL {

    private final String tableName;

    public EventDL(final String schema) {
        tableName = schema + ".event";
    }

    public String tableName() {
        return tableName;
    }

    private static final String FIELDS
            = "event_uuid"
            + ", custom_attribute"
            + ", event_eastern_time_zone_time"
            + ", event_id"
            + ", event_local_date"
            + ", event_local_time"
            + ", event_name"
            + ", event_number"
            + ", meeting_description"
            + ", meeting_employee_f_name"
            + ", meeting_employee_id"
            + ", meeting_employee_l_name"
            + ", shift_end_summary"
            + ", surgery_employee_f_name"
            + ", surgery_employee_id"
            + ", surgery_employee_l_name"
            + ", surgery_floor"
            + ", surgery_group_code"
            + ", surgery_group_id"
            + ", surgery_group_name"
            + ", surgery_room_number"
            + ", created_at"
            + ", modified_at"
            + ", deleted_at";

    private ExecutionResult<Void> insert(
            final Connection conn,
            final String eventUuid,
            final String customAttribute,
            final String eventEasternTimeZoneTime,
            final String eventId,
            final Integer eventLocalDate,
            final String eventLocalTime,
            final String eventName,
            final Integer eventNumber,
            final String meetingDescription,
            final String meetingEmployeeFName,
            final Long meetingEmployeeId,
            final String meetingEmployeeLName,
            final String shiftEndSummary,
            final String surgeryEmployeeFName,
            final Long surgeryEmployeeId,
            final String surgeryEmployeeLName,
            final Integer surgeryFloor,
            final String surgeryGroupCode,
            final Long surgeryGroupId,
            final String surgeryGroupName,
            final String surgeryRoomNumber) throws SQLException {

        final String INSERT
                = "INSERT INTO "
                + tableName()
                + "(" + FIELDS + ")"
                + " VALUES "
                + "(" + DBUtil.questionMarks(24) + ")";

        try (final PreparedStatement ps = conn.prepareStatement(INSERT)) {

            final DBUtil dBUtil = new DBUtil(ps);

            dBUtil.setNullableString(eventUuid);
            dBUtil.setNullableString(customAttribute);
            dBUtil.setNullableString(eventEasternTimeZoneTime);
            dBUtil.setNullableString(eventId);
            dBUtil.setNullableInt(eventLocalDate);
            dBUtil.setNullableString(eventLocalTime);
            dBUtil.setNullableString(eventName);
            dBUtil.setNullableInt(eventNumber);
            dBUtil.setNullableString(meetingDescription);
            dBUtil.setNullableString(meetingEmployeeFName);
            dBUtil.setNullableLong(meetingEmployeeId);
            dBUtil.setNullableString(meetingEmployeeLName);
            dBUtil.setNullableString(shiftEndSummary);
            dBUtil.setNullableString(surgeryEmployeeFName);
            dBUtil.setNullableLong(surgeryEmployeeId);
            dBUtil.setNullableString(surgeryEmployeeLName);
            dBUtil.setNullableInt(surgeryFloor);
            dBUtil.setNullableString(surgeryGroupCode);
            dBUtil.setNullableLong(surgeryGroupId);
            dBUtil.setNullableString(surgeryGroupName);
            dBUtil.setNullableString(surgeryRoomNumber);
            dBUtil.setNowTimestamp();
            dBUtil.setNowTimestamp();
            dBUtil.setNullableTimestamp(null);
            if (1 != ps.executeUpdate()) {
                return ExecutionResult.errorResult();
            }
            return ExecutionResult.successResult(null);
        }
    }

    public ExecutionResult<Void> insert(
            final Connection conn,
            final Event event) throws SQLException {

        return insert(
                conn,
                event.eventUuid,
                event.customAttribute,
                event.eventEasternTimeZoneTime,
                event.eventId,
                event.eventLocalDate,
                event.eventLocalTime,
                event.eventName,
                event.eventNumber,
                event.meetingDescription,
                event.meetingEmployeeFName,
                event.meetingEmployeeId,
                event.meetingEmployeeLName,
                event.shiftEndSummary,
                event.surgeryEmployeeFName,
                event.surgeryEmployeeId,
                event.surgeryEmployeeLName,
                event.surgeryFloor,
                event.surgeryGroupCode,
                event.surgeryGroupId,
                event.surgeryGroupName,
                event.surgeryRoomNumber);
    }
}
