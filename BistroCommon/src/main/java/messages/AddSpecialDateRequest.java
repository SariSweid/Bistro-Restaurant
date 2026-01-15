package messages;

import java.io.Serializable;


import Entities.SpecialDates;

@SuppressWarnings("serial")
public class AddSpecialDateRequest implements Serializable {
	private SpecialDates specialDate;
	
	public AddSpecialDateRequest(SpecialDates specialDate) {
		this.specialDate = specialDate;
	}
	
	public SpecialDates getSpecialDate() {
		return specialDate;
	}
	
	
}
