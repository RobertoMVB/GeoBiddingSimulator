package com.hypr.geobidding.domain.campaign;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CampaignExclusionTest {

    @Test
    void shouldReturnEmptyListWhenCoordsNull() {
        CampaignExclusion exclusion = new CampaignExclusion();
        assertTrue(exclusion.getCampaignCoords().isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenCoordsTooSmall() {
        CampaignExclusion exclusion = new CampaignExclusion();
        exclusion.setCoords(List.of(
                List.of(1.0, 2.0),
                List.of(3.0, 4.0),
                List.of(5.0, 6.0)
        ));

        assertTrue(exclusion.getCampaignCoords().isEmpty());
    }

    @Test
    void shouldConvertCoordsToGeoPoints() {
        CampaignExclusion exclusion = new CampaignExclusion();
        exclusion.setCoords(List.of(
                List.of(1.0, 2.0),
                List.of(3.0, 4.0),
                List.of(5.0, 6.0),
                List.of(1.0, 2.0)
        ));

        List<CampaignGeoPoint> points = exclusion.getCampaignCoords();

        assertEquals(4, points.size());
        assertEquals(1.0, points.get(0).getLat());
        assertEquals(2.0, points.get(0).getLon());
    }

    @Test
    void shouldIgnoreInvalidPairs() {
        CampaignExclusion exclusion = new CampaignExclusion();

        List<List<Double>> coords = new ArrayList<>();
        coords.add(null);                 // inválido
        coords.add(List.of(1.0));         // inválido
        coords.add(List.of(2.0, 3.0));    // válido
        coords.add(List.of(4.0, 5.0));    // válido

        exclusion.setCoords(coords);

        List<CampaignGeoPoint> points = exclusion.getCampaignCoords();

        assertEquals(2, points.size());
    }

}
