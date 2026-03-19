package jacks_test_fx.parking_gui;

import java.util.ArrayList;

/**
 * @author John Gabriel Allen
 * This class represents a car object with a basic constructor, getters and a citation add method.
 */
public class Car {
	private String make, model, plate;
	private ArrayList<ParkingCitation> citations = new ArrayList<>();
	private User owner;
	
	//Constructor
	public Car(String make, String model, String plate, User owner){
		make = this.make;
		model = this.model;
		plate = this.plate;
		owner = this.owner;
		return;
	}

	//Getters
	//=========================================================================
	public String getMake() { return make; }
	public String getModel() { return model; }
	public String getPlate() { return plate; }
	public ArrayList<ParkingCitation> getCitations() { return citations; }
	public User getOwner() { return owner; }
	
	//Adder
	public void addCitation(ParkingCitation citation){ citations.add(citation); }
	
}
