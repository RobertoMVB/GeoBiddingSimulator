package com.hypr.geobidding.utils.geo;

public final class GeoRadiusUtils {

    private static final double EARTH_RADIUS_KM = 6371.0088;

    private GeoRadiusUtils() {}

    public static boolean isPointInsideRadius(
            double userLat,
            double userLon,
            double centerLat,
            double centerLon,
            double radiusKm
    ) {
        if (!isPointInsideBoundingBox(
                userLat, userLon, centerLat, centerLon, radiusKm)) {
            return false;
        }

        if (radiusKm < 50) {
            return isPointInsideRadiusEquirectangular(
                    userLat, userLon, centerLat, centerLon, radiusKm);
        }

        return haversine(userLat, userLon, centerLat, centerLon) <= radiusKm;
    }

    private static boolean isPointInsideRadiusEquirectangular(
            double userLat,
            double userLon,
            double centerLat,
            double centerLon,
            double radiusKm
    ) {
        double lat1 = Math.toRadians(userLat);
        double lat2 = Math.toRadians(centerLat);

        double x = Math.toRadians(userLon - centerLon)
                * Math.cos((lat1 + lat2) / 2);

        double y = Math.toRadians(userLat - centerLat);

        double distanceKm = Math.sqrt(x * x + y * y) * EARTH_RADIUS_KM;

        return distanceKm <= radiusKm;
    }

    private static double haversine(
            double lat1,
            double lon1,
            double lat2,
            double lon2
    ) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a =
                Math.pow(Math.sin(dLat / 2), 2)
                        + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2))
                        * Math.pow(Math.sin(dLon / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    public static boolean isPointInsideBoundingBox(
            double userLat,
            double userLon,
            double centerLat,
            double centerLon,
            double radiusKm
    ) {
        double deltaLat = Math.toDegrees(radiusKm / EARTH_RADIUS_KM);
        double deltaLon = Math.toDegrees(
                radiusKm / (EARTH_RADIUS_KM * Math.cos(Math.toRadians(centerLat)))
        );

        return userLat >= centerLat - deltaLat
                && userLat <= centerLat + deltaLat
                && userLon >= centerLon - deltaLon
                && userLon <= centerLon + deltaLon;
    }
}
