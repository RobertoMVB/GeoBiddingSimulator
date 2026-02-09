package com.hypr.geobidding.domain.campaign;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CampaignExclusion {

    @JsonProperty("type")
    private CampaignAreaType type;

    @JsonProperty("center")
    private CampaignGeoPoint center;

    @JsonProperty("radius_km")
    private Double radiusKm;

    // vem do JSON como [[lat, lon], [lat, lon]]

    private List<List<Double>> coords;

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

    public List<List<Double>> getCoords() {
        return coords;
    }

    public void setCoords(List<List<Double>> coords) {
        this.coords = coords;
    }

    /**
     * Converte [[lat, lon]] para List<CampaignGeoPoint>
     */
    public List<CampaignGeoPoint> getCampaignCoords() {
        if (coords == null || coords.size() < 4) {
            return Collections.emptyList();
        }

        List<CampaignGeoPoint> points = new ArrayList<>(coords.size());

        for (List<Double> pair : coords) {
            if (pair == null || pair.size() != 2) {
                continue;
            }

            CampaignGeoPoint point = new CampaignGeoPoint();
            point.setLat(pair.get(0));
            point.setLon(pair.get(1));
            points.add(point);
        }

        return points;
    }
}
