package com.hypr.geobidding.dto.bid;

import jakarta.validation.constraints.NotBlank;

public class BidRequestUser {

    @NotBlank
    private Double lat;
    @NotBlank
    private Double lon;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

}
