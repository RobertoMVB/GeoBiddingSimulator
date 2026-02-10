package com.hypr.geobidding.domain.campaign;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CampaignGeoPointTest {

    @Test
    void shouldSetAndGetLatLon() {
        CampaignGeoPoint point = new CampaignGeoPoint();
        point.setLat(-23.5);
        point.setLon(-46.6);

        assertEquals(-23.5, point.getLat());
        assertEquals(-46.6, point.getLon());
    }
}
