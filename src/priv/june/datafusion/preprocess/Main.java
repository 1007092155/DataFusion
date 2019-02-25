package priv.june.datafusion.preprocess;

public class Main {
	public static void main(String arg[]){
	}
	
	public double getMean(double[] arr){
		double mean = 0;
		double sum = 0;
		for(int i=0; i<arr.length; i++)
			sum=sum+arr[i];
		mean = sum/arr.length;
		return mean;
	}
	
	public double[] getSimilar(int week,int timeSlot){
		double[] arr = null;
		
		return arr;
	}
}
