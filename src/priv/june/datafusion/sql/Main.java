package priv.june.datafusion.sql;

public class Main {

    public static void main(String[] args) {
    	MysqlConnecter sqlCon=new MysqlConnecter();
    	int lineNum=0;
    	for(int i=1;i<2;i++)
    	{
    		String day;
    		if(i<10){ 
    			day="0"+String.valueOf(i);
    		}
    		else{
    			day=String.valueOf(i);
    		}
//    		if(sqlCon.loadData("gps_201611"+day)==1){
    			
				lineNum=sqlCon.insertAreadata(104.075849,30.685079,104.07368,30.685836,day,"area_data_right");//方向：l1tol2
				System.out.println("Inserted "+lineNum+" to area_data_right.");
		    	sqlCon.initArea_ave(day,"area_ave_right","area_ave_right");

				lineNum=sqlCon.insertAreadata(104.073279,30.686010,104.073295,30.688026,day,"area_data_up");//方向：l1tol2
				System.out.println("Inserted "+lineNum+" to area_data_up.");
		    	sqlCon.initArea_ave(day,"area_ave_up","area_ave_up");
		    	
				lineNum=sqlCon.insertAreadata(104.072885,30.683739,104.073255,30.685765,day,"area_data_down");//方向：l1tol2
				System.out.println("Inserted "+lineNum+" to area_data_down.");
		    	sqlCon.initArea_ave(day,"area_ave_down","area_ave_down");

				lineNum=sqlCon.insertAreadata(104.070582,30.685845,104.072972,30.685812,day,"area_data_left");//方向：l1tol2
				System.out.println("Inserted "+lineNum+" to area_data_left.");
		    	sqlCon.initArea_ave(day,"area_ave_left","area_ave_left");
		    	
//		    	sqlCon.insertRrr(day,"area_ave_right");
		    	
		    	sqlCon.truncateTable("gps_201611"+day);
//    		}
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
