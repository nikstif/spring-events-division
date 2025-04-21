package com.divisonapp.controller;

import com.divisonapp.dto.Transfer;
import com.divisonapp.model.Event;
import com.divisonapp.model.EventParticipant;
import com.divisonapp.repository.EventParticipantRepository;
import com.divisonapp.repository.EventRepository;
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

    private final EventRepository eventRepository;
    private final EventParticipantRepository eventParticipantRepository;
    private final DivisionService divisionService;

    @GetMapping("/transfers")
    public ResponseEntity<?> calculateTransfers(@RequestParam Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElse(null);

        if (event == null) {
            return ResponseEntity.badRequest().body("event not found");
        }

        List<EventParticipant> participants = eventParticipantRepository.findByEvent(event);

        if (participants.isEmpty()) {
            return ResponseEntity.badRequest().body("no participants with payments in this event");
        }

        List<Transfer> result = divisionService.calculateTransfers(participants);
        return ResponseEntity.ok(result);
    }
}
