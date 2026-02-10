package com.hypr.geobidding.domain.campaign;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CampaignAdFormatTest {

    @Test
    void shouldDeserializeLowercaseValue() {
        CampaignAdFormat format = CampaignAdFormat.from("banner");
        assertEquals(CampaignAdFormat.BANNER, format);
    }

    @Test
    void shouldDeserializeUppercaseValue() {
        CampaignAdFormat format = CampaignAdFormat.from("VIDEO");
        assertEquals(CampaignAdFormat.VIDEO, format);
    }

    @Test
    void shouldSerializeToLowercase() {
        assertEquals("native", CampaignAdFormat.NATIVE.toValue());
    }

    @Test
    void shouldThrowExceptionForInvalidValue() {
        assertThrows(IllegalArgumentException.class, () ->
                CampaignAdFormat.from("invalid")
        );
    }

}
