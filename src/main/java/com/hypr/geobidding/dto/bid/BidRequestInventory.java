package com.hypr.geobidding.dto.bid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hypr.geobidding.domain.campaign.CampaignAdFormat;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class BidRequestInventory {

    @NotNull
    @JsonProperty("publisher_id")
    private String publisherId;

    @NotNull
    @JsonProperty("ad_format")
    private CampaignAdFormat adFormat;

    @NotNull
    @JsonProperty("floor_price")
    private BigDecimal floorPrice;

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public CampaignAdFormat getAdFormat() {
        return adFormat;
    }

    public void setAdFormat(CampaignAdFormat adFormat) {
        this.adFormat = adFormat;
    }

    public BigDecimal getFloorPrice() {
        return floorPrice;
    }

    public void setFloorPrice(BigDecimal floorPrice) {
        this.floorPrice = floorPrice;
    }
}
