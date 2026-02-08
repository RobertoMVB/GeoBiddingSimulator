package com.hypr.geobidding.dto.bid;

import jakarta.validation.constraints.NotBlank;

public class BidRequest {

    @NotBlank
    private String requestId;
    @NotBlank
    private BidRequestUser bidRequestUser;
    @NotBlank
    private BidRequestInventory bidRequestInventory;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public BidRequestUser getBidRequestUser() {
        return bidRequestUser;
    }

    public void setBidRequestUser(BidRequestUser bidRequestUser) {
        this.bidRequestUser = bidRequestUser;
    }

    public BidRequestInventory getBidRequestInventory() {
        return bidRequestInventory;
    }

    public void setBidRequestInventory(BidRequestInventory bidRequestInventory) {
        this.bidRequestInventory = bidRequestInventory;
    }
}
