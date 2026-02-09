package com.hypr.geobidding.service.campaign;

import com.hypr.geobidding.domain.campaign.*;
import com.hypr.geobidding.dto.bid.*;
import com.hypr.geobidding.repository.campaign.CampaignRepository;
import com.hypr.geobidding.utils.geo.*;
import org.springframework.stereotype.Service;

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
        CampaignAdFormat requestedFormat = request.getBidRequestInventory().getAdFormat();

        for (Campaign campaign : campaignRepository.findAll()) {

            // 1️⃣ campanha ativa
            if (!campaign.isActive()) {
                continue;
            }

            // 2️⃣ formato compatível
            if (campaign.getAdFormats() == null ||
                    !campaign.getAdFormats().contains(requestedFormat)) {
                continue;
            }

            // 3️⃣ usuário dentro da área da campanha
            if (!matchesTargeting(userLat, userLon, campaign.getTargeting())) {
                continue;
            }

            // 4️⃣ usuário NÃO pode estar em área de exclusão
            if (isInsideAnyExclusion(userLat, userLon, campaign.getExclusions())) {
                continue;
            }

            // ✔ CAMPANHA VENCEU
            BidResponse response = new BidResponse();
            response.setRequestId(request.getRequestId());
            response.setDecision(BidDecision.BID);
            response.setBidPrice(campaign.getBidPrice());
            response.setCampaignId(campaign.getCampaignId());

            return response;
        }

        // nenhuma campanha válida
        BidResponse response = new BidResponse();
        response.setRequestId(request.getRequestId());
        response.setDecision(BidDecision.NO_BID);
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
