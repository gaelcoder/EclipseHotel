package org.example.ohoteleclipse.services;

import org.example.ohoteleclipse.dtos.ReservationCreateDTO;
import org.example.ohoteleclipse.models.Customer;
import org.example.ohoteleclipse.models.Reservation;
import org.example.ohoteleclipse.models.Room;
import org.example.ohoteleclipse.repositories.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void create_shouldCreateReservation_whenDataIsValidAndRoomIsAvailable() {

        ReservationCreateDTO dto = new ReservationCreateDTO();
        dto.setCustomerId(1L);
        dto.setRoomId(1L);
        dto.setCheckin(LocalDateTime.now().plusDays(1));
        dto.setCheckout(LocalDateTime.now().plusDays(3));

        Customer customer = new Customer();
        customer.setId(1L);
        Room room = new Room();
        room.setId(1L);

        when(customerService.findById(1L)).thenReturn(customer);
        when(roomService.findById(1L)).thenReturn(room);
        when(reservationRepository.findOverlappingReservations(anyLong(), any(), any())).thenReturn(Collections.emptyList());
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(inv -> {
            Reservation r = inv.getArgument(0);
            r.setId(10L);
            return r;
        });

        Reservation createdReservation = reservationService.create(dto);

        assertNotNull(createdReservation);
        assertEquals(Reservation.StatusType.SCHEDULED, createdReservation.getStatus());
        assertEquals(1L, createdReservation.getCustomer().getId());
        assertEquals(1L, createdReservation.getRoom().getId());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void create_shouldThrowException_whenRoomIsAlreadyBooked() {

        ReservationCreateDTO dto = new ReservationCreateDTO();
        dto.setCustomerId(1L);
        dto.setRoomId(1L);
        dto.setCheckin(LocalDateTime.now().plusDays(1));
        dto.setCheckout(LocalDateTime.now().plusDays(3));

        when(customerService.findById(1L)).thenReturn(new Customer());
        when(roomService.findById(1L)).thenReturn(new Room());

        // Simulando que já existe uma reserva no período
        when(reservationRepository.findOverlappingReservations(anyLong(), any(), any())).thenReturn(List.of(new Reservation()));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reservationService.create(dto);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("O quarto já está reservado para o período solicitado.", exception.getReason());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void create_shouldThrowException_whenCheckinIsInThePast() {

        ReservationCreateDTO dto = new ReservationCreateDTO();
        dto.setCheckin(LocalDateTime.now().minusDays(1)); // Data no passado
        dto.setCheckout(LocalDateTime.now().plusDays(1));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reservationService.create(dto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("A data de check-in não pode ser uma data passada.", exception.getReason());
    }

    @Test
    void cancel_shouldCancelReservation_whenStatusIsScheduled() {

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatus(Reservation.StatusType.SCHEDULED);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation canceledReservation = reservationService.cancel(1L);

        assertEquals(Reservation.StatusType.CANCELED, canceledReservation.getStatus());
        verify(reservationRepository).save(reservation);
    }

    @Test
    void cancel_shouldThrowException_whenStatusIsNotScheduled() {

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatus(Reservation.StatusType.IN_USE); // Status inválido para cancelamento

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reservationService.cancel(1L);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Só é possível cancelar reservas com status 'SCHEDULED'.", exception.getReason());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void finish_shouldFinishReservation_whenStatusIsInUse() {

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatus(Reservation.StatusType.IN_USE);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation finishedReservation = reservationService.finish(1L);

        assertEquals(Reservation.StatusType.FINISHED, finishedReservation.getStatus());
        verify(reservationRepository).save(reservation);
    }

    @Test
    void findCurrentlyOccupiedRooms_shouldReturnDistinctRooms() {
        // Arrange
        Room room1 = new Room(1L, "101", "Standard", 100.0);
        Room room2 = new Room(2L, "202", "Deluxe", 200.0);

        Reservation res1 = new Reservation();
        res1.setRoom(room1);

        Reservation res2 = new Reservation();
        res2.setRoom(room2);

        Reservation res3 = new Reservation();
        res3.setRoom(room1);

        when(reservationRepository.findByStatus(Reservation.StatusType.IN_USE)).thenReturn(List.of(res1, res2, res3));

        List<Room> occupiedRooms = reservationService.findCurrentlyOccupiedRooms();

        assertNotNull(occupiedRooms);
        assertEquals(2, occupiedRooms.size()); // Deve retornar apenas 2 quartos distintos
        assertTrue(occupiedRooms.contains(room1));
        assertTrue(occupiedRooms.contains(room2));
    }
}