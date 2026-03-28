package jacks_test_fx.parking_gui;

//import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author John Gabriel Allen
 * This class represents a parking citation object with a basic constructor and some getter methods.
 */
public class ParkingCitation {
	private double feeAmount = 15.00;
	private LocalDate dateCited, payDeadline;
	private Car carCited;
	private User customerCited;
	private String citationNotes;
	
	
	//Constructors
	//========================================================================
	/**
	 * Constructor takes a car object and derives other fields automatically.
	 * dateCited gets the current date.
	 * payDeadline gets adds 3 months to the dateCited.
	 * customerCited is taken from the cars owner field.
	 */
	public ParkingCitation(Car car) {
		carCited = car;
		dateCited = LocalDate.now();
		payDeadline = dateCited.plusMonths(3);
		customerCited = car.getOwner();
		return;
	}
	
	/**
	 * Constructor takes a car object and a String of notes and derives other fields automatically.
	 * dateCited gets the current date.
	 * payDeadline gets adds 3 months to the dateCited.
	 * customerCited is taken from the cars owner field.
	 */
	public ParkingCitation(Car car, String notes) {
		carCited = car;
		dateCited = LocalDate.now();
		payDeadline = dateCited.plusMonths(3);
		customerCited = car.getOwner();
		citationNotes = notes;
		return;
	}
	
	//Getters
	//========================================================================

	/**
	 * Gets the fee amount for the citation.
	 * @return a double representing the fee amount.
	 */
	public double getFeeAmount() { return feeAmount; }
	/**
	 * Gets the date the citation was given.
	 * @return a LocalDate value representing when the citation was given.
	 */
	public LocalDate getDateCited() { return dateCited; }
	/**
	 * Gets the payment deadline for the citation.
	 * @return a LocalDate value representing when the citation must be payed by.
	 */
	public LocalDate getPayDeadline() { return payDeadline; }
	/**
	 * Gets the car being cited in the citation.
	 * @return a Car object representing the car in the wrong.
	 */
	public Car getCarCited() { return carCited; }
	/**
	 * Gets the owner of the car being cited.
	 * @return a User object representing the person being cited.
	 */
	public User getCustomerCited() { return customerCited; }
	/**
	 * Gets the notes associated with the citation.
	 * @return a String of any notes included with the citation.
	 */
	public String getNotes() { return citationNotes; }
}
