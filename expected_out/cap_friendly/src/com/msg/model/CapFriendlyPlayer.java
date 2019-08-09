package com.msg.model;

import common.ServiceUtil;
import common.TypeExtract;
import java.util.Map;

final public class CapFriendlyPlayer {

    public final String capFriendlyPlayerUuid;
    public final Integer age;
    public final String birth_date_str;
    public final Integer draft_rount;
    public final Integer draft_year;
    public final String drafted_by;
    public final Integer drafted_overall;
    public final Integer elc_signing_age;
    public final Long elite_prospect_id;
    public final String full_name;
    public final String height_str;
    public final Integer number;
    public final String shoots;
    public final String team_name;
    public final Integer waivers_signing_age;

    // ConstructorGenerator
    public CapFriendlyPlayer(
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
            final Integer waivers_signing_age) {
        this.capFriendlyPlayerUuid = capFriendlyPlayerUuid;
        this.age = age;
        this.birth_date_str = birth_date_str;
        this.draft_rount = draft_rount;
        this.draft_year = draft_year;
        this.drafted_by = drafted_by;
        this.drafted_overall = drafted_overall;
        this.elc_signing_age = elc_signing_age;
        this.elite_prospect_id = elite_prospect_id;
        this.full_name = full_name;
        this.height_str = height_str;
        this.number = number;
        this.shoots = shoots;
        this.team_name = team_name;
        this.waivers_signing_age = waivers_signing_age;
    }

    // FromThirdPartyGenerator
    public static CapFriendlyPlayer fromThirdPartyMap(final Map<String, Object> map) {

        final String capFriendlyPlayerUuid = ServiceUtil.getGuid();
        final Integer age = TypeExtract.getInteger((String) map.get("age"));
        final String birth_date_str = TypeExtract.getString((String) map.get("birth_date_str"));
        final Integer draft_rount = TypeExtract.getInteger((String) map.get("draft_rount"));
        final Integer draft_year = TypeExtract.getInteger((String) map.get("draft_year"));
        final String drafted_by = TypeExtract.getString((String) map.get("drafted_by"));
        final Integer drafted_overall = TypeExtract.getInteger((String) map.get("drafted_overall"));
        final Integer elc_signing_age = TypeExtract.getInteger((String) map.get("elc_signing_age"));
        final Long elite_prospect_id = TypeExtract.getLong((String) map.get("elite_prospect_id"));
        final String full_name = TypeExtract.getString((String) map.get("full_name"));
        final String height_str = TypeExtract.getString((String) map.get("height_str"));
        final Integer number = TypeExtract.getInteger((String) map.get("number"));
        final String shoots = TypeExtract.getString((String) map.get("shoots"));
        final String team_name = TypeExtract.getString((String) map.get("team_name"));
        final Integer waivers_signing_age = TypeExtract.getInteger((String) map.get("waivers_signing_age"));

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

    // ToStringGenerator
    @Override
    public String toString() {

        final StringBuilder bldr = new StringBuilder();

        bldr.append("CapFriendlyPlayer=[");
        bldr.append("cap_friendly_player_uuid='").append(capFriendlyPlayerUuid).append("'");
        bldr.append(",age='").append(age).append("'");
        bldr.append(",birth_date_str='").append(birth_date_str).append("'");
        bldr.append(",draft_rount='").append(draft_rount).append("'");
        bldr.append(",draft_year='").append(draft_year).append("'");
        bldr.append(",drafted_by='").append(drafted_by).append("'");
        bldr.append(",drafted_overall='").append(drafted_overall).append("'");
        bldr.append(",elc_signing_age='").append(elc_signing_age).append("'");
        bldr.append(",elite_prospect_id='").append(elite_prospect_id).append("'");
        bldr.append(",full_name='").append(full_name).append("'");
        bldr.append(",height_str='").append(height_str).append("'");
        bldr.append(",number='").append(number).append("'");
        bldr.append(",shoots='").append(shoots).append("'");
        bldr.append(",team_name='").append(team_name).append("'");
        bldr.append(",waivers_signing_age='").append(waivers_signing_age).append("'");
        bldr.append("]");

        return bldr.toString();
    }


}
