package com.divisonapp.controller;

import com.divisonapp.dto.Transfer;
import com.divisonapp.model.Participant;
import com.divisonapp.repository.ParticipantRepository;
import com.divisonapp.service.DivisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/division")
@RequiredArgsConstructor
public class DivisionController {

    private final ParticipantRepository participantRepository;
    private final DivisionService divisionService;

    @GetMapping("/transfers")
    public ResponseEntity<List<Transfer>> calculateTransfers(@RequestParam Long eventId) {
        List<Participant> participants = participantRepository.findByEventId(eventId);

        if (participants.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(divisionService.calculateTransfers(participants));
    }
}
