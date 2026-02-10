package com.hypr.geobidding.domain.campaign;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CampaignRadiusTargetTest {

    @Test
    void shouldSetAndGetFields() {
        CampaignGeoPoint center = new CampaignGeoPoint();
        center.setLat(10.0);
        center.setLon(20.0);

        CampaignRadiusTarget target = new CampaignRadiusTarget();
        target.setCenter(center);
        target.setRadiusKm(5.5);

        assertEquals(center, target.getCenter());
        assertEquals(5.5, target.getRadiusKm());
    }
}
