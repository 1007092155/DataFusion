package priv.june.datafusion.preprocess;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Preprocess {
	static final int N = 8640;
	/**
	 * 缺失值处理
	 * @param speed
	 */
	public static void fixMissingValue(double[] speed){
/*
		double[] speed = new double[N];
		FileInputStream fis = new FileInputStream("C:/Users/June/Desktop/area_ave(all).csv");
		BufferedReader in = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
		String line = in.readLine();
		int n = 0;
		while ((line = in.readLine())!= null) {
			String[] strArr = line.split(",");
			if(!strArr[2].isEmpty()){
				speed[n++] = Double.valueOf(strArr[2]);
			}else{
				speed[n++] = -1;
			}
		}
		in.close();
		fis.close();
		*/
		int i = 0;
		while(speed[i] == -1){
			i++;
		}
		for(; i < speed.length; i++){
			if(speed[i] == -1){
				double beforeData = 0;
				double afterData = 0;
				int beforeN = i;
				int afterN = i;
				while(speed[beforeN] == -1){
					beforeN--;
				}
				beforeData = speed[beforeN];
				
				while(speed[afterN] == -1){
					afterN++;
				}
				afterData = speed[afterN];
				speed[i] = (beforeData + afterData)/2;
			}
		}
		if(speed[0] == -1){
			repalceFirstData(speed);
		}
	}
	
	public static void repalceFirstData(double[] speed){
		if(speed[0] == -1){
			double total = 0;
			int count = 0;
			for(int i = 1; i < 30;i++){
				if(speed[i*288] != -1){
					total += speed[i*288];
					count++;
				}
			}
			if(count != 0){
				speed[0] = total/count;
				System.out.println("speed[0] = " + speed[0]);
			}
		}
		fixMissingValue(speed);
	}
}