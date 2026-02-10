package com.hypr.geobidding.domain.campaign;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CampaignAreaTypeTest {

    @Test
    void shouldDeserializeIgnoringCase() {
        assertEquals(CampaignAreaType.RADIUS, CampaignAreaType.from("radius"));
        assertEquals(CampaignAreaType.POLYGON, CampaignAreaType.from("POLYGON"));
        assertEquals(CampaignAreaType.MULTI_RADIUS, CampaignAreaType.from("multi_radius"));
    }

    @Test
    void shouldSerializeToLowercase() {
        assertEquals("radius", CampaignAreaType.RADIUS.toValue());
        assertEquals("polygon", CampaignAreaType.POLYGON.toValue());
    }

    @Test
    void shouldThrowExceptionForInvalidValue() {
        assertThrows(IllegalArgumentException.class, () ->
                CampaignAreaType.from("invalid")
        );
    }
}
