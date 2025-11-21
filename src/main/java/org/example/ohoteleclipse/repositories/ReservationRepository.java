package org.example.ohoteleclipse.repositories;

import org.example.ohoteleclipse.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByCustomerEmail(String email);
    List<Reservation> findAllByCustomerPhone(String phone);
    boolean existsByCustomerId(Long customerId);
    boolean existsByRoomId(Long roomId);

    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId AND r.status IN ('SCHEDULED', 'IN_USE') AND r.checkin < :checkout AND r.checkout > :checkin")
    List<Reservation> findOverlappingReservations(
            @Param("roomId") Long roomId,
            @Param("checkin") LocalDateTime checkin,
            @Param("checkout") LocalDateTime checkout
    );

    List<Reservation> findByStatus(Reservation.StatusType status);
    List<Reservation> findByCheckinBetween(LocalDateTime start, LocalDateTime end);

}
