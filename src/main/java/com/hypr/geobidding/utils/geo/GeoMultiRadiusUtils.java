package com.hypr.geobidding.utils.geo;

import com.hypr.geobidding.domain.campaign.CampaignRadiusTarget;
import com.hypr.geobidding.domain.campaign.CampaignTargeting;

public final class GeoMultiRadiusUtils {

    private GeoMultiRadiusUtils() {}

    public static boolean isPointInsideAnyRadius(
            double userLat,
            double userLon,
            CampaignTargeting campaignTargeting
    ) {
        if (campaignTargeting == null || campaignTargeting.getTargets() == null) {
            return false;
        }

        return campaignTargeting.getTargets().stream()
                .anyMatch(campaignTarget -> GeoRadiusUtils.isPointInsideRadius(
                        userLat, userLon,
                        campaignTarget.getCenter().getLat(),
                        campaignTarget.getCenter().getLon(),
                        campaignTarget.getRadiusKm()
                ));
    }
}
