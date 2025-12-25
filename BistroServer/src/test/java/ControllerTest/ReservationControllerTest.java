package ControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import logicControllers.ReservationController;
import Entities.Reservation;
import Entities.Reservation.Status;
import DB.DBController;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ReservationControllerTest {

    private ReservationController controller;
    private DBController mockDb;

    @BeforeEach
    void setup() {
        mockDb = mock(DBController.class); // create a mock
        controller = new ReservationController(mockDb); // inject mock
    }

    @Test
    void testGetAllReservations() throws Exception {
        LocalDate localDate = LocalDate.now();
        Date sqlDate = Date.valueOf(localDate); // convert LocalDate -> java.sql.Date

        List<Reservation> dummy = List.of(
            new Reservation(1, sqlDate, 2, 4422, Status.CONFIRMED, 1)
        );

        when(mockDb.readAllReservations()).thenReturn(dummy);

        List<Reservation> resList = controller.getAllReservations();
        assertEquals(1, resList.size());
    }
}
