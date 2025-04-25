package com.divisonapp.controller;

import com.divisonapp.dto.transfer.CalculatedTransfer;
import com.divisonapp.dto.transfer.Transfer;
import com.divisonapp.dto.transfer.TransferCalculationRequest;
import com.divisonapp.model.Event;
import com.divisonapp.model.EventParticipant;
import com.divisonapp.model.TransferEntity;
import com.divisonapp.repository.EventParticipantRepository;
import com.divisonapp.repository.EventRepository;
import com.divisonapp.repository.TransferRepository;
import com.divisonapp.service.DivisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final TransferRepository transferRepository;

    @PostMapping("/transfers")
    public ResponseEntity<?> calculateAndSaveTransfers(@RequestBody TransferCalculationRequest request) {
        Long eventId = request.getEventId();

        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            return ResponseEntity.badRequest().body("event not found");
        }

        List<EventParticipant> participants = eventParticipantRepository.findByEvent(event);
        if (participants.isEmpty()) {
            return ResponseEntity.badRequest().body("no participants with payments in this event");
        }

        List<CalculatedTransfer> calculatedTransfers = divisionService.calculateTransfers(participants);

        List<TransferEntity> entities = calculatedTransfers.stream()
                .map(t -> TransferEntity.builder()
                        .eventId(eventId)
                        .fromName(t.getFrom())
                        .toName(t.getTo())
                        .amount(t.getAmount())
                        .build())
                .toList();

        transferRepository.saveAll(entities);
        return ResponseEntity.ok("transfers have been saved");
    }

    @GetMapping("/transfers")
    public ResponseEntity<List<Transfer>> getTransfers(@RequestParam Long eventId) {
        List<TransferEntity> entities = transferRepository.findAllByEventId(eventId);
        List<Transfer> result = entities.stream()
                .map(t -> new Transfer(t.getEventId(), t.getFromName(), t.getToName(), t.getAmount()))
                .toList();
        return ResponseEntity.ok(result);
    }
}
