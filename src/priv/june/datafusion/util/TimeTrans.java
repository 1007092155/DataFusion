package priv.june.datafusion.util;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeTrans{
	Calendar calendar=Calendar.getInstance();
	int day;
	int week;
	int hour;
	int minute;
	int second;
	int timeSlot;
 /**
 * Java将Unix时间戳转换成指定格式日期字符串
 * @param timestampString 时间戳 如："1473048265";
 * @param formats 要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
 *
 * @return 返回结果 如："2016-09-05 16:06:42";
 */
public String TimeStamp2String(String timestampString) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
    Long timestamp = Long.parseLong(timestampString) * 1000;
    String d = format.format(timestamp);
    return d;
}

public Date String2Date(String d){
	Date date = new Date();
	try{
		date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA).parse(d);
	} catch (ParseException e){
		e.printStackTrace();
	}
	return date;
}

public String Date2String(Date d){
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String dateString = formatter.format(d);
	return dateString;
}
public Date TimeStamp2Date(String timestampString){
	String d=TimeStamp2String(timestampString);
	Date date = new Date();
	date=String2Date(d);
	return date;
}

public int getWeek(Date d){
	calendar.setTime(d);
	week=calendar.get(Calendar.DAY_OF_WEEK);
	if(week==1) week=7;
	else week=week-1;
	return week;
}

public int getWeek(String timestampString){
	Date d = new Date();
	d=TimeStamp2Date(timestampString);
	calendar.setTime(d);
	week=calendar.get(Calendar.DAY_OF_WEEK);
	if(week==1) week=7;
	else week=week-1;
	return week;
}

public int getDay(String d){
	Date date = new Date();
	date=String2Date(d);
	calendar.setTime(date);
	return calendar.get(Calendar.DAY_OF_MONTH);
}

public Date getDate(Date d){
	calendar.setTime(d);
	// 将时分秒,毫秒域清零
	calendar.set(Calendar.HOUR_OF_DAY, 0);
	calendar.set(Calendar.MINUTE, 0);
	calendar.set(Calendar.SECOND, 0);
	calendar.set(Calendar.MILLISECOND, 0);
	return calendar.getTime();// cal1.getTime()返回的Date已经是更新后的对象
}

public int getTimeSlot(Date d){
	calendar.setTime(d);
	hour=calendar.get(Calendar.HOUR_OF_DAY);
	minute=calendar.get(Calendar.MINUTE);
	
	timeSlot=(hour*60+minute)/5+1;
	
	return timeSlot;
}

public int getTimeSlot(String timestampString){
	Date d=new Date();
	d=TimeStamp2Date(timestampString);
	calendar.setTime(d);
	hour=calendar.get(Calendar.HOUR_OF_DAY);
	minute=calendar.get(Calendar.MINUTE);
	
	timeSlot=(hour*60+minute)/5+1;
	
	return timeSlot;
}

public String addMinute(Date d,int timeSlot){
	d=getDate(d);
	calendar.setTime(d);
	calendar.add(Calendar.MINUTE, timeSlot*5);
	return Date2String(calendar.getTime());
	
}

public double getTimeInterval(Date d1,Date d2,int scale){
	if (scale < 0) {
        throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }

	double f=3600*1000;
	double d=d2.getTime()-d1.getTime();
	BigDecimal b1 = new BigDecimal(d);   
	BigDecimal b2 = new BigDecimal(f);  
	
	return  b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
}
}