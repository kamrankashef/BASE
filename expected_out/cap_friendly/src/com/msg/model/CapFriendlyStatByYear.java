package com.msg.model;

import common.ServiceUtil;
import common.TypeExtract;
import java.util.Map;

final public class CapFriendlyStatByYear {

    public final String capFriendlyStatByYearUuid;
    public final String capFriendlyPlayerGuid;
    public final Double a;
    public final Double g;
    public final Double gp;
    public final String league;
    public final Double pim;
    public final String playoffs;
    public final Double playoffs_a;
    public final Double playoffs_g;
    public final Double playoffs_gp;
    public final Double playoffs_pim;
    public final Double playoffs_tp;
    public final String season;
    public final String team;
    public final Double tp;

    // ConstructorGenerator
    public CapFriendlyStatByYear(
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
            final Double tp) {
        this.capFriendlyStatByYearUuid = capFriendlyStatByYearUuid;
        this.capFriendlyPlayerGuid = capFriendlyPlayerGuid;
        this.a = a;
        this.g = g;
        this.gp = gp;
        this.league = league;
        this.pim = pim;
        this.playoffs = playoffs;
        this.playoffs_a = playoffs_a;
        this.playoffs_g = playoffs_g;
        this.playoffs_gp = playoffs_gp;
        this.playoffs_pim = playoffs_pim;
        this.playoffs_tp = playoffs_tp;
        this.season = season;
        this.team = team;
        this.tp = tp;
    }

    // FromThirdPartyGenerator
    public static CapFriendlyStatByYear fromThirdPartyMap(final Map<String, Object> map,
            final String capFriendlyPlayerGuid) {

        final String capFriendlyStatByYearUuid = ServiceUtil.getGuid();
        final Double a = TypeExtract.getDouble((String) map.get("a"));
        final Double g = TypeExtract.getDouble((String) map.get("g"));
        final Double gp = TypeExtract.getDouble((String) map.get("gp"));
        final String league = TypeExtract.getString((String) map.get("league"));
        final Double pim = TypeExtract.getDouble((String) map.get("pim"));
        final String playoffs = TypeExtract.getString((String) map.get("playoffs"));
        final Double playoffs_a = TypeExtract.getDouble((String) map.get("playoffs_a"));
        final Double playoffs_g = TypeExtract.getDouble((String) map.get("playoffs_g"));
        final Double playoffs_gp = TypeExtract.getDouble((String) map.get("playoffs_gp"));
        final Double playoffs_pim = TypeExtract.getDouble((String) map.get("playoffs_pim"));
        final Double playoffs_tp = TypeExtract.getDouble((String) map.get("playoffs_tp"));
        final String season = TypeExtract.getString((String) map.get("season"));
        final String team = TypeExtract.getString((String) map.get("team"));
        final Double tp = TypeExtract.getDouble((String) map.get("tp"));

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

    // ToStringGenerator
    @Override
    public String toString() {

        final StringBuilder bldr = new StringBuilder();

        bldr.append("CapFriendlyStatByYear=[");
        bldr.append("cap_friendly_stat_by_year_uuid='").append(capFriendlyStatByYearUuid).append("'");
        bldr.append(",cap_friendly_player_guid='").append(capFriendlyPlayerGuid).append("'");
        bldr.append(",a='").append(a).append("'");
        bldr.append(",g='").append(g).append("'");
        bldr.append(",gp='").append(gp).append("'");
        bldr.append(",league='").append(league).append("'");
        bldr.append(",pim='").append(pim).append("'");
        bldr.append(",playoffs='").append(playoffs).append("'");
        bldr.append(",playoffs_a='").append(playoffs_a).append("'");
        bldr.append(",playoffs_g='").append(playoffs_g).append("'");
        bldr.append(",playoffs_gp='").append(playoffs_gp).append("'");
        bldr.append(",playoffs_pim='").append(playoffs_pim).append("'");
        bldr.append(",playoffs_tp='").append(playoffs_tp).append("'");
        bldr.append(",season='").append(season).append("'");
        bldr.append(",team='").append(team).append("'");
        bldr.append(",tp='").append(tp).append("'");
        bldr.append("]");

        return bldr.toString();
    }


}
