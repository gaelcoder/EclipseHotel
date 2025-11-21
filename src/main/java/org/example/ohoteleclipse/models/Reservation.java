package org.example.ohoteleclipse.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "checkin", nullable = false)
    private LocalDateTime checkin;

    @Column(name = "checkout", nullable = false)
    private LocalDateTime checkout;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusType status;

    public enum StatusType {
        SCHEDULED,            // o quarto selecionado está reservado para o período de check-in escolhido
        IN_USE,      // o quarto está neste momento ocupado pela reserva realizada
        ABSENCE,             // o responsável pela reserva não compareceu ao hotel para se hospedar. (Nesse cenário, este status é considerado como situação final dareserva e não deve ser atualizado para manter histórico)
        FINISHED,          //  a reserva foi concluída com sucesso e o cliente já deixou o hotel. (Nesse cenário, este status é considerado como situação final da reserva e não deve ser atualizado para manter histórico)
        CANCELED               //  > Indica uma reserva cancelada antes de iniciar o check-in. (Nesse  cenário, este status é considerado como situação final da reserva e não deve ser atualizado para manter histórico)
    }

}
