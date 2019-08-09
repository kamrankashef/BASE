package com.sports.model;

import common.ServiceUtil;
import common.TypeExtract;
import java.util.Date;
import java.util.Map;

final public class CapFriendlyContract {

    public final String capFriendlyContractUuid;
    public final String capFriendlyPlayerGuid;
    public final Double ch;
    public final String expiry_status;
    public final String length_str;
    public final Date signing_date;
    public final String signing_team;
    public final String type;
    public final Double value;

    // ConstructorGenerator
    public CapFriendlyContract(
            final String capFriendlyContractUuid,
            final String capFriendlyPlayerGuid,
            final Double ch,
            final String expiry_status,
            final String length_str,
            final Date signing_date,
            final String signing_team,
            final String type,
            final Double value) {
        this.capFriendlyContractUuid = capFriendlyContractUuid;
        this.capFriendlyPlayerGuid = capFriendlyPlayerGuid;
        this.ch = ch;
        this.expiry_status = expiry_status;
        this.length_str = length_str;
        this.signing_date = signing_date;
        this.signing_team = signing_team;
        this.type = type;
        this.value = value;
    }

    // FromThirdPartyGenerator
    public static CapFriendlyContract fromThirdPartyMap(final Map<String, Object> map,
            final String capFriendlyPlayerGuid) {

        final String capFriendlyContractUuid = ServiceUtil.getGuid();
        final Double ch = TypeExtract.getDouble((String) map.get("ch"));
        final String expiry_status = TypeExtract.getString((String) map.get("expiry_status"));
        final String length_str = TypeExtract.getString((String) map.get("length_str"));
        final Date signing_date = TypeExtract.getDate((String) map.get("signing_date"));
        final String signing_team = TypeExtract.getString((String) map.get("signing_team"));
        final String type = TypeExtract.getString((String) map.get("type"));
        final Double value = TypeExtract.getDouble((String) map.get("value"));

        return new CapFriendlyContract(
            capFriendlyContractUuid,
            capFriendlyPlayerGuid,
            ch,
            expiry_status,
            length_str,
            signing_date,
            signing_team,
            type,
            value);
    }

    // ToStringGenerator
    @Override
    public String toString() {

        final StringBuilder bldr = new StringBuilder();

        bldr.append("CapFriendlyContract=[");
        bldr.append("cap_friendly_contract_uuid='").append(capFriendlyContractUuid).append("'");
        bldr.append(",cap_friendly_player_guid='").append(capFriendlyPlayerGuid).append("'");
        bldr.append(",ch='").append(ch).append("'");
        bldr.append(",expiry_status='").append(expiry_status).append("'");
        bldr.append(",length_str='").append(length_str).append("'");
        bldr.append(",signing_date='").append(signing_date).append("'");
        bldr.append(",signing_team='").append(signing_team).append("'");
        bldr.append(",type='").append(type).append("'");
        bldr.append(",value='").append(value).append("'");
        bldr.append("]");

        return bldr.toString();
    }


}
