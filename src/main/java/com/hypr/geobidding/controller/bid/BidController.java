package com.hypr.geobidding.controller.bid;

import com.hypr.geobidding.dto.bid.*;
import com.hypr.geobidding.service.campaign.CampaignBidService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/bid")
public class BidController {

    private final CampaignBidService campaignBidService;

    public BidController(CampaignBidService campaignBidService) {
        this.campaignBidService = campaignBidService;
    }

    @PostMapping
    public ResponseEntity<BidResponse> bid(
            @Valid @RequestBody BidRequest request
    ) {
        long start = System.currentTimeMillis();

        BidResponse response = campaignBidService.decide(request);

        response.setLatencyMs(System.currentTimeMillis() - start);

        return ResponseEntity.ok(response);
    }
}

