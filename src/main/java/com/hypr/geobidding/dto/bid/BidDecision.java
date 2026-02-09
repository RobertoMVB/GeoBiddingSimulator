package com.hypr.geobidding.dto.bid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.hypr.geobidding.domain.campaign.CampaignAreaType;

public enum BidDecision {
    BID,
    NO_BID;

    @JsonCreator
    public static BidDecision from(String value) {
        return BidDecision.valueOf(value.toUpperCase());
    }
}
