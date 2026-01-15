package messages;

import java.io.Serializable;
import java.time.LocalDate;

@SuppressWarnings("serial")
public class DeleteSpecialDateRequest implements Serializable {
	private LocalDate date;
	
	public DeleteSpecialDateRequest(LocalDate date) {
		this.date=date;
	}
	
	public LocalDate getDate() {
		return date;
	}
}
