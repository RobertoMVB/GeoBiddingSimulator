package com.hypr.geobidding.domain.campaign;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CampaignTargeting {

    @JsonProperty("type")
    private CampaignAreaType type;

    @JsonProperty("center")
    private CampaignGeoPoint center;

    @JsonProperty("radius_km")
    private Double radiusKm;

    private List<CampaignRadiusTarget> targets;

    // JSON: [[lat, lon], [lat, lon]]
    private List<List<Double>> coords;

    // cache interno (opcional)
    private List<CampaignGeoPoint> campaignCoords;

    public CampaignAreaType getType() {
        return type;
    }

    public void setType(CampaignAreaType type) {
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
        this.campaignCoords = null; // invalida cache
    }

    /**
     * Converte [[lat, lon]] -> List<CampaignGeoPoint>
     */
    public List<CampaignGeoPoint> getCampaignCoords() {
        if (campaignCoords != null) {
            return campaignCoords;
        }

        if (coords == null || coords.isEmpty()) {
            return Collections.emptyList();
        }

        List<CampaignGeoPoint> result = new ArrayList<>(coords.size());

        for (List<Double> point : coords) {
            if (point == null || point.size() < 2) {
                continue;
            }

            CampaignGeoPoint geo = new CampaignGeoPoint();
            geo.setLat(point.get(0));
            geo.setLon(point.get(1));

            result.add(geo);
        }

        this.campaignCoords = result;
        return campaignCoords;
    }
}
