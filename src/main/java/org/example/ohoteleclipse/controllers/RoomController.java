package org.example.ohoteleclipse.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ohoteleclipse.dtos.RoomCreateDTO;
import org.example.ohoteleclipse.dtos.RoomUpdateDTO;
import org.example.ohoteleclipse.models.Room;
import org.example.ohoteleclipse.services.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    // POST /api/rooms
    @PostMapping
    public ResponseEntity<Room> create(@RequestBody RoomCreateDTO dto) {
        Room newRoom = roomService.create(dto);
        return new ResponseEntity<>(newRoom, HttpStatus.CREATED);
    }

    // GET /api/rooms ou
    // GET /api/rooms?type=inserir_tipo
    @GetMapping
    public ResponseEntity<List<Room>> findAll(@RequestParam(required = false) String type) {
        List<Room> rooms;
        if (type != null && !type.isBlank()) {
            rooms = roomService.findAllByType(type);
        } else {
            rooms = roomService.findAll();
        }
        return ResponseEntity.ok(rooms);
    }

    // GET /api/rooms/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable Long id) {
        Room room = roomService.findById(id);
        return ResponseEntity.ok(room);
    }

    // PUT /api/rooms/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Room> update(@PathVariable Long id, @RequestBody RoomUpdateDTO dto) {
        Room updatedRoom = roomService.update(id, dto);
        return ResponseEntity.ok(updatedRoom);
    }

    // DELETE /api/rooms/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
