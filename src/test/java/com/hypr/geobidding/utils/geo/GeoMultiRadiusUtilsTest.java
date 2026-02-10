package com.hypr.geobidding.utils.geo;

import com.hypr.geobidding.domain.campaign.CampaignGeoPoint;
import com.hypr.geobidding.domain.campaign.CampaignRadiusTarget;
import com.hypr.geobidding.domain.campaign.CampaignTargeting;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeoMultiRadiusUtilsTest {

    private CampaignRadiusTarget radius(double lat, double lon, double km) {
        CampaignGeoPoint center = new CampaignGeoPoint();
        center.setLat(lat);
        center.setLon(lon);

        CampaignRadiusTarget r = new CampaignRadiusTarget();
        r.setCenter(center);
        r.setRadiusKm(km);
        return r;
    }

    @Test
    void shouldReturnFalseWhenTargetingNull() {
        assertFalse(GeoMultiRadiusUtils.isPointInsideAnyRadius(
                0, 0, null
        ));
    }

    @Test
    void shouldReturnFalseWhenTargetsNull() {
        CampaignTargeting t = new CampaignTargeting();
        t.setTargets(null);

        assertFalse(GeoMultiRadiusUtils.isPointInsideAnyRadius(
                0, 0, t
        ));
    }

    @Test
    void shouldReturnFalseWhenNoRadiusMatches() {
        CampaignTargeting t = new CampaignTargeting();
        t.setTargets(List.of(
                radius(50, 50, 10),
                radius(60, 60, 5)
        ));

        assertFalse(GeoMultiRadiusUtils.isPointInsideAnyRadius(
                0, 0, t
        ));
    }

    @Test
    void shouldReturnTrueWhenAnyRadiusMatches() {
        CampaignTargeting t = new CampaignTargeting();
        t.setTargets(List.of(
                radius(-23.55, -46.63, 1),
                radius(50, 50, 10)
        ));

        assertTrue(GeoMultiRadiusUtils.isPointInsideAnyRadius(
                -23.5505, -46.6333, t
        ));
    }
}
