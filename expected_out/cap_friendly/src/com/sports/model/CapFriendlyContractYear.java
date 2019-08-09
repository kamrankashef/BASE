package com.sports.model;

import common.ServiceUtil;
import common.TypeExtract;
import java.util.Map;

final public class CapFriendlyContractYear {

    public final String capFriendlyContractYearUuid;
    public final String capFriendlyContractGuid;
    public final Double ahl_salary;
    public final Double avv;
    public final Double cap_hit;
    public final String clause;
    public final Double nhl_salary;
    public final Double p_bonus;
    public final Double s_bonus;
    public final String season;

    // ConstructorGenerator
    public CapFriendlyContractYear(
            final String capFriendlyContractYearUuid,
            final String capFriendlyContractGuid,
            final Double ahl_salary,
            final Double avv,
            final Double cap_hit,
            final String clause,
            final Double nhl_salary,
            final Double p_bonus,
            final Double s_bonus,
            final String season) {
        this.capFriendlyContractYearUuid = capFriendlyContractYearUuid;
        this.capFriendlyContractGuid = capFriendlyContractGuid;
        this.ahl_salary = ahl_salary;
        this.avv = avv;
        this.cap_hit = cap_hit;
        this.clause = clause;
        this.nhl_salary = nhl_salary;
        this.p_bonus = p_bonus;
        this.s_bonus = s_bonus;
        this.season = season;
    }

    // FromThirdPartyGenerator
    public static CapFriendlyContractYear fromThirdPartyMap(final Map<String, Object> map,
            final String capFriendlyContractGuid) {

        final String capFriendlyContractYearUuid = ServiceUtil.getGuid();
        final Double ahl_salary = TypeExtract.getDouble((String) map.get("ahl_salary"));
        final Double avv = TypeExtract.getDouble((String) map.get("avv"));
        final Double cap_hit = TypeExtract.getDouble((String) map.get("cap_hit"));
        final String clause = TypeExtract.getString((String) map.get("clause"));
        final Double nhl_salary = TypeExtract.getDouble((String) map.get("nhl_salary"));
        final Double p_bonus = TypeExtract.getDouble((String) map.get("p_bonus"));
        final Double s_bonus = TypeExtract.getDouble((String) map.get("s_bonus"));
        final String season = TypeExtract.getString((String) map.get("season"));

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

    // ToStringGenerator
    @Override
    public String toString() {

        final StringBuilder bldr = new StringBuilder();

        bldr.append("CapFriendlyContractYear=[");
        bldr.append("cap_friendly_contract_year_uuid='").append(capFriendlyContractYearUuid).append("'");
        bldr.append(",cap_friendly_contract_guid='").append(capFriendlyContractGuid).append("'");
        bldr.append(",ahl_salary='").append(ahl_salary).append("'");
        bldr.append(",avv='").append(avv).append("'");
        bldr.append(",cap_hit='").append(cap_hit).append("'");
        bldr.append(",clause='").append(clause).append("'");
        bldr.append(",nhl_salary='").append(nhl_salary).append("'");
        bldr.append(",p_bonus='").append(p_bonus).append("'");
        bldr.append(",s_bonus='").append(s_bonus).append("'");
        bldr.append(",season='").append(season).append("'");
        bldr.append("]");

        return bldr.toString();
    }


}
