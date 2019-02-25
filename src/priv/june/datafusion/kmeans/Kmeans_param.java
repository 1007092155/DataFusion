package priv.june.datafusion.kmeans;

public class Kmeans_param {
	public static final int CENTER_ORDER = 0;
	public static final int CENTER_RANDOM = 1;
	public static final int MAX_ATTEMPTS = 4000;
	public static final double MIN_CRITERIA = 1.0;
	public static final double MIN_EuclideanDistance = 0.8;
	public double criteria = MIN_CRITERIA; // 阈值
	public int attempts = MAX_ATTEMPTS; // 尝试次数
	public int initCenterMethod = CENTER_RANDOM; // 初始化聚类中心点方式
	public boolean isDisplay = true; // 是否直接显示结果

	public double min_euclideanDistance = MIN_EuclideanDistance;
}