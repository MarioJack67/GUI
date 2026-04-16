package jacks_test_fx.parking_gui;

import java.util.ArrayList;

/**
 * @author John Gabriel Allen
 * This class represents a car object with a basic constructor, getters and a citation add method.
 */
public class Car {
	private String make, model, plate, year;
	private ArrayList<ParkingCitation> citations = new ArrayList<>();
	private User owner;
	
	//Constructor
	//=========================================================================
	/**
	 * Constructor for a car object.
	 * 
	 * @param make The make of the car.
	 * @param model The model of the car.
	 * @param plate The plate number on the car.
	 * @param year The year the car was made.
	 * @param owner The registered owner of the car.
	 */
	public Car(String make, String model, String year, String plate, User owner){
		this.make = make;
		this.model = model;
		this.year = year;
		this.plate = plate;
		this.owner = owner;
	}

	//Getters
	//=========================================================================
	/**
	 * Gets the make of the current car object.
	 * @return make of the car as a String.
	 */
	public String getMake() { return make; }
	/**
	 * Gets the model of the current car object.
	 * @return model of the car as a String.
	 */
	public String getModel() { return model; }
	/**
	 * Gets the license plate of the current car object.
	 * @return license plate of the car as a String.
	 */
	public String getPlate() { return plate; }
	/**
	 * Gets the year for the current car object.
	 * @return year of the car as a String.
	 */
	public String getYear() { return year; }
	/**
	 * Gets a list of any citations attached to the current car object.
	 * @return an ArrayList<ParkingCitation> of any existing citations for the car.
	 */
	public ArrayList<ParkingCitation> getCitations() { return citations; }
	/**
	 * Gets the registered car owner for the current car object.
	 * @return a User object for the registered car owner.
	 */
	public User getOwner() { return owner; }
	
	//Adder
	//=========================================================================
	/**
	 * Adds a ParkingCitation object to the ArrayList<> of ParkingCitations for the current car.
	 * @param citation The ParkingCitation object to attach to the current car.
	 */
	public void addCitation(ParkingCitation citation){ citations.add(citation); }
	
	@Override
	public String toString() {
		return this.make + " " + this.model + " [" + this.plate + "]";
    }
}

