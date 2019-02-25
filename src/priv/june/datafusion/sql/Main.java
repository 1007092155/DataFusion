package priv.june.datafusion.sql;

import java.io.File;

import priv.june.datafusion.kmeans.Kmeans;
import priv.june.datafusion.kmeans.Kmeans_param;
import weka.clusterers.SimpleKMeans;
import weka.core.DistanceFunction;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class Main {

    public static void main(String[] args) {
    	MysqlConnecter sqlCon=new MysqlConnecter();
//    	int lineNum=0;
//    	for(int i=1;i<31;i++)
//    	{
//    		String day;
//    		if(i<10){ 
//    			day="0"+String.valueOf(i);
//    		}
//    		else{
//    			day=String.valueOf(i);
//    		}
//    		if(sqlCon.loadData("gps_201611"+day)==1){
//    		lineNum=sqlCon.insertAreadata(103.970833,30.67518,103.97530,30.673915,day);//方向：l1tol2
//    		System.out.println("Inserted "+lineNum+" to area_data2.");
//        	sqlCon.initArea_ave(day);
//        	sqlCon.insertRrr(day);
//        	sqlCon.truncateTable("gps_201611"+day);
//    		}
    	sqlCon.replaceMissingValue("area_ave1");
    	sqlCon.transToTrainData("area_ave1", "train_data1");
    }
    	
/*    	Instances ins = null;  
        
        SimpleKMeans KM = null;  
        DistanceFunction disFun = null;  
          
        try {  
            // 读入样本数据  
            File file = new File("C:/Users/June/Desktop/area_ave(KmeansData).arff");  
            ArffLoader loader = new ArffLoader();  
            loader.setFile(file);  
            ins = loader.getDataSet();  
              
            // 初始化聚类器 （加载算法）  
            KM = new SimpleKMeans();  
            KM.setNumClusters(4);       //设置聚类要得到的类别数量  
            KM.buildClusterer(ins);     //开始进行聚类  
            System.out.println(KM.preserveInstancesOrderTipText());  
            // 打印聚类结果  
            System.out.println(KM.toString());  
    	
    	//Kmeans_param param = new Kmeans_param();
    	//double[][] kmeansData=sqlCon.searchAreaAve();
    	//Kmeans kmeans = new Kmeans(kmeansData);
    	//kmeans.doKmeans(5,param);
    	
    }catch (Exception e){
    	e.printStackTrace();
    }*/
}
