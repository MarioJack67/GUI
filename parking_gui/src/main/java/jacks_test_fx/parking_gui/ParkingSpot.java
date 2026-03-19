package jacks_test_fx.parking_gui;

public class ParkingSpot {

	//attributes
	private ParkingType type;
	private ParkingValues value;
	private String parkingID;
	private boolean spotTaken;
	@SuppressWarnings("unused")
	private User user;
	
	/**
	 * @param type
	 * @param value
	 * @param parkingID
	 * @param spotTaken
	 * @param user
	 */
	public ParkingSpot(ParkingType type, ParkingValues value, String parkingID, boolean spotTaken, User user) {
		this.type = type;
		this.value = value;
		this.parkingID = parkingID;
		this.spotTaken = spotTaken;
		this.user = user;
	}
	
	/**
	 * Overload version of the constructor. Only requires type, value, and spotTaken
	 * @param type - Normal, Handicap, Reserved
	 * @param value - Premium, Standard, Cheap
	 * @param spotTaken - is there currently a car in the spot?
	 */
	public ParkingSpot(ParkingType type, ParkingValues value, boolean spotTaken)
	{
		this.type = type;
		this.value = value;
		this.spotTaken = spotTaken;
	}

	/**
	 * @return the parkingID
	 */
	public String getParkingID() {
		return parkingID;
	}

	/**
	 * @param parkingID the parkingID to set
	 */
	public void setParkingID(String parkingID) {
		this.parkingID = parkingID;
	}

	/**
	 * @return the spotTaken
	 */
	public boolean isSpotTaken() {
		return spotTaken;
	}

	/**
	 * @param spotTaken the spotTaken to set
	 */
	public void setSpotTaken(boolean spotTaken) {
		this.spotTaken = spotTaken;
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
	public void setUser(User user) {
		this.user = user;
	}
	
}
