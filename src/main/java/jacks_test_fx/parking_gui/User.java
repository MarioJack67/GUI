package jacks_test_fx.parking_gui;

import java.util.Arrays;

/**
 * This class represents a generic user for the ParkingSystem program.
 * The notable parameter is accessLevel which governs the authority level of the user.
 * With 0 being no special access
 * @author SpencerJPeck
 */
public class User {
	//==================================
	//= FIELDS
	//==================================

	/**
	 * Tracks the User's unique ID
	 * (Currently Unused)
	 */
	private String userID;
	/**
	 * The mailing address of the User
	 */
	private String address;
	/**
	 * The First Name of the User
	 */
	private String fname;
	/**
	 * The Last Name of the User
	 */
	private String lname;
	/**
	 * The Preferred Phone Number of the User
	 */
	private String phoneNum;
	/**
	 * The List of Cars registered to the User
	 */
	private Car[] cars;
	/**
	 * The level of access of the User
	 * With 0 being a standard paying customer
	 */
	private int accessLevel;
	
	
	//==================================
	//= GETTERS AND SETTERS
	//==================================
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		//Validate that data is in correct format
		String regexStr = "^(1\\-)?[0-9]{3}\\-?[0-9]{3}\\-?[0-9]{4}$";
		if (phoneNum.matches(regexStr)) {
			this.phoneNum = phoneNum;
		}//TODO: Need to  learn to throw errors.
		
	}
	public int getAccessLevel() {
		return accessLevel;
	}
	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}
	public String getUserID() {
		return userID;
	}
	public String getFname() {
		return fname;
	}
	/**
	 * Adds a new car to the cars array
	 * @param car is the new car to add to the User's Registered Cars
	 */
	public void addCar(Car car) {
		Car[] tempArry = new Car[cars.length + 1]; // Create new array with room for new car
		System.arraycopy(cars, 0, tempArry, 0, cars.length); //Copy old data
		tempArry[cars.length] = car; //Add in new Car
		cars = tempArry; //Copy new array to field
	}
	public Car[] getCars() {
		return Arrays.copyOf(cars, cars.length);
	}

	//==================================
	//= CONSTRUCTERS
	//==================================

	public User(String address, String fname, String lname, String phoneNum, int accessLevel) {
		this.address = address;
		this.fname = fname;
		this.lname = lname;
		setPhoneNum(phoneNum);
		this.accessLevel = accessLevel;
	}

	//==================================
	//= METHODS
	//==================================
	
}
