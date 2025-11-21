package org.example.ohoteleclipse.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ohoteleclipse.dtos.ReservationCreateDTO;
import org.example.ohoteleclipse.models.Reservation;
import org.example.ohoteleclipse.models.Room;
import org.example.ohoteleclipse.services.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // Endpoint pra criar uma reserva
    // POST /api/reservations
    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody ReservationCreateDTO dto) {
        Reservation newReservation = reservationService.create(dto);
        return new ResponseEntity<>(newReservation, HttpStatus.CREATED);
    }

    // Endpoint pra encontrar uma reserva por ID
    // GET /api/reservations/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> findById(@PathVariable Long id) {
        Reservation reservation = reservationService.findById(id);
        return ResponseEntity.ok(reservation);
    }

    // Endpoint pra cancelar reserva
    // PATCH /api/reservations/{id}/cancel
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancel(@PathVariable Long id) {
        Reservation canceledReservation = reservationService.cancel(id);
        return ResponseEntity.ok(canceledReservation);
    }

    // Endpoint pra finalizar/fazaer check-out de uma reserva
    // PATCH /api/reservations/{id}/finish
    @PatchMapping("/{id}/finish")
    public ResponseEntity<Reservation> finish(@PathVariable Long id) {
        Reservation finishedReservation = reservationService.finish(id);
        return ResponseEntity.ok(finishedReservation);
    }

    // Endpoint pra ver quartos ocupados
    // GET /api/reservations/occupied-rooms
    @GetMapping("/occupied-rooms")
    public ResponseEntity<List<Room>> findCurrentlyOccupiedRooms() {
        List<Room> rooms = reservationService.findCurrentlyOccupiedRooms();
        return ResponseEntity.ok(rooms);
    }

    // Endpoint pra ver reservas em determinado período de tempo
    // GET /api/reservations/by-date-range?start=2024-10-01T14:00:00&end=2024-10-10T12:00:00
    @GetMapping("/by-date-range")
    public ResponseEntity<List<Reservation>> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Reservation> reservations = reservationService.findReservationsByDateRange(start, end);
        return ResponseEntity.ok(reservations);
    }
}