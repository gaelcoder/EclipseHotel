package org.example.ohoteleclipse.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ohoteleclipse.dtos.ReservationCreateDTO;
import org.example.ohoteleclipse.models.Customer;
import org.example.ohoteleclipse.models.Reservation;
import org.example.ohoteleclipse.models.Room;
import org.example.ohoteleclipse.repositories.ReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerService customerService;
    private final RoomService roomService;

    @Transactional
    public Reservation create(ReservationCreateDTO dto) {
        log.info("Iniciando processo de criação de reserva para o cliente id {} no quarto id {}", dto.getCustomerId(), dto.getRoomId());

        if (dto.getCheckin().isAfter(dto.getCheckout()) || dto.getCheckin().isEqual(dto.getCheckout())) {
            log.error("Data de check-out deve ser posterior à data de check-in.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data de check-out deve ser posterior à de check-in.");
        }
        if (dto.getCheckin().isBefore(LocalDateTime.now())) {
            log.error("Data de check-in não pode ser no passado.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data de check-in não pode ser uma data passada.");
        }

        Customer customer = customerService.findById(dto.getCustomerId());
        Room room = roomService.findById(dto.getRoomId());

        List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(
                dto.getRoomId(), dto.getCheckin(), dto.getCheckout());

        if (!overlappingReservations.isEmpty()) {
            log.error("Conflito de reserva. O quarto {} já está reservado no período solicitado.", room.getRoomNumber());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O quarto já está reservado para o período solicitado.");
        }

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setRoom(room);
        reservation.setCheckin(dto.getCheckin());
        reservation.setCheckout(dto.getCheckout());
        reservation.setStatus(Reservation.StatusType.SCHEDULED); // Status inicial

        Reservation savedReservation = reservationRepository.save(reservation);
        log.info("Reserva id {} criada com sucesso para o cliente {}", savedReservation.getId(), customer.getName());
        return savedReservation;
    }

    @Transactional
    public Reservation cancel(Long id) {
        log.info("Iniciando cancelamento da reserva id {}", id);
        Reservation reservation = findById(id);

        if (reservation.getStatus() != Reservation.StatusType.SCHEDULED) {
            log.error("Tentativa de cancelar reserva id {} com status inválido: {}", id, reservation.getStatus());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Só é possível cancelar reservas com status 'SCHEDULED'.");
        }

        reservation.setStatus(Reservation.StatusType.CANCELED);
        Reservation updatedReservation = reservationRepository.save(reservation);
        log.info("Reserva id {} cancelada com sucesso.", id);
        return updatedReservation;
    }

    @Transactional
    public Reservation finish(Long id) {
        log.info("Iniciando encerramento da estadia para a reserva id {}", id);
        Reservation reservation = findById(id);

        if (reservation.getStatus() != Reservation.StatusType.IN_USE) {
            log.error("Tentativa de finalizar reserva id {} com status inválido: {}", id, reservation.getStatus());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Só é possível finalizar reservas com status 'IN_USE'.");
        }

        reservation.setStatus(Reservation.StatusType.FINISHED);
        reservation.setCheckout(LocalDateTime.now());
        Reservation updatedReservation = reservationRepository.save(reservation);
        log.info("Reserva id {} finalizada com sucesso.", id);
        return updatedReservation;
    }

    public Reservation findById(Long id) {
        log.info("Buscando reserva com id: {}", id);
        return reservationRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Reserva não encontrada para o id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada.");
                });
    }

    public List<Reservation> findReservationsByDateRange(LocalDateTime start, LocalDateTime end) {
        log.info("Buscando reservas entre {} e {}", start, end);
        return reservationRepository.findByCheckinBetween(start, end);
    }

    public List<Room> findCurrentlyOccupiedRooms() {
        log.info("Buscando quartos atualmente ocupados (status IN_USE).");
        List<Reservation> activeReservations = reservationRepository.findByStatus(Reservation.StatusType.IN_USE);
        return activeReservations.stream()
                .map(Reservation::getRoom)
                .distinct()
                .collect(Collectors.toList());
    }
}
