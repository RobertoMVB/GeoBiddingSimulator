package com.hypr.geobidding.controller.bid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hypr.geobidding.domain.campaign.CampaignAdFormat;
import com.hypr.geobidding.dto.bid.*;
import com.hypr.geobidding.service.campaign.CampaignBidService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BidController.class)
class BidControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CampaignBidService campaignBidService;

    private BidRequest buildRequest() {
        BidRequest request = new BidRequest();
        request.setRequestId("req-123");

        BidRequestUser user = new BidRequestUser();
        user.setLat(-23.55);
        user.setLon(-46.63);
        user.setUserId("user-1");

        BidRequestInventory inventory = new BidRequestInventory();
        inventory.setAdFormat(CampaignAdFormat.BANNER);
        inventory.setFloorPrice(BigDecimal.valueOf(0.1));
        inventory.setPublisherId("pub-1");
        inventory.setSize("320x50");

        request.setBidRequestUser(user);
        request.setBidRequestInventory(inventory);

        return request;
    }

    @Test
    void shouldReturnBidDecision() throws Exception {
        BidResponse response = new BidResponse();
        response.setRequestId("req-123");
        response.setDecision(BidDecision.BID);
        response.setBidPrice(BigDecimal.valueOf(1.23));
        response.setCampaignId("camp-1");

        Mockito.when(campaignBidService.decide(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/bid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.decision").value("bid"))
                .andExpect(jsonPath("$.campaign_id").value("camp-1"))
                .andExpect(jsonPath("$.bid_price").value(1.23))
                .andExpect(jsonPath("$.latency_ms", greaterThanOrEqualTo(0)));

        Mockito.verify(campaignBidService).decide(Mockito.any());
    }

    @Test
    void shouldReturnNoBidDecision() throws Exception {
        BidResponse response = new BidResponse();
        response.setRequestId("req-123");
        response.setDecision(BidDecision.NO_BID);

        Mockito.when(campaignBidService.decide(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/bid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.decision").value("no_bid"))
                .andExpect(jsonPath("$.latency_ms", greaterThanOrEqualTo(0)));

        Mockito.verify(campaignBidService).decide(Mockito.any());
    }
}
