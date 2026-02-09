package com.hypr.geobidding.domain.campaign;

import java.util.List;

public class CampaignTargeting {
    private CampaignAreaType type;

    // centro da circunferencia
    private CampaignGeoPoint center;

    // raio da circunferencia
    private Double radiusKm;

    // Lista de centros de multiplas circunferencias
    private java.util.List<CampaignRadiusTarget> targets;

    // Lista de pontos do polígono
    private java.util.List<CampaignGeoPoint> coords;

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

    public List<CampaignGeoPoint> getCoords() {
        return coords;
    }

    public void setCoords(List<CampaignGeoPoint> coords) {
        this.coords = coords;
    }
}
