package com.sports.datalayer;

import com.sports.model.CapFriendlyContractYear;
import common.DBUtil;
import common.ErrorType;
import common.ExecutionResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

final public class CapFriendlyContractYearDL {

    private final String tableName;

    public CapFriendlyContractYearDL(final String schema) {
        tableName = schema + ".cap_friendly_contract_year";
    }

    public String tableName() {
        return tableName;
    }

    private static final String FIELDS
            = "cap_friendly_contract_year_uuid"
            + ", cap_friendly_contract_guid"
            + ", ahl_salary"
            + ", avv"
            + ", cap_hit"
            + ", clause"
            + ", nhl_salary"
            + ", p_bonus"
            + ", s_bonus"
            + ", season"
            + ", created_at"
            + ", modified_at"
            + ", deleted_at";

    private static CapFriendlyContractYear fromRS(final ResultSet rs)
            throws SQLException {

        final String capFriendlyContractYearUuid = DBUtil.getNullableString(rs, "cap_friendly_contract_year_uuid");
        final String capFriendlyContractGuid = DBUtil.getNullableString(rs, "cap_friendly_contract_guid");
        final Double ahl_salary = DBUtil.getNullableDouble(rs, "ahl_salary");
        final Double avv = DBUtil.getNullableDouble(rs, "avv");
        final Double cap_hit = DBUtil.getNullableDouble(rs, "cap_hit");
        final String clause = DBUtil.getNullableString(rs, "clause");
        final Double nhl_salary = DBUtil.getNullableDouble(rs, "nhl_salary");
        final Double p_bonus = DBUtil.getNullableDouble(rs, "p_bonus");
        final Double s_bonus = DBUtil.getNullableDouble(rs, "s_bonus");
        final String season = DBUtil.getNullableString(rs, "season");

        return new CapFriendlyContractYear(
            capFriendlyContractYearUuid,
            capFriendlyContractGuid,
            ahl_salary,
            avv,
            cap_hit,
            clause,
            nhl_salary,
            p_bonus,
            s_bonus,
            season);
    }

    public ExecutionResult<CapFriendlyContractYear> getByGuid(final Connection conn, final String capFriendlyContractYearUuid) throws SQLException {
        final String GET_BY_GUID
                = "SELECT "
                + FIELDS
                + " FROM "
                + tableName()
                + " WHERE "
                + " deleted_at IS NULL AND "
                + "cap_friendly_contract_year_uuid = ?";

        try(final PreparedStatement ps = conn.prepareStatement(GET_BY_GUID)) {
            final DBUtil dBUtil = new DBUtil(ps);
            dBUtil.setNullableString(capFriendlyContractYearUuid);
            final ResultSet rs = ps.executeQuery();
            if(!rs.next()) {
                return ExecutionResult.errorResult(new ErrorType("cap_friendly_contract_year_not_found", "Cap Friendly Contract Year not found"));
            }
            return ExecutionResult.successResult(CapFriendlyContractYearDL.fromRS(rs));
        }
    }

    public ExecutionResult<Void> insert(
            final Connection conn,
            final CapFriendlyContractYear capFriendlyContractYear) throws SQLException {

        return insert(
                conn,
                capFriendlyContractYear.capFriendlyContractYearUuid,
                capFriendlyContractYear.capFriendlyContractGuid,
                capFriendlyContractYear.ahl_salary,
                capFriendlyContractYear.avv,
                capFriendlyContractYear.cap_hit,
                capFriendlyContractYear.clause,
                capFriendlyContractYear.nhl_salary,
                capFriendlyContractYear.p_bonus,
                capFriendlyContractYear.s_bonus,
                capFriendlyContractYear.season);
    }

    private ExecutionResult<Void> insert(
            final Connection conn,
            final String capFriendlyContractYearUuid,
            final String capFriendlyContractGuid,
            final Double ahl_salary,
            final Double avv,
            final Double cap_hit,
            final String clause,
            final Double nhl_salary,
            final Double p_bonus,
            final Double s_bonus,
            final String season) throws SQLException {

        final String INSERT
                = "INSERT INTO "
                + tableName()
                + "(" + FIELDS + ")"
                + " VALUES "
                + "(" + DBUtil.questionMarks(13) + ")";

        try (final PreparedStatement ps = conn.prepareStatement(INSERT)) {

            final DBUtil dBUtil = new DBUtil(ps);

            dBUtil.setNullableString(capFriendlyContractYearUuid);
            dBUtil.setNullableString(capFriendlyContractGuid);
            dBUtil.setNullableDouble(ahl_salary);
            dBUtil.setNullableDouble(avv);
            dBUtil.setNullableDouble(cap_hit);
            dBUtil.setNullableString(clause);
            dBUtil.setNullableDouble(nhl_salary);
            dBUtil.setNullableDouble(p_bonus);
            dBUtil.setNullableDouble(s_bonus);
            dBUtil.setNullableString(season);
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
