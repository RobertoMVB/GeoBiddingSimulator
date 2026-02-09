package com.hypr.geobidding.dto.bid;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class BidRequestInventory {

    @NotNull
    @JsonProperty("publisher_id")
    private String publisherId;

    @NotNull
    @JsonProperty("ad_format")
    private String adFormat;

    @NotNull
    @JsonProperty("floor_price")
    private BigDecimal floorPrice;

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getAdFormat() {
        return adFormat;
    }

    public void setAdFormat(String adFormat) {
        this.adFormat = adFormat;
    }

    public BigDecimal getFloorPrice() {
        return floorPrice;
    }

    public void setFloorPrice(BigDecimal floorPrice) {
        this.floorPrice = floorPrice;
    }
}
