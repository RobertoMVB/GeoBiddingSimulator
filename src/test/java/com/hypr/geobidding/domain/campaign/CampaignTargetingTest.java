package com.hypr.geobidding.domain.campaign;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CampaignTargetingTest {

    @Test
    void shouldReturnEmptyListWhenCoordsNull() {
        CampaignTargeting targeting = new CampaignTargeting();
        assertTrue(targeting.getCampaignCoords().isEmpty());
    }

    @Test
    void shouldConvertCoordsToGeoPoints() {
        CampaignTargeting targeting = new CampaignTargeting();
        targeting.setCoords(List.of(
                List.of(1.0, 2.0),
                List.of(3.0, 4.0)
        ));

        List<CampaignGeoPoint> points = targeting.getCampaignCoords();

        assertEquals(2, points.size());
        assertEquals(1.0, points.get(0).getLat());
        assertEquals(2.0, points.get(0).getLon());
    }

    @Test
    void shouldCacheConvertedCoords() {
        CampaignTargeting targeting = new CampaignTargeting();
        targeting.setCoords(List.of(
                List.of(1.0, 2.0)
        ));

        List<CampaignGeoPoint> first = targeting.getCampaignCoords();
        List<CampaignGeoPoint> second = targeting.getCampaignCoords();

        assertSame(first, second);
    }

    @Test
    void shouldInvalidateCacheWhenCoordsChange() {
        CampaignTargeting targeting = new CampaignTargeting();
        targeting.setCoords(List.of(
                List.of(1.0, 2.0)
        ));

        List<CampaignGeoPoint> first = targeting.getCampaignCoords();

        targeting.setCoords(List.of(
                List.of(3.0, 4.0)
        ));

        List<CampaignGeoPoint> second = targeting.getCampaignCoords();

        assertNotSame(first, second);
        assertEquals(3.0, second.get(0).getLat());
    }

    @Test
    void shouldIgnoreInvalidPoints() {
        CampaignTargeting targeting = new CampaignTargeting();

        List<List<Double>> coords = new ArrayList<>();
        coords.add(null);              // inválido
        coords.add(List.of(1.0));      // inválido
        coords.add(List.of(2.0, 3.0)); // válido

        targeting.setCoords(coords);

        List<CampaignGeoPoint> points = targeting.getCampaignCoords();

        assertEquals(1, points.size());
    }

}
