package com.hypr.geobidding.domain.campaign;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CampaignTest {

    @Test
    void shouldSetAndGetAllFields() {
        Campaign campaign = new Campaign();

        campaign.setCampaignId("camp_001");
        campaign.setName("Test Campaign");
        campaign.setBudgetRemaining(BigDecimal.valueOf(100));
        campaign.setBidPrice(BigDecimal.valueOf(2.5));
        campaign.setDailyBudget(BigDecimal.valueOf(50));
        campaign.setActive(true);

        campaign.setAdFormats(List.of(
                CampaignAdFormat.BANNER,
                CampaignAdFormat.VIDEO
        ));

        assertEquals("camp_001", campaign.getCampaignId());
        assertEquals("Test Campaign", campaign.getName());
        assertEquals(BigDecimal.valueOf(100), campaign.getBudgetRemaining());
        assertEquals(BigDecimal.valueOf(2.5), campaign.getBidPrice());
        assertEquals(BigDecimal.valueOf(50), campaign.getDailyBudget());
        assertTrue(campaign.isActive());
        assertEquals(2, campaign.getAdFormats().size());
    }
}
