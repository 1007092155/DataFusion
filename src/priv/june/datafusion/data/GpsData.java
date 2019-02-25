package priv.june.datafusion.data;

import java.util.Date;

public class GpsData {
	private String driverId;
	private String orderId;
	private String dateString;
	private Date date;
	private double lat;
    private double lng;
    private int timeSlot;//一天分为288个timeSlot
	/**
	 * @return the driverId
	 */
	public String getDriverId() {
		return driverId;
	}
	/**
	 * @param driverId the driverId to set
	 */
	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the dateString
	 */
	public String getDateString() {
		return dateString;
	}
	/**
	 * @param dateString the dateString to set
	 */
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
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
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}
	/**
	 * @param lat the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}
	/**
	 * @return the lng
	 */
	public double getLng() {
		return lng;
	}
	/**
	 * @param lng the lng to set
	 */
	public void setLng(double lng) {
		this.lng = lng;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GpsData [driverId=" + driverId + ", orderId=" + orderId
				+ ", dateString=" + dateString + ", date=" + date + ", lat="
				+ lat + ", lng=" + lng + ", timeSlot=" + timeSlot + "]";
	}
	
    
    
    
}
