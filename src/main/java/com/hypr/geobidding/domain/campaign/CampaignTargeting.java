package com.hypr.geobidding.domain.campaign;

import java.util.List;

public class CampaignTargeting {
    private String type;

    private CampaignGeoPoint center;
    private Double radiusKm;

    private java.util.List<CampaignRadiusTarget> targets;

    private java.util.List<java.util.List<Double>> coords;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public List<CampaignRadiusTarget> getTargets() {
        return targets;
    }

    public void setTargets(List<CampaignRadiusTarget> targets) {
        this.targets = targets;
    }

    public List<List<Double>> getCoords() {
        return coords;
    }

    public void setCoords(List<List<Double>> coords) {
        this.coords = coords;
    }
}
