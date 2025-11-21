package org.example.ohoteleclipse.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ohoteleclipse.dtos.RoomCreateDTO;
import org.example.ohoteleclipse.dtos.RoomUpdateDTO;
import org.example.ohoteleclipse.models.Room;
import org.example.ohoteleclipse.repositories.ReservationRepository;
import org.example.ohoteleclipse.repositories.RoomRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    @Cacheable("rooms")
    public List<Room> findAll() {
        log.info("Buscando todos os quartos (sem cache).");
        return roomRepository.findAll();
    }

    public List<Room> findAllByType(String type) {
        log.info("Buscando quartos pelo tipo: {}", type);
        return roomRepository.findAllByType(type);
    }

    @Cacheable(value = "room", key = "#id")
    public Room findById(Long id) {
        log.info("Buscando quarto com id: {} (sem cache).", id);
        return roomRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Quarto não encontrado para o id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Quarto não encontrado.");
                });
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "rooms", allEntries = true),
            @CacheEvict(value = "room", key = "#result.id")
    })
    public Room create(RoomCreateDTO dto) {
        log.info("Iniciando criação de novo quarto com número: {}", dto.getRoomNumber());
        if (roomRepository.existsByRoomNumber(dto.getRoomNumber())) {
            log.error("Tentativa de criar quarto com número já existente: {}", dto.getRoomNumber());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Número do quarto já cadastrado.");
        }

        Room room = new Room();
        room.setRoomNumber(dto.getRoomNumber());
        room.setType(dto.getType());
        room.setPrice(dto.getPrice());

        Room savedRoom = roomRepository.save(room);
        log.info("Quarto {} criado com sucesso com o id: {}", savedRoom.getRoomNumber(), savedRoom.getId());
        return savedRoom;
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "rooms", allEntries = true),
            @CacheEvict(value = "room", key = "#id")
    })
    public Room update(Long id, RoomUpdateDTO dto) {
        log.info("Iniciando atualização para o quarto de id: {}", id);
        Room room = findById(id);
        if (dto.getType() != null && !dto.getType().isBlank()) {
            room.setType(dto.getType());
        }

        if (dto.getPrice() != 0.0) {
            room.setPrice(dto.getPrice());
        }

        Room updatedRoom = roomRepository.save(room);
        log.info("Quarto de id {} atualizado com sucesso.", id);
        return updatedRoom;
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "rooms", allEntries = true),
            @CacheEvict(value = "room", key = "#id")
    })
    public void delete(Long id) {
        log.info("Iniciando processo de deleção para o quarto de id: {}", id);
        if (!roomRepository.existsById(id)) {
            log.error("Tentativa de deletar quarto não existente com id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quarto não encontrado.");
        }

        if (reservationRepository.existsByRoomId(id)) {
            log.error("Tentativa de deletar quarto de id {} com reservas existentes.", id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível deletar um quarto com reservas associadas.");
        }

        roomRepository.deleteById(id);
        log.info("Quarto de id {} deletado com sucesso.", id);
    }
}
