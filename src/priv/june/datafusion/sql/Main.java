package priv.june.datafusion.sql;

public class Main {

    public static void main(String[] args) {
    	MysqlConnecter sqlCon=new MysqlConnecter();
    	int lineNum=0;
    	for(int i=2;i<31;i++)
    	{
    		String day;
    		if(i<10){ 
    			day="0"+String.valueOf(i);
    		}
    		else{
    			day=String.valueOf(i);
    		}
    		if(sqlCon.loadData("gps_201611"+day)==1){
    			
				lineNum=sqlCon.insertAreadata(104.07584,30.68507,104.07368,30.68583,day,"area_data_right");//方向：l1tol2
				System.out.println("Inserted "+lineNum+" to area_data_right.");
		    	sqlCon.initArea_ave(day,"area_ave_right","area_data_right");

				lineNum=sqlCon.insertAreadata(104.07327,30.68601,104.07329,30.68802,day,"area_data_up");//方向：l1tol2
				System.out.println("Inserted "+lineNum+" to area_data_up.");
		    	sqlCon.initArea_ave(day,"area_ave_up","area_data_up");
		    	
				lineNum=sqlCon.insertAreadata(104.07288,30.68373,104.07325,30.68576,day,"area_data_down");//方向：l1tol2
				System.out.println("Inserted "+lineNum+" to area_data_down.");
		    	sqlCon.initArea_ave(day,"area_ave_down","area_data_down");

				lineNum=sqlCon.insertAreadata(104.07058,30.68584,104.07297,30.68581,day,"area_data_left");//方向：l1tol2
				System.out.println("Inserted "+lineNum+" to area_data_left.");
		    	sqlCon.initArea_ave(day,"area_ave_left","area_data_left");
		    	
//		    	sqlCon.insertRrr(day,"area_ave_right");
		    	
		    	sqlCon.truncateTable("gps_201611"+day);
    		}
    	}
//    	sqlCon.replaceMissingValue("area_ave_up");
//    	sqlCon.transToTrainData("area_ave_up", "train_data_up");
    }
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
//}
