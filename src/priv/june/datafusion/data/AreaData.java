package priv.june.datafusion.data;

import java.util.Date;

public class AreaData {
	//尽量将属性定义为私有的，写出对应的setXXX和getXXX的方法
	private Date date;
	private int timeSlot;
	private int drvId;
	private double speed;
	private double rrr;
	
	public AreaData(){}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the timeSlot
	 */
	public int getTimeSlot() {
		return timeSlot;
	}

	/**
	 * @param timeSlot the timeSlot to set
	 */
	public void setTimeSlot(int timeSlot) {
		this.timeSlot = timeSlot;
	}

	/**
	 * @return the drvId
	 */
	public int getDrvId() {
		return drvId;
	}

	/**
	 * @param drvId the drvId to set
	 */
	public void setDrvId(int drvId) {
		this.drvId = drvId;
	}

	/**
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * @return the rrr
	 */
	public double getRrr() {
		return rrr;
	}

	/**
	 * @param rrr the rrr to set
	 */
	public void setRrr(double rrr) {
		this.rrr = rrr;
	}
	
	

}
