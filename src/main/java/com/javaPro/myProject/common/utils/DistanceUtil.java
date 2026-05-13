package com.javaPro.myProject.common.utils;

/**
 * 距离计算工具类
 * 用于计算两个经纬度坐标之间的距离
 */
public class DistanceUtil {

    private static final double EARTH_RADIUS = 6371.0; // 地球半径（公里）

    /**
     * 计算两个经纬度坐标之间的距离（单位：公里）
     * 使用Haversine公式
     *
     * @param lat1 第一个点的纬度
     * @param lon1 第一个点的经度
     * @param lat2 第二个点的纬度
     * @param lon2 第二个点的经度
     * @return 距离（公里）
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // 将角度转换为弧度
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // 计算差值
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        // Haversine公式
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 计算距离
        return EARTH_RADIUS * c;
    }

    /**
     * 格式化距离显示
     *
     * @param distance 距离（公里）
     * @return 格式化后的距离字符串
     */
    public static String formatDistance(double distance) {
        if (distance < 1) {
            // 小于1公里，显示米
            return String.format("%.0f米", distance * 1000);
        } else if (distance < 10) {
            // 1-10公里，保留一位小数
            return String.format("%.1f公里", distance);
        } else {
            // 大于10公里，保留整数
            return String.format("%.0f公里", distance);
        }
    }

    /**
     * 判断两个坐标是否在指定距离范围内
     *
     * @param lat1 第一个点的纬度
     * @param lon1 第一个点的经度
     * @param lat2 第二个点的纬度
     * @param lon2 第二个点的经度
     * @param maxDistance 最大距离（公里）
     * @return 是否在范围内
     */
    public static boolean isWithinDistance(double lat1, double lon1, double lat2, double lon2, double maxDistance) {
        double distance = calculateDistance(lat1, lon1, lat2, lon2);
        return distance <= maxDistance;
    }

    /**
     * 验证经纬度是否有效
     *
     * @param latitude 纬度
     * @param longitude 经度
     * @return 是否有效
     */
    public static boolean isValidCoordinate(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            return false;
        }
        return latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180;
    }
}

