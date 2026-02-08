package com.hypr.geobidding.dto.bid;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class BidResponse {

    @NotNull
    @JsonProperty("request_id")
    private String requestId;

    @NotNull
    private BidDecision decision;

    @JsonProperty("bid_price")
    private BigDecimal bidPrice; // obrigatório só se decision = BID

    @JsonProperty("campaign_id")
    private String campaignId; // obrigatório só se decision = BID

    @NotNull
    @JsonProperty("latency_ms")
    private Long latencyMs;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public BidDecision getDecision() {
        return decision;
    }

    public void setDecision(BidDecision decision) {
        this.decision = decision;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public Long getLatencyMs() {
        return latencyMs;
    }

    public void setLatencyMs(Long latencyMs) {
        this.latencyMs = latencyMs;
    }
}
