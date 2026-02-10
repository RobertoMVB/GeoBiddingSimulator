package com.hypr.geobidding.service.campaign;

import com.hypr.geobidding.domain.campaign.*;
import com.hypr.geobidding.dto.bid.*;
import com.hypr.geobidding.repository.campaign.CampaignRepository;
import com.hypr.geobidding.utils.geo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampaignBidServiceTest {

    @Mock
    private CampaignRepository campaignRepository;

    @InjectMocks
    private CampaignBidService service;

    private BidRequest request;

    @BeforeEach
    void setup() {
        request = new BidRequest();
        request.setRequestId("req-1");

        BidRequestUser user = new BidRequestUser();
        user.setLat(-23.5);
        user.setLon(-46.6);
        request.setBidRequestUser(user);

        BidRequestInventory inventory = new BidRequestInventory();
        inventory.setFloorPrice(BigDecimal.valueOf(1.5));
        inventory.setAdFormat(CampaignAdFormat.BANNER);
        request.setBidRequestInventory(inventory);
    }

    // ------------------ NO BID ------------------

    @Test
    void shouldReturnNoBidWhenNoCampaignMatches() {
        when(campaignRepository.findAll()).thenReturn(List.of());

        BidResponse response = service.decide(request);

        assertThat(response.getDecision()).isEqualTo(BidDecision.NO_BID);
        assertThat(response.getCampaignId()).isNull();
    }

    // ------------------ BID (RADIUS) ------------------

    @Test
    void shouldBidWhenCampaignMatchesRadiusTargeting() {

        Campaign campaign = buildBaseCampaign();
        campaign.setBidPrice(BigDecimal.valueOf(2.0));
        campaign.setTargeting(radiusTargeting());

        when(campaignRepository.findAll()).thenReturn(List.of(campaign));

        try (MockedStatic<GeoRadiusUtils> geoMock = mockStatic(GeoRadiusUtils.class)) {

            geoMock.when(() ->
                    GeoRadiusUtils.isPointInsideRadius(anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble())
            ).thenReturn(true);

            BidResponse response = service.decide(request);

            assertThat(response.getDecision()).isEqualTo(BidDecision.BID);
            assertThat(response.getCampaignId()).isEqualTo("c1");
            assertThat(response.getBidPrice()).isEqualByComparingTo("2.0");
        }
    }

    // ------------------ FLOOR PRICE ------------------

    @Test
    void shouldNotBidWhenBidPriceBelowFloor() {

        Campaign campaign = buildBaseCampaign();
        campaign.setBidPrice(BigDecimal.valueOf(1.0)); // abaixo do floor

        when(campaignRepository.findAll()).thenReturn(List.of(campaign));

        BidResponse response = service.decide(request);

        assertThat(response.getDecision()).isEqualTo(BidDecision.NO_BID);
    }

    // ------------------ EXCLUSION ------------------

    @Test
    void shouldNotBidWhenUserInsideExclusionRadius() {

        Campaign campaign = buildBaseCampaign();
        campaign.setBidPrice(BigDecimal.valueOf(3.0));
        campaign.setTargeting(radiusTargeting());
        campaign.setExclusions(List.of(radiusExclusion()));

        when(campaignRepository.findAll()).thenReturn(List.of(campaign));

        try (
                MockedStatic<GeoRadiusUtils> radiusMock = mockStatic(GeoRadiusUtils.class)
        ) {

            // targeting = true
            radiusMock.when(() ->
                    GeoRadiusUtils.isPointInsideRadius(
                            anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble()
                    )
            ).thenReturn(true);

            BidResponse response = service.decide(request);

            assertThat(response.getDecision()).isEqualTo(BidDecision.NO_BID);
        }
    }

    // ------------------ HELPERS ------------------

    private Campaign buildBaseCampaign() {
        Campaign c = new Campaign();
        c.setCampaignId("c1");
        c.setActive(true);
        c.setAdFormats(List.of(CampaignAdFormat.BANNER));
        c.setBidPrice(BigDecimal.valueOf(2.0));
        return c;
    }

    private CampaignTargeting radiusTargeting() {
        CampaignGeoPoint center = new CampaignGeoPoint();
        center.setLat(-23.5);
        center.setLon(-46.6);

        CampaignTargeting t = new CampaignTargeting();
        t.setType(CampaignAreaType.RADIUS);
        t.setCenter(center);
        t.setRadiusKm(5.0);
        return t;
    }

    private CampaignExclusion radiusExclusion() {
        CampaignGeoPoint center = new CampaignGeoPoint();
        center.setLat(-23.5);
        center.setLon(-46.6);

        CampaignExclusion e = new CampaignExclusion();
        e.setType(CampaignAreaType.RADIUS);
        e.setCenter(center);
        e.setRadiusKm(1.0);
        return e;
    }
}
