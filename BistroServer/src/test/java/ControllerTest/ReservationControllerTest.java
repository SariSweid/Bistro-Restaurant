package ControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import logicControllers.ReservationController;
import Entities.Reservation;
import enums.ReservationStatus;
import DB.DBController;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ReservationControllerTest {

    private ReservationController controller;
    private DBController mockDb;

    @BeforeEach
    void setup() {
        mockDb = mock(DBController.class);
        controller = new ReservationController();
    }

    @Test
    void testGetAllReservations() throws Exception {

        Reservation reservation = new Reservation(
                1,                     
                100,                 
                2,                     
                4422,                 
                LocalDate.now(),       
                LocalTime.of(19, 0),    
                ReservationStatus.CONFIRMED
        );

        List<Reservation> dummy = List.of(reservation);

        when(mockDb.readAllReservations()).thenReturn(dummy);

        List<Reservation> resList = controller.getAllReservations();

        assertEquals(1, resList.size());
        assertEquals(ReservationStatus.CONFIRMED, resList.get(0).getStatus());
    }
}
