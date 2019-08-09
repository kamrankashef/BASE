package base.parsergen.json;

// http://www.nhl.com/stats/rest/grouped/skaters/shootout/season/skatershootout?cayenneExp=seasonId=19992000%20and%20gameTypeId=2&factCayenneExp=gamesPlayed%3E=1
// http://www.nhl.com/stats/rest/grouped/skaters/shootout/season/skatershootout?cayenneExp=seasonId=20162017%20and%20gameTypeId=2
// http://www.nhl.com/stats/rest/grouped/skaters/shootout/season/skatershootout?cayenneExp=seasonId=20162017%20and%20gameTypeId=2
public class EndPointDesc {

    final String name;
    final String subject;
    final String category;
    final boolean queryBySeason;
    final boolean queryByGame;
    final String service;
    final int startYear; // 1996;

    public EndPointDesc(
            final String name,
            final String subject,
            final String category,
            final boolean queryBySeason,
            final boolean queryByGame,
            final String service) {
        this(
                name,
                subject,
                category,
                queryBySeason,
                queryByGame,
                service,
                1996);
    }

    public EndPointDesc(
            final String name,
            final String subject,
            final String category,
            final boolean queryBySeason,
            final boolean queryByGame,
            final String service,
            final int startYear) {
        this.name = name;
        this.subject = subject;
        this.category = category;
        this.queryBySeason = queryBySeason;
        this.queryByGame = queryByGame;
        this.service = service;
        this.startYear = startYear;
    }

    private static final String BASE_URL = "http://www.nhl.com/stats/rest/grouped";
    private static final String SEASON = "season";
    private static final String GAME = "game";

    private String getServiceBase(final String seasonOrGame) {
        return BASE_URL + "/" + subject + "/" + category + "/" + seasonOrGame + "/" + service;
    }

    private int getSeasonType(final boolean isPlayoffs) {
        return isPlayoffs ? 3 : 2;
    }

    public String getUrlForSeason(final int seasonEndYear, final boolean isPlayoffs) {
        final String season = (seasonEndYear - 1) + "" + seasonEndYear;
        String url = getServiceBase(SEASON) + "?cayenneExp=seasonId=" + season
                + "%20and%20gameTypeId=" + getSeasonType(isPlayoffs);
//        if ("skaters".equals(subject)) {
//            url += "&factCayenneExp=gamesPlayed%3E=1";
//        }
        return url;
    }

    private static String yearToRange(final int year) {
        final String start = year + "-01-01T00:00:00.000Z";
        final String end = year + "-12-31T23:59:59.000Z";
        return "gameDate%3E=%22" + start
                + "%22%20and%20"
                + "gameDate%3C=%22" + end;
    }

    public String getUrlForGame(final int seasonEndYear, final boolean isPlayoffs) {

//        final String season = (seasonEnd - 1) + "" + seasonEnd;
        // cayenneExp=gameDate%3E=%2c2016-12-18T05:00:00.000Z%22%20and%20gameDate%3C=%222017-01-17T05:00:00.000Z%22%20and%20gameTypeId=2
        String url = getServiceBase(GAME) + "?cayenneExp=" + yearToRange(seasonEndYear)
                + "%22%20and%20gameTypeId=" + getSeasonType(isPlayoffs);
//        if ("skaters".equals(subject)) {
//            url += "&factCayenneExp=gamesPlayed%3E=1";
//        }
        return url;
    }

    public String getBySeasonSampleUrl() {
        if (!queryBySeason) {
            throw new RuntimeException("Method not supported");
        }

        return getUrlForSeason(2012, false);
    }

    public String getByGameSampleUrl() {
        if (!queryByGame) {
            throw new RuntimeException("Method not supported");
        }

        return getUrlForGame(2012, false);
    }
}
