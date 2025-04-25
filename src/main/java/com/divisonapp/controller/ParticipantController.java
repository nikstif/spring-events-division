package com.divisonapp.controller;

import com.divisonapp.dto.event.EventDto;
import com.divisonapp.dto.participant.ParticipantDto;
import com.divisonapp.dto.participant.ParticipantRequest;
import com.divisonapp.service.ParticipantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @GetMapping
    public ResponseEntity<List<ParticipantDto>> getAllParticipants() {
        return ResponseEntity.ok(participantService.getAllParticipants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipantDto> getParticipantById(@PathVariable Long id) {
        return participantService.getParticipantById(id);
    }

    @PostMapping
    public ResponseEntity<ParticipantDto> createParticipant(@Valid @RequestBody ParticipantRequest request) {
        return ResponseEntity.ok(participantService.createParticipant(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParticipantDto> updateParticipant(@PathVariable Long id,
                                                            @Valid @RequestBody ParticipantRequest request) {
        return participantService.updateParticipant(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParticipant(@PathVariable Long id) {
        return participantService.deleteParticipant(id);
    }

    @GetMapping("/{participantId}/events")
    public ResponseEntity<List<EventDto>> getParticipantEvents(@PathVariable Long participantId) {
        return participantService.getParticipantEvents(participantId);
    }
}
