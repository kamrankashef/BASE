package playerscouting.model;

import common.ServiceUtil;
import common.TypeExtract;
import java.text.ParseException;
import java.util.Date;
import org.apache.commons.csv.CSVRecord;

final public class PlayerScoutingFeed {

    public final String playerScoutingFeedUuid;
    public final Integer _3pa;
    public final Integer _3pm;
    public final Integer ast;
    public final String awayTeam;
    public final String birthDate;
    public final Integer blk;
    public final String contributionBullseye;
    public final String contributionHigh;
    public final String contributionLow;
    public final String createdOn;
    public final String defense;
    public final Integer drb;
    public final String editedOn;
    public final String eval;
    public final Integer fga;
    public final Integer fgm;
    public final Integer fta;
    public final Integer ftm;
    public final Long gameId;
    public final String height;
    public final String homeTeam;
    public final String intelBackground;
    public final String intelCharacter;
    public final String intelGameDay;
    public final String intelInjury;
    public final String intelLaw;
    public final String intelLevel;
    public final String intelOnCourtEvals;
    public final String intelOrg;
    public final String intelPersonality;
    public final String intelRelationship;
    public final String intelSocialMedia;
    public final String knicksFit;
    public final String league;
    public final String level;
    public final String nbaPositionRole;
    public final String nbaSkill;
    public final String offense;
    public final Integer orb;
    public final Integer pf;
    public final String player;
    public final Long playerId;
    public final Integer pts;
    public final String reportDate;
    public final Long reportId;
    public final String scout;
    public final Long scoutId;
    public final Integer secondsPlayed;
    public final String setting;
    public final Integer stl;
    public final String team;
    public final Integer tov;
    public final Integer weight;
    public final Date birthDateAug;
    public final Date createdOnAug;
    public final Date editedOnAug;
    public final Date reportDateAug;

    // FromCSVGenerator
    public static PlayerScoutingFeed fromRow(final CSVRecord record) throws ParseException {

        final String playerScoutingFeedUuid = ServiceUtil.getUUID();

        final Integer _3pa = TypeExtract.getInteger(record.get("3PA"));
        final Integer _3pm = TypeExtract.getInteger(record.get("3PM"));
        final Integer ast = TypeExtract.getInteger(record.get("AST"));
        final String awayTeam = TypeExtract.getString(record.get("AwayTeam"));
        final String birthDate = TypeExtract.getString(record.get("BirthDate"));
        final Integer blk = TypeExtract.getInteger(record.get("BLK"));
        final String contributionBullseye = TypeExtract.getString(record.get("ContributionBullseye"));
        final String contributionHigh = TypeExtract.getString(record.get("ContributionHigh"));
        final String contributionLow = TypeExtract.getString(record.get("ContributionLow"));
        final String createdOn = TypeExtract.getString(record.get("CreatedOn"));
        final String defense = TypeExtract.getString(record.get("Defense"));
        final Integer drb = TypeExtract.getInteger(record.get("DRB"));
        final String editedOn = TypeExtract.getString(record.get("EditedOn"));
        final String eval = TypeExtract.getString(record.get("Eval"));
        final Integer fga = TypeExtract.getInteger(record.get("FGA"));
        final Integer fgm = TypeExtract.getInteger(record.get("FGM"));
        final Integer fta = TypeExtract.getInteger(record.get("FTA"));
        final Integer ftm = TypeExtract.getInteger(record.get("FTM"));
        final Long gameId = TypeExtract.getLong(record.get("GameID"));
        final String height = TypeExtract.getString(record.get("Height"));
        final String homeTeam = TypeExtract.getString(record.get("HomeTeam"));
        final String intelBackground = TypeExtract.getString(record.get("IntelBackground"));
        final String intelCharacter = TypeExtract.getString(record.get("IntelCharacter"));
        final String intelGameDay = TypeExtract.getString(record.get("IntelGameDay"));
        final String intelInjury = TypeExtract.getString(record.get("IntelInjury"));
        final String intelLaw = TypeExtract.getString(record.get("IntelLaw"));
        final String intelLevel = TypeExtract.getString(record.get("IntelLevel"));
        final String intelOnCourtEvals = TypeExtract.getString(record.get("IntelOnCourtEvals"));
        final String intelOrg = TypeExtract.getString(record.get("IntelOrg"));
        final String intelPersonality = TypeExtract.getString(record.get("IntelPersonality"));
        final String intelRelationship = TypeExtract.getString(record.get("IntelRelationship"));
        final String intelSocialMedia = TypeExtract.getString(record.get("IntelSocialMedia"));
        final String knicksFit = TypeExtract.getString(record.get("KnicksFit"));
        final String league = TypeExtract.getString(record.get("League"));
        final String level = TypeExtract.getString(record.get("Level"));
        final String nbaPositionRole = TypeExtract.getString(record.get("NBAPositionRole"));
        final String nbaSkill = TypeExtract.getString(record.get("NBASkill"));
        final String offense = TypeExtract.getString(record.get("Offense"));
        final Integer orb = TypeExtract.getInteger(record.get("ORB"));
        final Integer pf = TypeExtract.getInteger(record.get("PF"));
        final String player = TypeExtract.getString(record.get("Player"));
        final Long playerId = TypeExtract.getLong(record.get("PlayerID"));
        final Integer pts = TypeExtract.getInteger(record.get("PTS"));
        final String reportDate = TypeExtract.getString(record.get("ReportDate"));
        final Long reportId = TypeExtract.getLong(record.get("ReportID"));
        final String scout = TypeExtract.getString(record.get("Scout"));
        final Long scoutId = TypeExtract.getLong(record.get("ScoutID"));
        final Integer secondsPlayed = TypeExtract.getInteger(record.get("SecondsPlayed"));
        final String setting = TypeExtract.getString(record.get("Setting"));
        final Integer stl = TypeExtract.getInteger(record.get("STL"));
        final String team = TypeExtract.getString(record.get("Team"));
        final Integer tov = TypeExtract.getInteger(record.get("TOV"));
        final Integer weight = TypeExtract.getInteger(record.get("Weight"));

        final Date birthDateAug = birthDate == null ? null : common.DateUtil.extract(common.DateUtil.SIMPLE_DATE, birthDate, "EDT");
        final Date createdOnAug = createdOn == null ? null : common.DateUtil.extract(common.DateUtil.SIMPLE_DATE, createdOn, "EDT");
        final Date editedOnAug = editedOn == null ? null : common.DateUtil.extract(common.DateUtil.SIMPLE_DATE, editedOn, "EDT");
        final Date reportDateAug = reportDate == null ? null : common.DateUtil.extract(common.DateUtil.SIMPLE_DATE, reportDate, "EDT");

        return new PlayerScoutingFeed(
            playerScoutingFeedUuid,
            _3pa,
            _3pm,
            ast,
            awayTeam,
            birthDate,
            blk,
            contributionBullseye,
            contributionHigh,
            contributionLow,
            createdOn,
            defense,
            drb,
            editedOn,
            eval,
            fga,
            fgm,
            fta,
            ftm,
            gameId,
            height,
            homeTeam,
            intelBackground,
            intelCharacter,
            intelGameDay,
            intelInjury,
            intelLaw,
            intelLevel,
            intelOnCourtEvals,
            intelOrg,
            intelPersonality,
            intelRelationship,
            intelSocialMedia,
            knicksFit,
            league,
            level,
            nbaPositionRole,
            nbaSkill,
            offense,
            orb,
            pf,
            player,
            playerId,
            pts,
            reportDate,
            reportId,
            scout,
            scoutId,
            secondsPlayed,
            setting,
            stl,
            team,
            tov,
            weight,
            birthDateAug,
            createdOnAug,
            editedOnAug,
            reportDateAug);
    }

    // ConstructorGenerator
    public PlayerScoutingFeed(
            final String playerScoutingFeedUuid,
            final Integer _3pa,
            final Integer _3pm,
            final Integer ast,
            final String awayTeam,
            final String birthDate,
            final Integer blk,
            final String contributionBullseye,
            final String contributionHigh,
            final String contributionLow,
            final String createdOn,
            final String defense,
            final Integer drb,
            final String editedOn,
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
            final Long reportId,
            final String scout,
            final Long scoutId,
            final Integer secondsPlayed,
            final String setting,
            final Integer stl,
            final String team,
            final Integer tov,
            final Integer weight,
            final Date birthDateAug,
            final Date createdOnAug,
            final Date editedOnAug,
            final Date reportDateAug) {
        this.playerScoutingFeedUuid = playerScoutingFeedUuid;
        this._3pa = _3pa;
        this._3pm = _3pm;
        this.ast = ast;
        this.awayTeam = awayTeam;
        this.birthDate = birthDate;
        this.blk = blk;
        this.contributionBullseye = contributionBullseye;
        this.contributionHigh = contributionHigh;
        this.contributionLow = contributionLow;
        this.createdOn = createdOn;
        this.defense = defense;
        this.drb = drb;
        this.editedOn = editedOn;
        this.eval = eval;
        this.fga = fga;
        this.fgm = fgm;
        this.fta = fta;
        this.ftm = ftm;
        this.gameId = gameId;
        this.height = height;
        this.homeTeam = homeTeam;
        this.intelBackground = intelBackground;
        this.intelCharacter = intelCharacter;
        this.intelGameDay = intelGameDay;
        this.intelInjury = intelInjury;
        this.intelLaw = intelLaw;
        this.intelLevel = intelLevel;
        this.intelOnCourtEvals = intelOnCourtEvals;
        this.intelOrg = intelOrg;
        this.intelPersonality = intelPersonality;
        this.intelRelationship = intelRelationship;
        this.intelSocialMedia = intelSocialMedia;
        this.knicksFit = knicksFit;
        this.league = league;
        this.level = level;
        this.nbaPositionRole = nbaPositionRole;
        this.nbaSkill = nbaSkill;
        this.offense = offense;
        this.orb = orb;
        this.pf = pf;
        this.player = player;
        this.playerId = playerId;
        this.pts = pts;
        this.reportDate = reportDate;
        this.reportId = reportId;
        this.scout = scout;
        this.scoutId = scoutId;
        this.secondsPlayed = secondsPlayed;
        this.setting = setting;
        this.stl = stl;
        this.team = team;
        this.tov = tov;
        this.weight = weight;
        this.birthDateAug = birthDateAug;
        this.createdOnAug = createdOnAug;
        this.editedOnAug = editedOnAug;
        this.reportDateAug = reportDateAug;
    }


}
