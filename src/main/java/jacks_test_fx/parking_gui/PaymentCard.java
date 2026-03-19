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
	private int secCode;
	private int cardNum;
	private String cardName;
	private Date cardExp;

	
	//==================================
	//= GETTERS AND SETTERS
	//==================================
	public int getSecCode() {
		return secCode;
	}
	public int getCardNum() {
		return cardNum;
	}
	public String getCardName() {
		return cardName;
	}
	public Date getCardExp() {
		return cardExp;
	}

	//==================================
	//= CONSTRUCTERS
	//==================================
	/**
	 * Creates a standard Payment Card for credit purchases
	 * @param secCode The three digit Security Code
	 * @param cardNum The Card Number
	 * @param cardName The Name of the Card Owner
	 * @param cardExp The Card's Expiration date
	 */
	public PaymentCard(int secCode, int cardNum, String cardName, Date cardExp) {
		
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
		this.cardExp = cardExp;
	}
	//==================================
	//= METHODS
	//==================================
	
	
	
}
