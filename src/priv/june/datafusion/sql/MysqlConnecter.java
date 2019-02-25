package priv.june.datafusion.sql;

import java.sql.*;
import java.util.Date;
import priv.june.datafusion.data.GpsData;
import priv.june.datafusion.preprocess.Preprocess;
import priv.june.datafusion.util.GpsTransform;
import priv.june.datafusion.util.TimeTrans;

public class MysqlConnecter {
	/**
	 * ------------- # if you want to connect mysql, you should go to
	 * com.teamghz.configure.MysqlConnecter.java to edit information
	 * -------------- # insert/update -> int update(String sql) : "sql" is what
	 * you want to execute # return a integer, when 0 -> false; when other(n)
	 * success and this operation affect n lines -------------- # delete -> int
	 * delete(String sql) : "sql" is what you want to execute # return a
	 * integer, when 0 -> false; when other(n) success and this operation affect
	 * n lines -------------- # query -> ArrayList<Map<String, String>>
	 * select(String sql, String tableName) : "sql" is what you want to execute
	 * "tableName" is the table name which you want to operate # return a
	 * ArrayList, the elements in the ArrayList is Map<String, String> # every
	 * Map is one query result # when you need to use the data returned:
	 * ArrayList<Map<String, String>> result = mc.select("select * from User",
	 * "User"); for (Map<String, String> map : result) {
	 * System.out.println("______________________"); for(Map.Entry<String,
	 * String> entry:map.entrySet()){
	 * System.out.println(entry.getKey()+"--->"+entry.getValue()); } }
	 * --------------
	 * 
	 */
	// 声明Connection对象
	private Connection con = null;
	private boolean connected = false;
	//插入table名
//	private String aveTable="area_ave_up";
//	private String dataTable="area_data_up";

	public MysqlConnecter() {
		try {
			Class.forName(Configure.DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR AT MysqlConnecter");
			e.printStackTrace();
		}
		try {
			con = DriverManager.getConnection(Configure.URL,
					Configure.USERNAME, Configure.PASSWORD);
			con.setAutoCommit(false);
			connected = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据经纬度范围查找并处理数据插入areaData表
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @param day
	 * @return
	 */
	public int insertAreadata(double lng1, double lat1, double lng2, double lat2,String day,String dataTable) {
		GpsData gpsData1 = new GpsData();
		GpsData gpsData2 = new GpsData();
		TimeTrans tm = new TimeTrans();
		GpsTransform gpsTrans = new GpsTransform();
		double lngOrigin1 = lng1;
		double lngOrigin2 = lng2;
		double latOrigin1 = lat1;
		double latOrigin2 = lat2;
		int i = 0;
		if(lng1>lng2) {double t;t=lng1;lng1=lng2;lng2=t;}
		if(lat1>lat2) {double t;t=lat1;lat1=lat2;lat2=t;}
		if (!connected) return 0;
		else
			System.out.println("Succeeded connecting to the Database!");
		try {
			// 创建statement类对象，用来执行SQL语句！！
			Statement queryStm = con.createStatement();
			Statement insertStm = con.createStatement();
			// 要执行的SQL语句
			String sql = "select id,driverId,orderId,time,longitude,latitude from gps_201611"+day+" where (longitude between "
					+ lng1
					+ " and "
					+ lng2
					+ ") and (latitude between "
					+ lat1
					+ " and " + lat2 + ") ";
			System.out.print("serch sql:" + sql);
			// 3.ResultSet类，用来存放获取的结果集！！
			ResultSet rs = queryStm.executeQuery(sql);
			System.out.println("-----------------");
			System.out.println("执行结果如下所示:");
			System.out.println("-----------------");
			System.out.println("id" + "\t" + "week" + "\t" + "time" + "\t"
					+ "timeSlot" + "\t" + "longitude" + "\t" + "latitude");
			System.out.println("-----------------");

			String insertSql = null;
			String temp = null;
			double distance = 0;// 某辆车在timeSlot内的距离
			double timeInterval = 0;// 某辆车在timeSlot内的时间间隔
			double speed;// 某辆车在timeSlot内的平均速度
			boolean start = true;
			String tempOrder = null;
			String orderId;
			while (rs.next()) {
				//判断订单起终点是否在研究范围内。
				orderId=rs.getString("orderId");
				if(tempOrder==null){
					if(searchOrder(lng1,lat1,lng2,lat2,orderId,day))
					{
						tempOrder=orderId;
						continue;
					}
				}
				else if(tempOrder.equals(orderId)) continue;
				else if(searchOrder(lng1,lat1,lng2,lat2,orderId,day)){
					tempOrder = orderId;
					continue;
				}
				/*
				 * //获取driverId这列数据
				 * gpsData1.setDriverId(rs.getString("driverId"));//driverId值
				 * gpsData1.setOrderId(rs.getString("orderId"));//orderId值
				 */
				if (temp == null) {
					gpsData1.setLng(rs.getDouble("longitude"));// 获取经度
					gpsData1.setLat(rs.getDouble("latitude"));// 获取纬度
					gpsData1.setDateString(tm.TimeStamp2String(rs
							.getString("time")));// String格式的时间
					gpsData1.setDate(tm.TimeStamp2Date(rs.getString("time")));// Date格式时间
					gpsData1.setTimeSlot(tm.getTimeSlot(gpsData1.getDate()));// 获取当前timeSlot;
					gpsData1.setDriverId(rs.getString("driverId"));// driverId值
					gpsData1.setOrderId(rs.getString("orderId"));// orderId值
					temp = gpsData1.getDriverId();
				}
				if (!gpsData1.getDriverId().equals(rs.getString("driverId"))
						|| !gpsData1.getOrderId().equals(rs.getString("orderId"))) {
					if (!start) {
						distance = gpsTrans.getDistance(gpsData1.getLat(),
								gpsData1.getLng(), gpsData2.getLat(),
								gpsData2.getLng());
						System.out.print("distance=" + distance + "\t");
						timeInterval = tm.getTimeInterval(gpsData1.getDate(),
								gpsData2.getDate(), 6);
						System.out.print("timeInterval=" + timeInterval + "\t");

						System.out.println(gpsData1.getDriverId()+" "+gpsData2.getDriverId());
						speed = gpsTrans.getSpeed(distance, timeInterval);
						System.out.println("speed=" + speed + "\t");
						
						//判断方向
						if(gpsData1.getDate().before(gpsData2.getDate())&&
								gpsTrans.getDistance(gpsData1.getLat(),gpsData1.getLng(),latOrigin2,lngOrigin2)
								>gpsTrans.getDistance(gpsData2.getLat(),gpsData2.getLng(),latOrigin2,lngOrigin2)){
						
							//System.out.println("insert "+gpsData2.getDriverId());
							// 插入area_data表
						insertSql = "insert into "+dataTable+" (datetime,week,timeSlot,drvId,speed) values ('"
								+ gpsData1.getDateString()
								+ "',"
								+ tm.getWeek(gpsData1.getDate())
								+ ","
								+ gpsData1.getTimeSlot()
								+ ",'"
								+ gpsData1.getDriverId() + "'," + speed + ")";
						insertStm.executeUpdate(insertSql);
						i++;
						}
					}
					start = true;
					gpsData1.setLng(rs.getDouble("longitude"));// 获取经度
					gpsData1.setLat(rs.getDouble("latitude"));// 获取纬度
					gpsData1.setDateString(tm.TimeStamp2String(rs
							.getString("time")));// String格式的时间
					gpsData1.setDate(tm.TimeStamp2Date(rs.getString("time")));// Date格式时间
					gpsData1.setTimeSlot(tm.getTimeSlot(gpsData1.getDate()));// 获取当前timeSlot;
					gpsData1.setDriverId(rs.getString("driverId"));// driverId值
					gpsData1.setOrderId(rs.getString("orderId"));// orderId值
				} else if (gpsData1.getTimeSlot() != tm.getTimeSlot((rs.getString("time")))) {
					if (!start) {
						distance = gpsTrans.getDistance(gpsData1.getLat(),
								gpsData1.getLng(), gpsData2.getLat(),
								gpsData2.getLng());
						System.out.print("distance=" + distance + "\t");
						timeInterval = tm.getTimeInterval(gpsData1.getDate(),
								gpsData2.getDate(), 6);
						System.out.print("timeInterval=" + timeInterval + "\t");
						speed = gpsTrans.getSpeed(distance, timeInterval);
						System.out.println("speed=" + speed + "\t");
						
						//System.out.println(gpsData1.getDate()+" "+gpsData2.getDate());
						//System.out.println("data1 distance:"+gpsTrans.getDistance(gpsData1.getLat(),gpsData1.getLng(),30.706538,104.052880));
						//System.out.println("data2 distance:"+gpsTrans.getDistance(gpsData2.getLat(),gpsData2.getLng(),30.706538,104.052880));
						
						if(gpsData1.getDate().before(gpsData2.getDate())&&
								gpsTrans.getDistance(gpsData1.getLat(),gpsData1.getLng(),latOrigin2,lngOrigin2)
								>gpsTrans.getDistance(gpsData2.getLat(),gpsData2.getLng(),latOrigin2,lngOrigin2)){
						//System.out.println("insert "+gpsData2.getDriverId());
							// 插入area_data表
						insertSql = "insert into "+dataTable+" (datetime,week,timeSlot,drvId,speed) values ('"
								+ gpsData1.getDateString()
								+ "',"
								+ tm.getWeek(gpsData1.getDate())
								+ ","
								+ gpsData1.getTimeSlot() 
								+ ",'"
								+ gpsData1.getDriverId() + "'," + speed + ")";
						insertStm.executeUpdate(insertSql);
						i++;
						}
					}
					start = true;
					gpsData1.setLng(rs.getDouble("longitude"));// 获取经度
					gpsData1.setLat(rs.getDouble("latitude"));// 获取纬度
					gpsData1.setDateString(tm.TimeStamp2String((rs
							.getString("time"))));// String格式的时间
					gpsData1.setDate(tm.String2Date(gpsData1.getDateString()));// Date格式时间
					gpsData1.setTimeSlot(tm.getTimeSlot(gpsData1.getDate()));// 获取当前timeSlot;
					gpsData1.setDriverId(rs.getString("driverId"));// driverId值
					gpsData1.setOrderId(rs.getString("orderId"));// orderId值
				} else {
					if(gpsData2.getTimeSlot()==0){
						start = true;
					}
					else start = false;
					gpsData2.setDriverId(rs.getString("driverId"));
					gpsData2.setOrderId(rs.getString("orderId"));
					gpsData2.setLat(rs.getDouble("latitude"));
					gpsData2.setLng(rs.getDouble("longitude"));
					gpsData2.setDateString(tm.TimeStamp2String(rs
							.getString("time")));// String格式的时间
					gpsData2.setDate(tm.TimeStamp2Date(rs.getString("time")));// Date格式时间
					gpsData2.setTimeSlot(tm.getTimeSlot(gpsData1.getDate()));
				}
				// 输出结果
				/*System.out.println(i + "\t" + tm.getWeek(rs.getString("time"))
						+ "\t" + tm.TimeStamp2String(rs.getString("time"))
						+ "\t" + tm.getTimeSlot(rs.getString("time")) + "\t"
						+ rs.getDouble("longitude") + "\t"
						+ rs.getDouble("latitude"));*/
			}
			if (!start) {
				distance = gpsTrans
						.getDistance(gpsData1.getLat(), gpsData1.getLng(),
								gpsData2.getLat(), gpsData2.getLng());
				System.out.print("distance=" + distance + "\t");
				timeInterval = tm.getTimeInterval(gpsData1.getDate(),
						gpsData2.getDate(), 6);
				System.out.print("timeInterval=" + timeInterval + "\t");
				speed = gpsTrans.getSpeed(distance, timeInterval);
				System.out.println("speed=" + speed + "\t");
				
				if(gpsData1.getDate().before(gpsData2.getDate())&&
						gpsTrans.getDistance(gpsData1.getLat(),gpsData1.getLng(),latOrigin2,lngOrigin2)
						>gpsTrans.getDistance(gpsData2.getLat(),gpsData2.getLng(),latOrigin2,lngOrigin2)){
				
					// 插入area_data表
				insertSql = "insert into "+dataTable+" (datetime,week,timeSlot,drvId,speed) values ('"
						+ gpsData1.getDateString()
						+ "',"
						+ tm.getWeek(gpsData1.getDate())
						+ ","
						+ gpsData1.getTimeSlot()
						+ ",'"
						+ gpsData1.getDriverId() + "'," + speed + ")";
				insertStm.executeUpdate(insertSql);
				i++;
				}
			}

			con.commit();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}

	/**
	 * 初始化area_ave表
	 * @param day
	 * @return
	 */
	public int initArea_ave(String day,String aveTable,String dataTable) {

		if (!connected)
			return 0;
		// 创建statement类对象，用来执行SQL语句！！
		TimeTrans timeTrans = new TimeTrans();
		try {
			int i;
			int j;
			Statement initStm = con.createStatement();
			// 要执行的SQL语句
			for (j = 1; j < 289; j++) {
				double sum = 0;
				double aveSpeed;
				String datetime;
				Date d = new Date();
				int week;
				int level;
				int n;
				String searchSql = "select datetime,speed from " + dataTable
						+ " where timeSlot = " + j
						+ " and datetime like '%2016-11-" + day + "%'";
				ResultSet rs = initStm.executeQuery(searchSql);
				if (!rs.next()) {
					String dateTemp = "2016-11-" + day + " 00:00:00";
					String insertSql = "insert into "
							+ aveTable
							+ " (week,date,aveSpeed,timeSlot) values ("
							+ timeTrans.getWeek(timeTrans.String2Date(dateTemp))
							+ ",'"
							+ timeTrans.addMinute(timeTrans.String2Date(dateTemp), j) + "',"
							+ null + "," + j + ")";
					initStm.executeUpdate(insertSql);
					continue;
				}
				rs.last(); // 结果集指针指到最后一行数据
				datetime = rs.getString("datetime");
				d = timeTrans.String2Date(datetime);
				week = timeTrans.getWeek(d);
				d = timeTrans.getDate(d);
				n = rs.getRow();
				rs.beforeFirst();// 将结果集指针指回到开始位置，这样才能通过while获取rs中的数据
				while (rs.next())
					sum = sum + rs.getDouble("speed");
				aveSpeed = sum / n;
				if (aveSpeed > 35){
					level = 1;
				}
				else if (aveSpeed > 25){
					level = 2;
				}
				else if (aveSpeed > 15){
					level = 3;
				}
				else if (aveSpeed > 10){
					level = 4;
				}
				else{
					level = 5;
				}
				rs.close();
				String insertSql = "insert into " + aveTable
						+ " (week,date,aveSpeed,timeSlot,level) values ("
						+ week + ",'" + timeTrans.addMinute(d, j) + "',"
						+ aveSpeed + "," + j + "," + level + ")";
				initStm.executeUpdate(insertSql);
				con.commit();
				
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 1;
	}

	/**
	 * 插入降雨量
	 * @param day
	 */
	public void insertRrr(String day,String aveTable) {
		Statement rrrStm;
		try {
			rrrStm = con.createStatement();
			String searchSql = "select time from byhou_data where date = '2016-11-"
					+ day + "' and conditions like '%rain%'";
			ResultSet rs = rrrStm.executeQuery(searchSql);
			while (rs.next()) {
				String insertSql;
				for (int i = (rs.getInt("time") - 1) * 12; i < rs.getInt("time") * 12 + 1; i++) {
					insertSql = "update " + aveTable
							+ " set rrr=1 where timeSlot=" + i
							+ " and date like '%2016-11-" + day + "%'";
					Statement rrrinsertStm = con.createStatement();
					rrrinsertStm.executeUpdate(insertSql);
					con.commit();
				}
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 批量导入表
	 * @param tableName
	 * @return
	 */
	public int loadData(String tableName) {

		if (!connected)
			return 0;

		// 创建statement类对象，用来执行SQL语句！！
		try {
			System.out.println("start load data "+tableName);
			Statement loadDataStm = con.createStatement();
			// 要执行的SQL语句
			String sql = "load data local infile 'F:/June/didi/"
					+ tableName
					+ ".txt' into table "
					+ tableName
					+ " fields terminated by ',' (driverId,orderId,time,longitude,latitude)";
			loadDataStm.executeUpdate(sql);
			con.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} finally{
			System.out.println("success load data "+tableName);
		}

		return 1;
	}

	/**
	 * 清空表
	 * @param tableName
	 * @return
	 */
	public int truncateTable(String tableName) {

		if (!connected)
			return 0;

		// 创建statement类对象，用来执行SQL语句！！
		try {
			Statement truncateStm = con.createStatement();
			// 要执行的SQL语句
			String sql = "truncate " + tableName;
			System.out.println("start truncate "+tableName);
			truncateStm.executeUpdate(sql);
			con.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}finally{
			System.out.println("success truncate "+tableName);
		}

		return 1;
	}

	/**
	 * 查找订单开始/结束坐标是否在研究范围内，若不在返回1
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @param orderId
	 * @param day
	 * @return
	 */
	public boolean searchOrder(double lng1, double lat1, double lng2,
			double lat2, String orderId, String day) {
		boolean result = false;
		String sql = "select start_longitude,start_latitude,end_longitude,end_latitude from order_201611"
				+ day + " " + "where orderId='" + orderId + "'";
		// System.out.println(sql);
		try {
			Statement searchOrder = con.createStatement();
			ResultSet rs = searchOrder.executeQuery(sql);
			while (rs.next()) {
				if (rs.getDouble("start_longitude") > lng1
						&& rs.getDouble("start_longitude") < lng2
						&& rs.getDouble("start_latitude") > lat1
						&& rs.getDouble("start_latitude") < lat2)
					result = true;
				if (rs.getDouble("end_longitude") > lng1
						&& rs.getDouble("end_longitude") < lng2
						&& rs.getDouble("end_latitude") > lat1
						&& rs.getDouble("end_latitude") < lat2)
					result = true;
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 查找area_ave数据，返回二维数组
	 * @return
	 */
	public double[][] searchAreaAve(String aveTable) {
		double[][] result = new double[30][218];
		for (int i = 0; i < 7; i++)
			result[i][217] = 0;
		TimeTrans timeTrans = new TimeTrans();
		String sql = "select date,timeSlot,aveSpeed,rrr from " + aveTable
				+ " where timeSlot>71";
		try {
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next()) {
				int day = timeTrans.getDay(rs.getString("date"));
				int timeSlot = rs.getInt("timeSlot");
				if (timeSlot == 288 && day != 1)
					day = day - 1;
				if (timeSlot == 288 && day == 1)
					day = 30;
				if (rs.getBoolean("aveSpeed"))
					result[day - 1][timeSlot - 72] = rs.getDouble("aveSpeed");
				else
					result[day - 1][timeSlot - 72] = -1;
				if (rs.getInt("rrr") == 1 && result[day - 1][217] == 0)
					result[day - 1][217] = 1;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 218; j++) {
				System.out.print(result[i][j] + ",");
			}
			System.out.println();
		}
		return result;
	}
	
	/**
	 * 缺失值补全
	 * @param tableName
	 * @return
	 */
	public double[] replaceMissingValue(String tableName){
		double[] speedValue = new double[8640];
		try {
			Statement selectStm = con.createStatement();
			String selectSql = "select id,aveSpeed from " + tableName;
			ResultSet rs =selectStm.executeQuery(selectSql);
			while(rs.next()){
				if(rs.getObject("aveSpeed") == null){
//					System.out.println("miss value Id is "+ rs.getInt("id"));
					speedValue[rs.getInt("id")-1] = -1;
				}
				else{
					speedValue[rs.getInt("id")-1] = rs.getDouble("aveSpeed");
				}
			}
//			for(int i = 0; i < speedValue.length; i++){
//				System.out.println(speedValue[i]);
//			}
			Preprocess.fixMissingValue(speedValue);
			System.out.println("处理后：");
			for(int i = 0; i < speedValue.length; i++){
				System.out.println(speedValue[i]);
			}
			
			rs.beforeFirst();
			Statement updateStm=con.createStatement();
			while(rs.next()){
				if(rs.getObject("aveSpeed") == null){
					String updateSql = "update " + tableName
							+ " set aveSpeed=" + speedValue[rs.getInt("id")-1] + "where id=" + rs.getInt("id");
					updateStm.addBatch(updateSql);
				}
			}
			updateStm.executeBatch();
			con.commit();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return speedValue;
	}
	
	/**
	 * 转换成训练数据格式
	 */
	public void transToTrainData(String originalTableName,String newTableName){
		double[] speedValue = new double[8640];
		try {
			Statement selectStm = con.createStatement();
			String selectSql = "select id,aveSpeed from " + originalTableName;
			ResultSet rs =selectStm.executeQuery(selectSql);
			while(rs.next()){
				speedValue[rs.getInt("id")-1] = rs.getDouble("aveSpeed");
			}
			System.out.println("获取原始数据完成…");
			Statement updateStm=con.createStatement();
			for (int i = 6; i < 8640; i++) {
				String updateSql = "update " + newTableName +
						" set Vt6=" + speedValue[i-6] +
						", Vt5= " + speedValue[i-5] +
						", Vt4=" + speedValue[i-4] +
						", Vt3=" + speedValue[i-3] +
						", Vt2=" + speedValue[i-2] +
						", Vt1=" + speedValue[i-1] +
						", Vt=" + speedValue[i] +
						" where id=" + (i+1);
				updateStm.addBatch(updateSql);
			}
			updateStm.executeBatch();
			System.out.println("转换完成！");
			con.commit();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}