package com.hypr.geobidding.domain.campaign;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CampaignAreaType {
    RADIUS,
    POLYGON,
    MULTI_RADIUS;

    @JsonCreator
    public static CampaignAreaType from(String value) {
        return CampaignAreaType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }
}

