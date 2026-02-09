package com.hypr.geobidding.utils.geo;

import com.hypr.geobidding.domain.campaign.CampaignGeoPoint;

import java.util.List;

public final class GeoPolygonUtils {

    private GeoPolygonUtils() {}

    public static boolean isPointInsidePolygon(
            double userLat,
            double userLon,
            List<CampaignGeoPoint> coords
    ) {
        if (coords == null || coords.size() < 4) {
            return false;
        }

        CampaignGeoPoint first = coords.get(0);
        CampaignGeoPoint last  = coords.get(coords.size() - 1);

        if (!pointsEqual(first, last)) {
            // polígono aberto
            return false;
        }

        boolean inside = false;

        for (int i = 0, j = coords.size() - 1; i < coords.size(); j = i++) {
            double xi = coords.get(i).getLat();
            double yi = coords.get(i).getLon();
            double xj = coords.get(j).getLat();
            double yj = coords.get(j).getLon();

            boolean intersect =
                    ((yi > userLon) != (yj > userLon)) &&
                            (userLat < (xj - xi) * (userLon - yi) / (yj - yi) + xi);

            if (intersect) {
                inside = !inside;
            }
        }

        return inside;
    }

    private static boolean pointsEqual(
            CampaignGeoPoint a,
            CampaignGeoPoint b
    ) {
        return Double.compare(a.getLat(), b.getLat()) == 0 &&
                Double.compare(a.getLon(), b.getLon()) == 0;
    }
}
