package jacks_test_fx.parking_gui;

import java.sql.Date;

/**
 * Holds relevant data values pertaining to credit cards.
 * @author SpencerJPeck
 */
public class PaymentCard {
	//==================================
	//= FIELDS
	//==================================
	/**
	 * The 3 digit code on the back on the card
	 */
	private int secCode;
	/**
	 * The 16 digit number of the card
	 */
	private int cardNum;
	/**
	 * The first and last name of the card holder
	 */
	private String cardName;
	/**
	 * The date the card expires.
	 */
	private Date cardExpirationDate;

	
	//==================================
	//= GETTERS AND SETTERS
	//==================================
	/**
	 * Returns The 3 digit code on the back on the card
	 * @return secCode
	 */
	public int getSecCode() {
		return secCode;
	}
	/**
	 * Returns the 16 digit number of the card
	 * @return cardNum
	 */
	public int getCardNum() {
		return cardNum;
	}
	/**
	 * The first and last name of the card holder
	 * @return
	 */
	public String getCardName() {
		return cardName;
	}
	/**
	 * The date the card expires.
	 * @return cardExpirationDate
	 */
	public Date getCardExpirationDate() {
		return cardExpirationDate;
	}

	//==================================
	//= CONSTRUCTERS
	//==================================
	/**
	 * Creates a standard Payment Card for credit purchases
	 * @param secCode The three digit Security Code
	 * @param cardNum The Card Number
	 * @param cardName The Name of the Card Owner
	 * @param cardExpirationDate The Card's Expiration date
	 */
	public PaymentCard(int secCode, int cardNum, String cardName, Date cardExpirationDate) {
		
		//Validate Security Code
		if (secCode < 1000) { //Less than three digits
			this.secCode = secCode;
		}else {
			this.secCode = -1;
		}
		//Validate Card Number (kind of)
		String numRegex = "^[0-9]{16}$"; //16 digit code
		if (((Integer)cardNum).toString().matches(numRegex)) { //Check if passed data is 16 digits
			this.cardNum = cardNum;
		}else {// If not then set card number to an invalid //TODO: Need to  learn to throw errors.
			this.secCode = -1;
		}
		
		this.cardNum = cardNum;
		this.cardName = cardName;
		this.cardExpirationDate = cardExpirationDate;
	}
	//==================================
	//= METHODS
	//==================================
	
	
	
}
