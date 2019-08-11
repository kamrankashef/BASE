package playerscouting.derived.model;

import java.util.Date;

final public class PlayerScouting {

    public final String playerScoutingUuid;
    public final Integer _3pa;
    public final Integer _3pm;
    public final Integer ast;
    public final String awayTeam;
    public final String birthDate;
    public final Date birthDateAug;
    public final Integer blk;
    public final String contributionBullseye;
    public final String contributionHigh;
    public final String contributionLow;
    public final String createdOn;
    public final Date createdOnAug;
    public final String defense;
    public final Integer drb;
    public final String editedOn;
    public final Date editedOnAug;
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
    public final Date reportDateAug;
    public final Long reportId;
    public final String scout;
    public final Long scoutId;
    public final Integer secondsPlayed;
    public final String setting;
    public final Integer stl;
    public final String team;
    public final Integer tov;
    public final Integer weight;

    // ConstructorGenerator
    public PlayerScouting(
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
            final Integer weight) {
        this.playerScoutingUuid = playerScoutingUuid;
        this._3pa = _3pa;
        this._3pm = _3pm;
        this.ast = ast;
        this.awayTeam = awayTeam;
        this.birthDate = birthDate;
        this.birthDateAug = birthDateAug;
        this.blk = blk;
        this.contributionBullseye = contributionBullseye;
        this.contributionHigh = contributionHigh;
        this.contributionLow = contributionLow;
        this.createdOn = createdOn;
        this.createdOnAug = createdOnAug;
        this.defense = defense;
        this.drb = drb;
        this.editedOn = editedOn;
        this.editedOnAug = editedOnAug;
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
        this.reportDateAug = reportDateAug;
        this.reportId = reportId;
        this.scout = scout;
        this.scoutId = scoutId;
        this.secondsPlayed = secondsPlayed;
        this.setting = setting;
        this.stl = stl;
        this.team = team;
        this.tov = tov;
        this.weight = weight;
    }

    // DerivedModelConstructorGenerator
    public PlayerScouting(
            final playerscouting.model.PlayerScoutingFeed playerScoutingFeed) {
        this.playerScoutingUuid = common.ServiceUtil.getUUID();
        this._3pa = playerScoutingFeed._3pa;
        this._3pm = playerScoutingFeed._3pm;
        this.ast = playerScoutingFeed.ast;
        this.awayTeam = playerScoutingFeed.awayTeam;
        this.birthDate = playerScoutingFeed.birthDate;
        this.blk = playerScoutingFeed.blk;
        this.contributionBullseye = playerScoutingFeed.contributionBullseye;
        this.contributionHigh = playerScoutingFeed.contributionHigh;
        this.contributionLow = playerScoutingFeed.contributionLow;
        this.createdOn = playerScoutingFeed.createdOn;
        this.defense = playerScoutingFeed.defense;
        this.drb = playerScoutingFeed.drb;
        this.editedOn = playerScoutingFeed.editedOn;
        this.eval = playerScoutingFeed.eval;
        this.fga = playerScoutingFeed.fga;
        this.fgm = playerScoutingFeed.fgm;
        this.fta = playerScoutingFeed.fta;
        this.ftm = playerScoutingFeed.ftm;
        this.gameId = playerScoutingFeed.gameId;
        this.height = playerScoutingFeed.height;
        this.homeTeam = playerScoutingFeed.homeTeam;
        this.intelBackground = playerScoutingFeed.intelBackground;
        this.intelCharacter = playerScoutingFeed.intelCharacter;
        this.intelGameDay = playerScoutingFeed.intelGameDay;
        this.intelInjury = playerScoutingFeed.intelInjury;
        this.intelLaw = playerScoutingFeed.intelLaw;
        this.intelLevel = playerScoutingFeed.intelLevel;
        this.intelOnCourtEvals = playerScoutingFeed.intelOnCourtEvals;
        this.intelOrg = playerScoutingFeed.intelOrg;
        this.intelPersonality = playerScoutingFeed.intelPersonality;
        this.intelRelationship = playerScoutingFeed.intelRelationship;
        this.intelSocialMedia = playerScoutingFeed.intelSocialMedia;
        this.knicksFit = playerScoutingFeed.knicksFit;
        this.league = playerScoutingFeed.league;
        this.level = playerScoutingFeed.level;
        this.nbaPositionRole = playerScoutingFeed.nbaPositionRole;
        this.nbaSkill = playerScoutingFeed.nbaSkill;
        this.offense = playerScoutingFeed.offense;
        this.orb = playerScoutingFeed.orb;
        this.pf = playerScoutingFeed.pf;
        this.player = playerScoutingFeed.player;
        this.playerId = playerScoutingFeed.playerId;
        this.pts = playerScoutingFeed.pts;
        this.reportDate = playerScoutingFeed.reportDate;
        this.reportId = playerScoutingFeed.reportId;
        this.scout = playerScoutingFeed.scout;
        this.scoutId = playerScoutingFeed.scoutId;
        this.secondsPlayed = playerScoutingFeed.secondsPlayed;
        this.setting = playerScoutingFeed.setting;
        this.stl = playerScoutingFeed.stl;
        this.team = playerScoutingFeed.team;
        this.tov = playerScoutingFeed.tov;
        this.weight = playerScoutingFeed.weight;
        this.birthDateAug = playerScoutingFeed.birthDateAug;
        this.createdOnAug = playerScoutingFeed.createdOnAug;
        this.editedOnAug = playerScoutingFeed.editedOnAug;
        this.reportDateAug = playerScoutingFeed.reportDateAug;
    }


}
