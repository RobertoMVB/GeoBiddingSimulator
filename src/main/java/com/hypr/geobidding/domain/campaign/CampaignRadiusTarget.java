package com.hypr.geobidding.domain.campaign;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CampaignRadiusTarget {

    private CampaignGeoPoint center;

    @JsonProperty("radius_km")
    private Double radiusKm;

    public CampaignGeoPoint getCenter() {
        return center;
    }

    public void setCenter(CampaignGeoPoint center) {
        this.center = center;
    }

    public Double getRadiusKm() {
        return radiusKm;
    }

    public void setRadiusKm(Double radiusKm) {
        this.radiusKm = radiusKm;
    }
}
