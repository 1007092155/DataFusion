package priv.june.datafusion.util;

import java.math.BigDecimal;

public class GpsTransform {
	public static double pi = 3.1415926535897932384626;  
	public static double a = 6378245.0;  
	public static double ee = 0.00669342162296594323; 
	
	/**  
	 * 地球坐标系 (WGS84) 与火星坐标系 (GCJ-02) 的转换算法 将 GCJ-02 坐标转换成 WGS84 坐标  
	 */  
	public static Gps gcj_To_Gps84(double lat, double lon) {  
	    Gps gps = transform(lat, lon);  
	    double lontitude = lon * 2 - gps.getWgLon();  
	    double latitude = lat * 2 - gps.getWgLat();  
	    return new Gps(latitude, lontitude);  
	}  
	
	public static boolean outOfChina(double lat, double lon) {
	    if (lon < 72.004 || lon > 137.8347) return true;
	    if (lat < 0.8293 || lat > 55.8271) return true;
	    return false;
	}
	private static Gps transform(double lat, double lon) {
	    if (outOfChina(lat, lon)) return new Gps(lat, lon);
	    double dLat = transformLat(lon - 105.0, lat - 35.0);
	    double dLon = transformLon(lon - 105.0, lat - 35.0);
	    double radLat = lat / 180.0 * pi;
	    double magic = Math.sin(radLat);
	    magic = 1 - ee * magic * magic;
	    double sqrtMagic = Math.sqrt(magic);
	    dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
	    dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
	    double mgLat = lat + dLat;
	    double mgLon = lon + dLon;
	    return new Gps(mgLat, mgLon);
	}
	private static double transformLat(double x, double y) {
	    double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
	            + 0.2 * Math.sqrt(Math.abs(x));
	    ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
	    ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
	    ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
	    return ret;
	}
	private static double transformLon(double x, double y) {
	    double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
	            * Math.sqrt(Math.abs(x));
	    ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
	    ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
	    ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
	    return ret;
	}
	
	private final static double EARTH_RADIUS = 6378.137;//地球半径  
	private static double rad(double d) {  
	    return d * Math.PI / 180.0;  
	}  
	/**  
	 * 计算两点间距离  
	 * @return double 距离 单位公里,精确到米  
	 */  
	public double getDistance(double lat1, double lng1, double lat2, double lng2) {  
	    double radLat1 = rad(lat1);  
	    double radLat2 = rad(lat2);  
	    double a = radLat1 - radLat2;  
	    double b = rad(lng1) - rad(lng2);  
	    double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +  
	            Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));  
	    s = s * EARTH_RADIUS;  
	    s = new BigDecimal(s).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
	    return s;  
	}  

	public double getSpeed(double distance,double timeInterval){
		double speed;
		BigDecimal d = new BigDecimal(distance);
		BigDecimal t = new BigDecimal(timeInterval);
		
		speed = d.divide(t, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		return speed;
	}
}
