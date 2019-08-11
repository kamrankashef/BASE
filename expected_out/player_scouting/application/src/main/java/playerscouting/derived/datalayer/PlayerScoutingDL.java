package playerscouting.derived.datalayer;

import common.DBUtil;
import common.ExecutionResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import playerscouting.derived.model.PlayerScouting;

final public class PlayerScoutingDL {

    private final String tableName;

    public PlayerScoutingDL(final String schema) {
        tableName = schema + ".player_scouting";
    }

    public String tableName() {
        return tableName;
    }

    private static final String FIELDS
            = "player_scouting_uuid"
            + ", \"3pa\""
            + ", \"3pm\""
            + ", ast"
            + ", away_team"
            + ", birth_date"
            + ", birth_date_aug"
            + ", blk"
            + ", contribution_bullseye"
            + ", contribution_high"
            + ", contribution_low"
            + ", created_on"
            + ", created_on_aug"
            + ", defense"
            + ", drb"
            + ", edited_on"
            + ", edited_on_aug"
            + ", eval"
            + ", fga"
            + ", fgm"
            + ", fta"
            + ", ftm"
            + ", game_id"
            + ", height"
            + ", home_team"
            + ", intel_background"
            + ", intel_character"
            + ", intel_game_day"
            + ", intel_injury"
            + ", intel_law"
            + ", intel_level"
            + ", intel_on_court_evals"
            + ", intel_org"
            + ", intel_personality"
            + ", intel_relationship"
            + ", intel_social_media"
            + ", knicks_fit"
            + ", league"
            + ", level"
            + ", nba_position_role"
            + ", nba_skill"
            + ", offense"
            + ", orb"
            + ", pf"
            + ", player"
            + ", player_id"
            + ", pts"
            + ", report_date"
            + ", report_date_aug"
            + ", report_id"
            + ", scout"
            + ", scout_id"
            + ", seconds_played"
            + ", setting"
            + ", stl"
            + ", team"
            + ", tov"
            + ", weight"
            + ", created_at"
            + ", modified_at"
            + ", deleted_at";

    private ExecutionResult<Void> insert(
            final Connection conn,
            final String playerScoutingUuid,
            final Integer _3pa,
            final Integer _3pm,
            final Integer ast,
            final String awayTeam,
            final String birthDate,
            final Date birthDateAug,
            final Integer blk,
            final String contributionBullseye,
            final String contributionHigh,
            final String contributionLow,
            final String createdOn,
            final Date createdOnAug,
            final String defense,
            final Integer drb,
            final String editedOn,
            final Date editedOnAug,
            final String eval,
            final Integer fga,
            final Integer fgm,
            final Integer fta,
            final Integer ftm,
            final Long gameId,
            final String height,
            final String homeTeam,
            final String intelBackground,
            final String intelCharacter,
            final String intelGameDay,
            final String intelInjury,
            final String intelLaw,
            final String intelLevel,
            final String intelOnCourtEvals,
            final String intelOrg,
            final String intelPersonality,
            final String intelRelationship,
            final String intelSocialMedia,
            final String knicksFit,
            final String league,
            final String level,
            final String nbaPositionRole,
            final String nbaSkill,
            final String offense,
            final Integer orb,
            final Integer pf,
            final String player,
            final Long playerId,
            final Integer pts,
            final String reportDate,
            final Date reportDateAug,
            final Long reportId,
            final String scout,
            final Long scoutId,
            final Integer secondsPlayed,
            final String setting,
            final Integer stl,
            final String team,
            final Integer tov,
            final Integer weight) throws SQLException {

        final String INSERT
                = "INSERT INTO "
                + tableName()
                + "(" + FIELDS + ")"
                + " VALUES "
                + "(" + DBUtil.questionMarks(61) + ")";

        try (final PreparedStatement ps = conn.prepareStatement(INSERT)) {

            final DBUtil dBUtil = new DBUtil(ps);

            dBUtil.setNullableString(playerScoutingUuid);
            dBUtil.setNullableInt(_3pa);
            dBUtil.setNullableInt(_3pm);
            dBUtil.setNullableInt(ast);
            dBUtil.setNullableString(awayTeam);
            dBUtil.setNullableString(birthDate);
            dBUtil.setNullableTimestamp(birthDateAug);
            dBUtil.setNullableInt(blk);
            dBUtil.setNullableString(contributionBullseye);
            dBUtil.setNullableString(contributionHigh);
            dBUtil.setNullableString(contributionLow);
            dBUtil.setNullableString(createdOn);
            dBUtil.setNullableTimestamp(createdOnAug);
            dBUtil.setNullableString(defense);
            dBUtil.setNullableInt(drb);
            dBUtil.setNullableString(editedOn);
            dBUtil.setNullableTimestamp(editedOnAug);
            dBUtil.setNullableString(eval);
            dBUtil.setNullableInt(fga);
            dBUtil.setNullableInt(fgm);
            dBUtil.setNullableInt(fta);
            dBUtil.setNullableInt(ftm);
            dBUtil.setNullableLong(gameId);
            dBUtil.setNullableString(height);
            dBUtil.setNullableString(homeTeam);
            dBUtil.setNullableString(intelBackground);
            dBUtil.setNullableString(intelCharacter);
            dBUtil.setNullableString(intelGameDay);
            dBUtil.setNullableString(intelInjury);
            dBUtil.setNullableString(intelLaw);
            dBUtil.setNullableString(intelLevel);
            dBUtil.setNullableString(intelOnCourtEvals);
            dBUtil.setNullableString(intelOrg);
            dBUtil.setNullableString(intelPersonality);
            dBUtil.setNullableString(intelRelationship);
            dBUtil.setNullableString(intelSocialMedia);
            dBUtil.setNullableString(knicksFit);
            dBUtil.setNullableString(league);
            dBUtil.setNullableString(level);
            dBUtil.setNullableString(nbaPositionRole);
            dBUtil.setNullableString(nbaSkill);
            dBUtil.setNullableString(offense);
            dBUtil.setNullableInt(orb);
            dBUtil.setNullableInt(pf);
            dBUtil.setNullableString(player);
            dBUtil.setNullableLong(playerId);
            dBUtil.setNullableInt(pts);
            dBUtil.setNullableString(reportDate);
            dBUtil.setNullableTimestamp(reportDateAug);
            dBUtil.setNullableLong(reportId);
            dBUtil.setNullableString(scout);
            dBUtil.setNullableLong(scoutId);
            dBUtil.setNullableInt(secondsPlayed);
            dBUtil.setNullableString(setting);
            dBUtil.setNullableInt(stl);
            dBUtil.setNullableString(team);
            dBUtil.setNullableInt(tov);
            dBUtil.setNullableInt(weight);
            dBUtil.setNowTimestamp();
            dBUtil.setNowTimestamp();
            dBUtil.setNullableTimestamp(null);
            if (1 != ps.executeUpdate()) {
                return ExecutionResult.errorResult();
            }
            return ExecutionResult.successResult(null);
        }
    }

    public void truncate(final Connection conn) throws SQLException {
        final String TRUNCATE
                = "TRUNCATE TABLE "
                + tableName();

            try(final PreparedStatement ps = conn.prepareStatement(TRUNCATE)) {
            ps.executeUpdate();
        }
    }

    public ExecutionResult<Void> insert(
            final Connection conn,
            final PlayerScouting playerScouting) throws SQLException {

        return insert(
                conn,
                playerScouting.playerScoutingUuid,
                playerScouting._3pa,
                playerScouting._3pm,
                playerScouting.ast,
                playerScouting.awayTeam,
                playerScouting.birthDate,
                playerScouting.birthDateAug,
                playerScouting.blk,
                playerScouting.contributionBullseye,
                playerScouting.contributionHigh,
                playerScouting.contributionLow,
                playerScouting.createdOn,
                playerScouting.createdOnAug,
                playerScouting.defense,
                playerScouting.drb,
                playerScouting.editedOn,
                playerScouting.editedOnAug,
                playerScouting.eval,
                playerScouting.fga,
                playerScouting.fgm,
                playerScouting.fta,
                playerScouting.ftm,
                playerScouting.gameId,
                playerScouting.height,
                playerScouting.homeTeam,
                playerScouting.intelBackground,
                playerScouting.intelCharacter,
                playerScouting.intelGameDay,
                playerScouting.intelInjury,
                playerScouting.intelLaw,
                playerScouting.intelLevel,
                playerScouting.intelOnCourtEvals,
                playerScouting.intelOrg,
                playerScouting.intelPersonality,
                playerScouting.intelRelationship,
                playerScouting.intelSocialMedia,
                playerScouting.knicksFit,
                playerScouting.league,
                playerScouting.level,
                playerScouting.nbaPositionRole,
                playerScouting.nbaSkill,
                playerScouting.offense,
                playerScouting.orb,
                playerScouting.pf,
                playerScouting.player,
                playerScouting.playerId,
                playerScouting.pts,
                playerScouting.reportDate,
                playerScouting.reportDateAug,
                playerScouting.reportId,
                playerScouting.scout,
                playerScouting.scoutId,
                playerScouting.secondsPlayed,
                playerScouting.setting,
                playerScouting.stl,
                playerScouting.team,
                playerScouting.tov,
                playerScouting.weight);
    }

    private void addBatch(final DBUtil dBUtil, final PlayerScouting playerScouting) throws SQLException {

        dBUtil.setNullableString(playerScouting.playerScoutingUuid);
        dBUtil.setNullableInt(playerScouting._3pa);
        dBUtil.setNullableInt(playerScouting._3pm);
        dBUtil.setNullableInt(playerScouting.ast);
        dBUtil.setNullableString(playerScouting.awayTeam);
        dBUtil.setNullableString(playerScouting.birthDate);
        dBUtil.setNullableTimestamp(playerScouting.birthDateAug);
        dBUtil.setNullableInt(playerScouting.blk);
        dBUtil.setNullableString(playerScouting.contributionBullseye);
        dBUtil.setNullableString(playerScouting.contributionHigh);
        dBUtil.setNullableString(playerScouting.contributionLow);
        dBUtil.setNullableString(playerScouting.createdOn);
        dBUtil.setNullableTimestamp(playerScouting.createdOnAug);
        dBUtil.setNullableString(playerScouting.defense);
        dBUtil.setNullableInt(playerScouting.drb);
        dBUtil.setNullableString(playerScouting.editedOn);
        dBUtil.setNullableTimestamp(playerScouting.editedOnAug);
        dBUtil.setNullableString(playerScouting.eval);
        dBUtil.setNullableInt(playerScouting.fga);
        dBUtil.setNullableInt(playerScouting.fgm);
        dBUtil.setNullableInt(playerScouting.fta);
        dBUtil.setNullableInt(playerScouting.ftm);
        dBUtil.setNullableLong(playerScouting.gameId);
        dBUtil.setNullableString(playerScouting.height);
        dBUtil.setNullableString(playerScouting.homeTeam);
        dBUtil.setNullableString(playerScouting.intelBackground);
        dBUtil.setNullableString(playerScouting.intelCharacter);
        dBUtil.setNullableString(playerScouting.intelGameDay);
        dBUtil.setNullableString(playerScouting.intelInjury);
        dBUtil.setNullableString(playerScouting.intelLaw);
        dBUtil.setNullableString(playerScouting.intelLevel);
        dBUtil.setNullableString(playerScouting.intelOnCourtEvals);
        dBUtil.setNullableString(playerScouting.intelOrg);
        dBUtil.setNullableString(playerScouting.intelPersonality);
        dBUtil.setNullableString(playerScouting.intelRelationship);
        dBUtil.setNullableString(playerScouting.intelSocialMedia);
        dBUtil.setNullableString(playerScouting.knicksFit);
        dBUtil.setNullableString(playerScouting.league);
        dBUtil.setNullableString(playerScouting.level);
        dBUtil.setNullableString(playerScouting.nbaPositionRole);
        dBUtil.setNullableString(playerScouting.nbaSkill);
        dBUtil.setNullableString(playerScouting.offense);
        dBUtil.setNullableInt(playerScouting.orb);
        dBUtil.setNullableInt(playerScouting.pf);
        dBUtil.setNullableString(playerScouting.player);
        dBUtil.setNullableLong(playerScouting.playerId);
        dBUtil.setNullableInt(playerScouting.pts);
        dBUtil.setNullableString(playerScouting.reportDate);
        dBUtil.setNullableTimestamp(playerScouting.reportDateAug);
        dBUtil.setNullableLong(playerScouting.reportId);
        dBUtil.setNullableString(playerScouting.scout);
        dBUtil.setNullableLong(playerScouting.scoutId);
        dBUtil.setNullableInt(playerScouting.secondsPlayed);
        dBUtil.setNullableString(playerScouting.setting);
        dBUtil.setNullableInt(playerScouting.stl);
        dBUtil.setNullableString(playerScouting.team);
        dBUtil.setNullableInt(playerScouting.tov);
        dBUtil.setNullableInt(playerScouting.weight);
        dBUtil.setNowTimestamp();
        dBUtil.setNowTimestamp();
        dBUtil.setNullableTimestamp(null);
        dBUtil.addBatch();
    }


    public void insertBatch(
            final Connection conn, final Collection<PlayerScouting> objects) throws SQLException {

        final int batchSize = 5000;
        if (objects.isEmpty()) {
            return;
        }
        final String INSERT
                = "INSERT INTO "
                + tableName()
                + "(" + FIELDS + ")"
                + " VALUES "
                + "(" + DBUtil.questionMarks(61) + ")";

        int count = 0;
        try (final PreparedStatement ps = conn.prepareStatement(INSERT)) {

            final DBUtil dBUtil = new DBUtil(ps);

            for (final PlayerScouting playerScouting : objects) {
                count++;
                addBatch( dBUtil, playerScouting);
                if(count % batchSize == 0) {
                    ps.executeBatch();
                }
            }
            if(count % batchSize != 0) {
                ps.executeBatch();
            }
        }
    }
}
