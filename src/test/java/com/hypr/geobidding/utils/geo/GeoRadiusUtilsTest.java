package com.hypr.geobidding.utils.geo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeoRadiusUtilsTest {

    @Test
    void shouldReturnFalseWhenOutsideBoundingBox() {
        boolean result = GeoRadiusUtils.isPointInsideRadius(
                0, 0,
                50, 50,
                10
        );
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueForSmallRadiusUsingEquirectangular() {
        boolean result = GeoRadiusUtils.isPointInsideRadius(
                -23.5505, -46.6333,   // SP
                -23.5506, -46.6334,   // muito perto
                1
        );
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForSmallRadiusOutside() {
        boolean result = GeoRadiusUtils.isPointInsideRadius(
                -23.5505, -46.6333,
                -23.5600, -46.6500,
                0.5
        );
        assertFalse(result);
    }

    @Test
    void shouldUseHaversineForLargeRadius() {
        boolean result = GeoRadiusUtils.isPointInsideRadius(
                -23.5505, -46.6333, // SP
                -22.9068, -43.1729, // RJ
                500
        );
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForLargeRadiusOutside() {
        boolean result = GeoRadiusUtils.isPointInsideRadius(
                -23.5505, -46.6333,
                40.7128, -74.0060, // NYC
                500
        );
        assertFalse(result);
    }

    @Test
    void shouldAcceptPointExactlyOnBoundary() {
        boolean result = GeoRadiusUtils.isPointInsideRadius(
                0, 0,
                0, 0,
                0
        );
        assertTrue(result);
    }
}
