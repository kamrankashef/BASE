package com.sports.datalayer;

import com.sports.model.CapFriendlyContract;
import common.DBUtil;
import common.ErrorType;
import common.ExecutionResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

final public class CapFriendlyContractDL {

    private final String tableName;

    public CapFriendlyContractDL(final String schema) {
        tableName = schema + ".cap_friendly_contract";
    }

    public String tableName() {
        return tableName;
    }

    private static final String FIELDS
            = "cap_friendly_contract_uuid"
            + ", cap_friendly_player_guid"
            + ", ch"
            + ", expiry_status"
            + ", length_str"
            + ", signing_date"
            + ", signing_team"
            + ", type"
            + ", value"
            + ", created_at"
            + ", modified_at"
            + ", deleted_at";

    private static CapFriendlyContract fromRS(final ResultSet rs)
            throws SQLException {

        final String capFriendlyContractUuid = DBUtil.getNullableString(rs, "cap_friendly_contract_uuid");
        final String capFriendlyPlayerGuid = DBUtil.getNullableString(rs, "cap_friendly_player_guid");
        final Double ch = DBUtil.getNullableDouble(rs, "ch");
        final String expiry_status = DBUtil.getNullableString(rs, "expiry_status");
        final String length_str = DBUtil.getNullableString(rs, "length_str");
        final Date signing_date = DBUtil.getNullableDate(rs, "signing_date");
        final String signing_team = DBUtil.getNullableString(rs, "signing_team");
        final String type = DBUtil.getNullableString(rs, "type");
        final Double value = DBUtil.getNullableDouble(rs, "value");

        return new CapFriendlyContract(
            capFriendlyContractUuid,
            capFriendlyPlayerGuid,
            ch,
            expiry_status,
            length_str,
            signing_date,
            signing_team,
            type,
            value);
    }

    public ExecutionResult<CapFriendlyContract> getByGuid(final Connection conn, final String capFriendlyContractUuid) throws SQLException {
        final String GET_BY_GUID
                = "SELECT "
                + FIELDS
                + " FROM "
                + tableName()
                + " WHERE "
                + " deleted_at IS NULL AND "
                + "cap_friendly_contract_uuid = ?";

        try(final PreparedStatement ps = conn.prepareStatement(GET_BY_GUID)) {
            final DBUtil dBUtil = new DBUtil(ps);
            dBUtil.setNullableString(capFriendlyContractUuid);
            final ResultSet rs = ps.executeQuery();
            if(!rs.next()) {
                return ExecutionResult.errorResult(new ErrorType("cap_friendly_contract_not_found", "Cap Friendly Contract not found"));
            }
            return ExecutionResult.successResult(CapFriendlyContractDL.fromRS(rs));
        }
    }

    public ExecutionResult<Void> insert(
            final Connection conn,
            final CapFriendlyContract capFriendlyContract) throws SQLException {

        return insert(
                conn,
                capFriendlyContract.capFriendlyContractUuid,
                capFriendlyContract.capFriendlyPlayerGuid,
                capFriendlyContract.ch,
                capFriendlyContract.expiry_status,
                capFriendlyContract.length_str,
                capFriendlyContract.signing_date,
                capFriendlyContract.signing_team,
                capFriendlyContract.type,
                capFriendlyContract.value);
    }

    private ExecutionResult<Void> insert(
            final Connection conn,
            final String capFriendlyContractUuid,
            final String capFriendlyPlayerGuid,
            final Double ch,
            final String expiry_status,
            final String length_str,
            final Date signing_date,
            final String signing_team,
            final String type,
            final Double value) throws SQLException {

        final String INSERT
                = "INSERT INTO "
                + tableName()
                + "(" + FIELDS + ")"
                + " VALUES "
                + "(" + DBUtil.questionMarks(12) + ")";

        try (final PreparedStatement ps = conn.prepareStatement(INSERT)) {

            final DBUtil dBUtil = new DBUtil(ps);

            dBUtil.setNullableString(capFriendlyContractUuid);
            dBUtil.setNullableString(capFriendlyPlayerGuid);
            dBUtil.setNullableDouble(ch);
            dBUtil.setNullableString(expiry_status);
            dBUtil.setNullableString(length_str);
            dBUtil.setNullableTimestamp(signing_date);
            dBUtil.setNullableString(signing_team);
            dBUtil.setNullableString(type);
            dBUtil.setNullableDouble(value);
            dBUtil.setNowTimestamp();
            dBUtil.setNowTimestamp();
            dBUtil.setNullableTimestamp(null);
            if (1 != ps.executeUpdate()) {
                return ExecutionResult.errorResult();
            }
            return ExecutionResult.successResult(null);
        }
    }
}
