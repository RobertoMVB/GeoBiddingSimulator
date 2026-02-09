package com.hypr.geobidding.domain.campaign;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CampaignAdFormat {
    NATIVE,
    VIDEO,
    BANNER,
    INTERSTITIAL;

    @JsonCreator
    public static CampaignAdFormat from(String value) {
        return CampaignAdFormat.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }
}
