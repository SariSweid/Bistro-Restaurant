package messages;

import java.io.Serializable;
import java.time.LocalDate;

public class GetWaitingListBetweenDatesRequest implements Serializable {

    private LocalDate startDate;
    private LocalDate endDate;

    public GetWaitingListBetweenDatesRequest(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
}
