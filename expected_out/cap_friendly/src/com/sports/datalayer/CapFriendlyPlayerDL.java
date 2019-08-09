package com.sports.datalayer;

import com.sports.model.CapFriendlyPlayer;
import common.DBUtil;
import common.ErrorType;
import common.ExecutionResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

final public class CapFriendlyPlayerDL {

    private final String tableName;

    public CapFriendlyPlayerDL(final String schema) {
        tableName = schema + ".cap_friendly_player";
    }

    public String tableName() {
        return tableName;
    }

    private static final String FIELDS
            = "cap_friendly_player_uuid"
            + ", age"
            + ", birth_date_str"
            + ", draft_rount"
            + ", draft_year"
            + ", drafted_by"
            + ", drafted_overall"
            + ", elc_signing_age"
            + ", elite_prospect_id"
            + ", full_name"
            + ", height_str"
            + ", number"
            + ", shoots"
            + ", team_name"
            + ", waivers_signing_age"
            + ", created_at"
            + ", modified_at"
            + ", deleted_at";

    private static CapFriendlyPlayer fromRS(final ResultSet rs)
            throws SQLException {

        final String capFriendlyPlayerUuid = DBUtil.getNullableString(rs, "cap_friendly_player_uuid");
        final Integer age = DBUtil.getNullableInt(rs, "age");
        final String birth_date_str = DBUtil.getNullableString(rs, "birth_date_str");
        final Integer draft_rount = DBUtil.getNullableInt(rs, "draft_rount");
        final Integer draft_year = DBUtil.getNullableInt(rs, "draft_year");
        final String drafted_by = DBUtil.getNullableString(rs, "drafted_by");
        final Integer drafted_overall = DBUtil.getNullableInt(rs, "drafted_overall");
        final Integer elc_signing_age = DBUtil.getNullableInt(rs, "elc_signing_age");
        final Long elite_prospect_id = DBUtil.getNullableLong(rs, "elite_prospect_id");
        final String full_name = DBUtil.getNullableString(rs, "full_name");
        final String height_str = DBUtil.getNullableString(rs, "height_str");
        final Integer number = DBUtil.getNullableInt(rs, "number");
        final String shoots = DBUtil.getNullableString(rs, "shoots");
        final String team_name = DBUtil.getNullableString(rs, "team_name");
        final Integer waivers_signing_age = DBUtil.getNullableInt(rs, "waivers_signing_age");

        return new CapFriendlyPlayer(
            capFriendlyPlayerUuid,
            age,
            birth_date_str,
            draft_rount,
            draft_year,
            drafted_by,
            drafted_overall,
            elc_signing_age,
            elite_prospect_id,
            full_name,
            height_str,
            number,
            shoots,
            team_name,
            waivers_signing_age);
    }

    public ExecutionResult<CapFriendlyPlayer> getByGuid(final Connection conn, final String capFriendlyPlayerUuid) throws SQLException {
        final String GET_BY_GUID
                = "SELECT "
                + FIELDS
                + " FROM "
                + tableName()
                + " WHERE "
                + " deleted_at IS NULL AND "
                + "cap_friendly_player_uuid = ?";

        try(final PreparedStatement ps = conn.prepareStatement(GET_BY_GUID)) {
            final DBUtil dBUtil = new DBUtil(ps);
            dBUtil.setNullableString(capFriendlyPlayerUuid);
            final ResultSet rs = ps.executeQuery();
            if(!rs.next()) {
                return ExecutionResult.errorResult(new ErrorType("cap_friendly_player_not_found", "Cap Friendly Player not found"));
            }
            return ExecutionResult.successResult(CapFriendlyPlayerDL.fromRS(rs));
        }
    }

    public ExecutionResult<Void> insert(
            final Connection conn,
            final CapFriendlyPlayer capFriendlyPlayer) throws SQLException {

        return insert(
                conn,
                capFriendlyPlayer.capFriendlyPlayerUuid,
                capFriendlyPlayer.age,
                capFriendlyPlayer.birth_date_str,
                capFriendlyPlayer.draft_rount,
                capFriendlyPlayer.draft_year,
                capFriendlyPlayer.drafted_by,
                capFriendlyPlayer.drafted_overall,
                capFriendlyPlayer.elc_signing_age,
                capFriendlyPlayer.elite_prospect_id,
                capFriendlyPlayer.full_name,
                capFriendlyPlayer.height_str,
                capFriendlyPlayer.number,
                capFriendlyPlayer.shoots,
                capFriendlyPlayer.team_name,
                capFriendlyPlayer.waivers_signing_age);
    }

    private ExecutionResult<Void> insert(
            final Connection conn,
            final String capFriendlyPlayerUuid,
            final Integer age,
            final String birth_date_str,
            final Integer draft_rount,
            final Integer draft_year,
            final String drafted_by,
            final Integer drafted_overall,
            final Integer elc_signing_age,
            final Long elite_prospect_id,
            final String full_name,
            final String height_str,
            final Integer number,
            final String shoots,
            final String team_name,
            final Integer waivers_signing_age) throws SQLException {

        final String INSERT
                = "INSERT INTO "
                + tableName()
                + "(" + FIELDS + ")"
                + " VALUES "
                + "(" + DBUtil.questionMarks(18) + ")";

        try (final PreparedStatement ps = conn.prepareStatement(INSERT)) {

            final DBUtil dBUtil = new DBUtil(ps);

            dBUtil.setNullableString(capFriendlyPlayerUuid);
            dBUtil.setNullableInt(age);
            dBUtil.setNullableString(birth_date_str);
            dBUtil.setNullableInt(draft_rount);
            dBUtil.setNullableInt(draft_year);
            dBUtil.setNullableString(drafted_by);
            dBUtil.setNullableInt(drafted_overall);
            dBUtil.setNullableInt(elc_signing_age);
            dBUtil.setNullableLong(elite_prospect_id);
            dBUtil.setNullableString(full_name);
            dBUtil.setNullableString(height_str);
            dBUtil.setNullableInt(number);
            dBUtil.setNullableString(shoots);
            dBUtil.setNullableString(team_name);
            dBUtil.setNullableInt(waivers_signing_age);
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
