package priv.june.datafusion.sql;

//this class used for configure your project
//the values in this class should be static and will be visited by other class
//you should eidt these values when your environment changed
public class Configure {
	//MySQL配置时的用户名
    public final static String USERNAME = "root";
    //MySQL配置时的密码
    public final static String PASSWORD = "123456";
    //数据库名
    public final static String DBNAME   = "didi_test";
    //驱动程序名
    public final static String DRIVER   = "com.mysql.jdbc.Driver";
    //URL指向要访问的数据库名
    public final static String URL      = "jdbc:mysql://115.157.200.88:3306/"+DBNAME+"?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    // must bigger than the number of the keyword in your database table 
    public final static int    TABLELEN = 10;

}
