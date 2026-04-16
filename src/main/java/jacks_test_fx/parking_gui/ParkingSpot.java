package jacks_test_fx.parking_gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ParkingSpot {

	//attributes
	private ParkingType type;
	private ParkingValues value;
	private int parkingID;
	private final BooleanProperty spotTaken = new SimpleBooleanProperty(false);
	@SuppressWarnings("unused")
	private int userID;
	
	/**
	 * @param type
	 * @param value
	 * @param parkingID
	 * @param spotTaken
	 * @param user
	 */
	public ParkingSpot(ParkingType type, ParkingValues value, int parkingID, int userID) {
		this.type = type;
		this.value = value;
		this.parkingID = parkingID;
		this.userID = userID;
	}
	
	/**
	 * Overload version of the constructor. Only requires type, value, and spotTaken
	 * @param type - Normal, Handicap, Reserved
	 * @param value - Premium, Standard, Cheap
	 * @param spotTaken - is there currently a car in the spot?
	 */
	public ParkingSpot(int parkingID, ParkingType type, ParkingValues value, boolean taken, int userID)
	{
		this.parkingID = parkingID;
		this.type = type;
		this.value = value;
		this.spotTaken.set(taken);
		this.userID = userID;
	}

	/**
	 * @return the parkingID
	 */
	public int getParkingID() {
		return parkingID;
	}

	/**
	 * @param parkingID the parkingID to set
	 */
	public void setParkingID(int parkingID) {
		this.parkingID = parkingID;
	}

	public boolean isTaken() {
		return spotTaken.get();
	}
	
	public void setTaken(boolean value) {
		spotTaken.set(value);
	}
	
	public BooleanProperty takenProperty() {
		return spotTaken;
	}


	/**
	 * @return the type
	 */
	public ParkingType getType() {
		return type;
	}

	/**
	 * @return the value
	 */
	public ParkingValues getValue() {
		return value;
	}

	/**
	 * @param user the user to set
	 */
	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getUserID() {
		return userID;
	}
	
}
