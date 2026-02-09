package com.hypr.geobidding.domain.campaign;

public class CampaignRadiusTarget {

    private CampaignGeoPoint center;
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
