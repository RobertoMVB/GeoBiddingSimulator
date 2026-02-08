package com.hypr.geobidding.controller.bid;

import com.hypr.geobidding.dto.bid.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/bid")
public class BidController {

    @PostMapping
    public ResponseEntity<BidResponse> bid(@Valid @RequestBody BidRequest request) {

        long start = System.currentTimeMillis();

        // lógica dummy de decisão
        boolean shouldBid = request.getBidRequestInventory()
                .getFloorPrice()
                .compareTo(BigDecimal.ONE) < 0;

        BidResponse response = new BidResponse();
        response.setRequestId(request.getRequestId());

        if (shouldBid) {
            response.setDecision(BidDecision.BID);
            response.setBidPrice(new BigDecimal("0.75"));
            response.setCampaignId("camp_456");
        } else {
            response.setDecision(BidDecision.NO_BID);
        }

        response.setLatencyMs(System.currentTimeMillis() - start);

        return ResponseEntity.ok(response);
    }
}

