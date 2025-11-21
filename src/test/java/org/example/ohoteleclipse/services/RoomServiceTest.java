package org.example.ohoteleclipse.services;

import org.example.ohoteleclipse.dtos.RoomCreateDTO;
import org.example.ohoteleclipse.dtos.RoomUpdateDTO;
import org.example.ohoteleclipse.models.Room;
import org.example.ohoteleclipse.repositories.ReservationRepository;
import org.example.ohoteleclipse.repositories.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private RoomService roomService;

    @Test
    void create_shouldCreateRoom_whenRoomNumberIsUnique() {

        RoomCreateDTO dto = new RoomCreateDTO();
        dto.setRoomNumber("101");
        dto.setType("Standard");
        dto.setPrice(150.0);

        when(roomRepository.existsByRoomNumber("101")).thenReturn(false);
        when(roomRepository.save(any(Room.class))).thenAnswer(invocation -> {
            Room room = invocation.getArgument(0);
            room.setId(1L);
            return room;
        });

        Room createdRoom = roomService.create(dto);

        assertNotNull(createdRoom);
        assertEquals(1L, createdRoom.getId());
        assertEquals("101", createdRoom.getRoomNumber());
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void create_shouldThrowException_whenRoomNumberExists() {

        RoomCreateDTO dto = new RoomCreateDTO();
        dto.setRoomNumber("101");
        when(roomRepository.existsByRoomNumber("101")).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            roomService.create(dto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Número do quarto já cadastrado.", exception.getReason());
        verify(roomRepository, never()).save(any());
    }

    @Test
    void findById_shouldReturnRoom_whenFound() {

        Room room = new Room(1L, "102", "Deluxe", 250.0);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Room foundRoom = roomService.findById(1L);

        assertNotNull(foundRoom);
        assertEquals(1L, foundRoom.getId());
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {

        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            roomService.findById(1L);
        });
    }


    @Test
    void update_shouldUpdateRoom_whenDataIsValid() {
        Long roomId = 1L;
        Room existingRoom = new Room(roomId, "103", "Standard", 120.0);
        RoomUpdateDTO updateDTO = new RoomUpdateDTO();
        updateDTO.setType("Deluxe");
        updateDTO.setPrice(220.0);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(existingRoom));
        when(roomRepository.save(any(Room.class))).thenReturn(existingRoom);

        Room updatedRoom = roomService.update(roomId, updateDTO);

        assertNotNull(updatedRoom);
        assertEquals("Deluxe", updatedRoom.getType());
        assertEquals(220.0, updatedRoom.getPrice());
        verify(roomRepository).save(existingRoom);
    }

    @Test
    void delete_shouldDeleteRoom_whenNoReservationsExist() {

        Long roomId = 1L;
        when(roomRepository.existsById(roomId)).thenReturn(true);
        when(reservationRepository.existsByRoomId(roomId)).thenReturn(false);
        doNothing().when(roomRepository).deleteById(roomId);

        assertDoesNotThrow(() -> roomService.delete(roomId));
        verify(roomRepository).deleteById(roomId);
    }

    @Test
    void delete_shouldThrowException_whenRoomHasReservations() {
        Long roomId = 1L;
        when(roomRepository.existsById(roomId)).thenReturn(true);
        when(reservationRepository.existsByRoomId(roomId)).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            roomService.delete(roomId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Não é possível deletar um quarto com reservas associadas.", exception.getReason());
        verify(roomRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_shouldThrowException_whenRoomNotFound() {

        Long roomId = 1L;
        when(roomRepository.existsById(roomId)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            roomService.delete(roomId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Quarto não encontrado.", exception.getReason());
        verify(reservationRepository, never()).existsByRoomId(anyLong());
        verify(roomRepository, never()).deleteById(anyLong());
    }
}