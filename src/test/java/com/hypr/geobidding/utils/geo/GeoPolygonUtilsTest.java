package com.hypr.geobidding.utils.geo;

import com.hypr.geobidding.domain.campaign.CampaignGeoPoint;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeoPolygonUtilsTest {

    private CampaignGeoPoint p(double lat, double lon) {
        CampaignGeoPoint g = new CampaignGeoPoint();
        g.setLat(lat);
        g.setLon(lon);
        return g;
    }

    @Test
    void shouldReturnFalseWhenCoordsNull() {
        assertFalse(GeoPolygonUtils.isPointInsidePolygon(0, 0, null));
    }

    @Test
    void shouldReturnFalseWhenCoordsTooSmall() {
        assertFalse(GeoPolygonUtils.isPointInsidePolygon(
                0, 0,
                List.of(p(0,0), p(1,1), p(2,2))
        ));
    }

    @Test
    void shouldReturnFalseForOpenPolygon() {
        List<CampaignGeoPoint> polygon = List.of(
                p(0,0),
                p(0,10),
                p(10,10),
                p(10,0) // não fecha
        );

        assertFalse(GeoPolygonUtils.isPointInsidePolygon(5, 5, polygon));
    }

    @Test
    void shouldReturnTrueWhenPointInsidePolygon() {
        List<CampaignGeoPoint> polygon = List.of(
                p(0,0),
                p(0,10),
                p(10,10),
                p(10,0),
                p(0,0) // fechado
        );

        assertTrue(GeoPolygonUtils.isPointInsidePolygon(5, 5, polygon));
    }

    @Test
    void shouldReturnFalseWhenPointOutsidePolygon() {
        List<CampaignGeoPoint> polygon = List.of(
                p(0,0),
                p(0,10),
                p(10,10),
                p(10,0),
                p(0,0)
        );

        assertFalse(GeoPolygonUtils.isPointInsidePolygon(15, 15, polygon));
    }
}
