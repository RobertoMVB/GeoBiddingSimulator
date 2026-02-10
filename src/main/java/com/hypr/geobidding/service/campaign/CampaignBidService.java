package com.hypr.geobidding.service.campaign;

import com.hypr.geobidding.domain.campaign.*;
import com.hypr.geobidding.dto.bid.*;
import com.hypr.geobidding.repository.campaign.CampaignRepository;
import com.hypr.geobidding.utils.geo.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.hypr.geobidding.domain.campaign.CampaignAreaType.*;

@Service
public class CampaignBidService {

    private final CampaignRepository campaignRepository;

    public CampaignBidService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public BidResponse decide(BidRequest request) {

        double userLat = request.getBidRequestUser().getLat();
        double userLon = request.getBidRequestUser().getLon();
        BigDecimal floorPrice = request.getBidRequestInventory().getFloorPrice();
        CampaignAdFormat requestedFormat = request.getBidRequestInventory().getAdFormat();
        Campaign winenrCampaign = null;

        winenrCampaign =
                campaignRepository.findAll()
                        .parallelStream()
                        .filter(Campaign::isActive)
                        .filter(c -> c.getAdFormats() != null &&
                                c.getAdFormats().stream()
                                        .anyMatch(f -> f.name().equalsIgnoreCase(requestedFormat.name())))
                        .filter(c -> c.getBidPrice().compareTo(floorPrice) >= 0)
                        .filter(c -> matchesTargeting(userLat, userLon, c.getTargeting()))
                        .filter(c -> !isInsideAnyExclusion(userLat, userLon, c.getExclusions()))
                        .max((a, b) -> a.getBidPrice().compareTo(b.getBidPrice()))
                        .orElse(null);

        if (winenrCampaign == null) {
            BidResponse response = new BidResponse();
            response.setRequestId(request.getRequestId());
            response.setDecision(BidDecision.NO_BID);
            return response;
        }

        BidResponse response = new BidResponse();
        response.setRequestId(request.getRequestId());
        response.setDecision(BidDecision.BID);
        response.setBidPrice(winenrCampaign.getBidPrice());
        response.setCampaignId(winenrCampaign.getCampaignId());
        return response;
    }

    // --------- helpers ---------

    private boolean matchesTargeting(
            double lat,
            double lon,
            CampaignTargeting targeting
    ) {
        if (targeting == null) return false;

        switch (targeting.getType()) {
            case RADIUS:
                return GeoRadiusUtils.isPointInsideRadius(
                        lat, lon,
                        targeting.getCenter().getLat(),
                        targeting.getCenter().getLon(),
                        targeting.getRadiusKm()
                );

            case MULTI_RADIUS:
                return GeoMultiRadiusUtils.isPointInsideAnyRadius(
                        lat, lon, targeting
                );

            case POLYGON:
                return GeoPolygonUtils.isPointInsidePolygon(
                        lat, lon, targeting.getCampaignCoords()
                );

            default:
                return false;
        }
    }

    private boolean isInsideAnyExclusion(
            double lat,
            double lon,
            List<CampaignExclusion> exclusions
    ) {
        if (exclusions == null || exclusions.isEmpty()) {
            return false;
        }

        for (CampaignExclusion exclusion : exclusions) {

            if (exclusion.getType() == null) {
                continue;
            }

//            System.out.println("Exclusion :  " + exclusion.toString());

            switch (exclusion.getType()) {

                case RADIUS:
                    if (GeoRadiusUtils.isPointInsideRadius(
                            lat, lon,
                            exclusion.getCenter().getLat(),
                            exclusion.getCenter().getLon(),
                            exclusion.getRadiusKm()
                    )) {
                        return true;
                    }
                    break;

                case POLYGON:
                    if (GeoPolygonUtils.isPointInsidePolygon(
                            lat, lon,
                            exclusion.getCampaignCoords()
                    )) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

}
