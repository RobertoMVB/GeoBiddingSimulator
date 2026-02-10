package com.hypr.geobidding.dto.bid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public class BidRequest {

    @NotNull
    @JsonProperty("request_id")
    private String requestId;

    @NotNull
    @Valid
    @JsonProperty("user")
    private BidRequestUser bidRequestUser;

    @NotNull
    @Valid
    @JsonProperty("inventory")
    private BidRequestInventory bidRequestInventory;

    @JsonProperty("device")
    private BidRequestDevice device;

    private String timestamp;

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public BidRequestDevice getDevice() {
        return device;
    }

    public void setDevice(BidRequestDevice device) {
        this.device = device;
    }
}
