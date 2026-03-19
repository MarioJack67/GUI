package jacks_test_fx.parking_gui;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author John Gabriel Allen
 * This class represents a parking citation object with a basic constructor and some getter methods.
 */
public class ParkingCitation {
	private BigDecimal feeAmnt;
	private LocalDate dateCited, payDeadline;
	private Car carCited;
	private User customerCited;
	
	
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
	
	//Getters
	//=========================================================================
	public BigDecimal getFeeAmnt() { return feeAmnt; }
	public LocalDate getDateCited() { return dateCited; }
	public LocalDate getPayDeadline() { return payDeadline; }
	public Car getCarCited() { return carCited; }
	public User getCustomerCited() { return customerCited; }
}
