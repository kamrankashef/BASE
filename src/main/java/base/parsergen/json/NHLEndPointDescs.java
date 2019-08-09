package base.parsergen.json;

public interface NHLEndPointDescs {

    final static EndPointDesc[] END_POINTS = new EndPointDesc[]{
        new EndPointDesc("SkaterSummary", "skaters", "basic", true, true, "skatersummary"),
//        new EndPointDesc("SkaterFranchiseSummary", "skaters", "basic", true, true, "skaterfranchisesummary"),
        new EndPointDesc("SkaterGoals", "skaters", "basic", true, true, "goals"),
        new EndPointDesc("SkaterPoints", "skaters", "basic", true, true, "points"),
        new EndPointDesc("SkaterFaceoffs", "skaters", "basic", true, true, "faceoffs"),
        new EndPointDesc("SkaterPowerPlay", "skaters", "basic", true, true, "powerplay"),
        new EndPointDesc("SkaterPenaltyKill", "skaters", "basic", true, true, "penaltykill"),
        new EndPointDesc("SkaterRealtime", "skaters", "basic", true, true, "realtime"),
        new EndPointDesc("SkaterPenalties", "skaters", "basic", true, true, "penalties"),
        new EndPointDesc("SkaterTimeOnIce", "skaters", "basic", true, true, "timeonice"),
        new EndPointDesc("SkaterShootout", "skaters", "shootout", true, true, "skatershootout"),
        new EndPointDesc("SkaterBio", "skaters", "basic", true, false, "bios"),
        new EndPointDesc("SkaterPercentages", "skaters", "shooting", true, true, "skaterpercentages", 2011),
        new EndPointDesc("SkaterSummaryShooting", "skaters", "shooting", true, true, "skatersummaryshooting", 2011),
        new EndPointDesc("SkaterScoring", "skaters", "core", true, true, "skaterscoring", 2011),
        new EndPointDesc("SkaterFaceoffByZone", "skaters", "core", true, true, "faceoffsbyzone", 2011),
        new EndPointDesc("TeamSummary", "team", "basic", true, true, "teamsummary"),
//        new EndPointDesc("TeamFranchiseSummary", "team", "basic", true, true, "franchisesummary"),
        new EndPointDesc("TeamPowerPlay", "team", "basic", true, true, "powerplay"),
        new EndPointDesc("TeamPenaltyKill", "team", "basic", true, true, "penaltykill"),
        new EndPointDesc("TeamPenalties", "team", "basic", true, true, "penalties"),
        new EndPointDesc("TeamRealtime", "team", "basic", true, true, "realtime"),
        new EndPointDesc("TeamFaceoffsByStrength", "team", "basic", true, true, "faceoffsbystrength"),
        new EndPointDesc("TeamShootout", "team", "shootout", true, true, "shootout"),
        new EndPointDesc("TeamDaysBetweenGames", "team", "basic", false, true, "teamdaysbetweengames"),
        new EndPointDesc("TeamLeadingTrailing", "team", "basic", true, false, "leadingtrailing"),
        new EndPointDesc("TeamOutShootOutShotBy", "team", "basic", true, false, "outshootoutshotby"),
        new EndPointDesc("TeamGoalGames", "team", "basic", true, false, "teamgoalgames"),
        new EndPointDesc("TeamGoalsByStrength", "team", "basic", true, false, "goalsbystrength"),
        new EndPointDesc("TeamGoalsAgainstByStrength", "team", "basic", true, false, "goalsagainstbystrength"),
        new EndPointDesc("TeamGoalsByPeriod", "team", "basic", true, false, "goalsbyperiod"),
        new EndPointDesc("TeamPowerplayTime", "team", "specialteamstime", true, false, "powerplaytime")
    };
}
