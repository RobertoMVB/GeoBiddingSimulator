package com.hypr.geobidding.dto.bid;

import jakarta.validation.constraints.NotBlank;

public class BidRequestInventory {

    @NotBlank
    private String publisherId;
    @NotBlank
    private String adFormat;
    @NotBlank
    private Double floorPrice;

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

    public Double getFloorPrice() {
        return floorPrice;
    }

    public void setFloorPrice(Double floorPrice) {
        this.floorPrice = floorPrice;
    }

}
