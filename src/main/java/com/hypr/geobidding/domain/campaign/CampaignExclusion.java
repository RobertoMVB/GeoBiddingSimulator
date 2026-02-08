package com.hypr.geobidding.domain.campaign;

import java.util.List;

public class CampaignExclusion {
    private String type;

    private CampaignGeoPoint center;
    private Double radiusKm;

    private List<List<Double>> coords;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Double getRadiusKm() {
        return radiusKm;
    }

    public void setRadiusKm(Double radiusKm) {
        this.radiusKm = radiusKm;
    }

    public List<List<Double>> getCoords() {
        return coords;
    }

    public void setCoords(List<List<Double>> coords) {
        this.coords = coords;
    }

    public CampaignGeoPoint getCenter() {
        return center;
    }

    public void setCenter(CampaignGeoPoint center) {
        this.center = center;
    }
}
