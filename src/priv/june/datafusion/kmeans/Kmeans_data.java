package priv.june.datafusion.kmeans;

/**
 * 
 * @param <b>data</b> <i>in double[length][dim]</i><br/>
 *        length个instance的坐标，第i(0~length-1)个instance为data[i]
 * @param <b>length</b> <i>in</i> instance个数
 * @param <b>dim</b> <i>in</i> instance维数
 * @param <b>labels</b> <i>out int[length]</i><br/>
 *        聚类后，instance所属的聚类标号(0~k-1)
 * @param <b>centers</b> <i>in out double[k][dim]</i><br/>
 *        k个聚类中心点的坐标，第i(0~k-1)个中心点为centers[i]
 * 
 */
public class Kmeans_data {
	public double[][] data;// 原始矩阵
	public int length;// 矩阵长度
	public int dim;// 特征维度
	public int[] labels;// 数据所属类别的标签，即聚类中心的索引值
	public double[][] centers;// 聚类中心矩阵
	public int[] centerCounts;// 每个聚类中心的元素个数
	public double[][] originalCenters;// 最初的聚类中心坐标点集

	public Kmeans_data(double[][] data, int length, int dim) {
		this.data = data;
		this.length = length;
		this.dim = dim;
	}
}