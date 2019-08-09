package com.msg.datalayer;

import com.msg.model.CapFriendlyStatByYear;
import common.DBUtil;
import common.ErrorType;
import common.ExecutionResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

final public class CapFriendlyStatByYearDL {

    private final String tableName;

    public CapFriendlyStatByYearDL(final String schema) {
        tableName = schema + ".cap_friendly_stat_by_year";
    }

    public String tableName() {
        return tableName;
    }

    private static final String FIELDS
            = "cap_friendly_stat_by_year_uuid"
            + ", cap_friendly_player_guid"
            + ", a"
            + ", g"
            + ", gp"
            + ", league"
            + ", pim"
            + ", playoffs"
            + ", playoffs_a"
            + ", playoffs_g"
            + ", playoffs_gp"
            + ", playoffs_pim"
            + ", playoffs_tp"
            + ", season"
            + ", team"
            + ", tp"
            + ", created_at"
            + ", modified_at"
            + ", deleted_at";

    private static CapFriendlyStatByYear fromRS(final ResultSet rs)
            throws SQLException {

        final String capFriendlyStatByYearUuid = DBUtil.getNullableString(rs, "cap_friendly_stat_by_year_uuid");
        final String capFriendlyPlayerGuid = DBUtil.getNullableString(rs, "cap_friendly_player_guid");
        final Double a = DBUtil.getNullableDouble(rs, "a");
        final Double g = DBUtil.getNullableDouble(rs, "g");
        final Double gp = DBUtil.getNullableDouble(rs, "gp");
        final String league = DBUtil.getNullableString(rs, "league");
        final Double pim = DBUtil.getNullableDouble(rs, "pim");
        final String playoffs = DBUtil.getNullableString(rs, "playoffs");
        final Double playoffs_a = DBUtil.getNullableDouble(rs, "playoffs_a");
        final Double playoffs_g = DBUtil.getNullableDouble(rs, "playoffs_g");
        final Double playoffs_gp = DBUtil.getNullableDouble(rs, "playoffs_gp");
        final Double playoffs_pim = DBUtil.getNullableDouble(rs, "playoffs_pim");
        final Double playoffs_tp = DBUtil.getNullableDouble(rs, "playoffs_tp");
        final String season = DBUtil.getNullableString(rs, "season");
        final String team = DBUtil.getNullableString(rs, "team");
        final Double tp = DBUtil.getNullableDouble(rs, "tp");

        return new CapFriendlyStatByYear(
            capFriendlyStatByYearUuid,
            capFriendlyPlayerGuid,
            a,
            g,
            gp,
            league,
            pim,
            playoffs,
            playoffs_a,
            playoffs_g,
            playoffs_gp,
            playoffs_pim,
            playoffs_tp,
            season,
            team,
            tp);
    }

    public ExecutionResult<CapFriendlyStatByYear> getByGuid(final Connection conn, final String capFriendlyStatByYearUuid) throws SQLException {
        final String GET_BY_GUID
                = "SELECT "
                + FIELDS
                + " FROM "
                + tableName()
                + " WHERE "
                + " deleted_at IS NULL AND "
                + "cap_friendly_stat_by_year_uuid = ?";

        try(final PreparedStatement ps = conn.prepareStatement(GET_BY_GUID)) {
            final DBUtil dBUtil = new DBUtil(ps);
            dBUtil.setNullableString(capFriendlyStatByYearUuid);
            final ResultSet rs = ps.executeQuery();
            if(!rs.next()) {
                return ExecutionResult.errorResult(new ErrorType("cap_friendly_stat_by_year_not_found", "Cap Friendly Stat By Year not found"));
            }
            return ExecutionResult.successResult(CapFriendlyStatByYearDL.fromRS(rs));
        }
    }

    public ExecutionResult<Void> insert(
            final Connection conn,
            final CapFriendlyStatByYear capFriendlyStatByYear) throws SQLException {

        return insert(
                conn,
                capFriendlyStatByYear.capFriendlyStatByYearUuid,
                capFriendlyStatByYear.capFriendlyPlayerGuid,
                capFriendlyStatByYear.a,
                capFriendlyStatByYear.g,
                capFriendlyStatByYear.gp,
                capFriendlyStatByYear.league,
                capFriendlyStatByYear.pim,
                capFriendlyStatByYear.playoffs,
                capFriendlyStatByYear.playoffs_a,
                capFriendlyStatByYear.playoffs_g,
                capFriendlyStatByYear.playoffs_gp,
                capFriendlyStatByYear.playoffs_pim,
                capFriendlyStatByYear.playoffs_tp,
                capFriendlyStatByYear.season,
                capFriendlyStatByYear.team,
                capFriendlyStatByYear.tp);
    }

    private ExecutionResult<Void> insert(
            final Connection conn,
            final String capFriendlyStatByYearUuid,
            final String capFriendlyPlayerGuid,
            final Double a,
            final Double g,
            final Double gp,
            final String league,
            final Double pim,
            final String playoffs,
            final Double playoffs_a,
            final Double playoffs_g,
            final Double playoffs_gp,
            final Double playoffs_pim,
            final Double playoffs_tp,
            final String season,
            final String team,
            final Double tp) throws SQLException {

        final String INSERT
                = "INSERT INTO "
                + tableName()
                + "(" + FIELDS + ")"
                + " VALUES "
                + "(" + DBUtil.questionMarks(19) + ")";

        try (final PreparedStatement ps = conn.prepareStatement(INSERT)) {

            final DBUtil dBUtil = new DBUtil(ps);

            dBUtil.setNullableString(capFriendlyStatByYearUuid);
            dBUtil.setNullableString(capFriendlyPlayerGuid);
            dBUtil.setNullableDouble(a);
            dBUtil.setNullableDouble(g);
            dBUtil.setNullableDouble(gp);
            dBUtil.setNullableString(league);
            dBUtil.setNullableDouble(pim);
            dBUtil.setNullableString(playoffs);
            dBUtil.setNullableDouble(playoffs_a);
            dBUtil.setNullableDouble(playoffs_g);
            dBUtil.setNullableDouble(playoffs_gp);
            dBUtil.setNullableDouble(playoffs_pim);
            dBUtil.setNullableDouble(playoffs_tp);
            dBUtil.setNullableString(season);
            dBUtil.setNullableString(team);
            dBUtil.setNullableDouble(tp);
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
