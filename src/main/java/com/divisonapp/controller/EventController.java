package com.divisonapp.controller;

import com.divisonapp.dto.transfer.AddParticipantRequest;
import com.divisonapp.dto.event.EventDto;
import com.divisonapp.dto.event.EventParticipantInfoDto;
import com.divisonapp.dto.event.EventRequest;
import com.divisonapp.service.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @PostMapping
    public ResponseEntity<EventDto> createEvent(@Valid @RequestBody EventRequest request) {
        return ResponseEntity.ok(eventService.createEvent(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Long id,
                                                @Valid @RequestBody EventRequest request) {
        return ResponseEntity.ok(eventService.updateEvent(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        return eventService.deleteEvent(id);
    }

    @PostMapping("/{eventId}/add-participant")
    public ResponseEntity<?> addParticipantToEvent(@PathVariable Long eventId,
                                                   @RequestBody @Valid AddParticipantRequest request) {
        return eventService.addParticipantToEvent(eventId, request.getParticipantId(), request.getPayment());
    }


    @DeleteMapping("/{eventId}/remove-participant/{participantId}")
    public ResponseEntity<?> removeParticipantFromEvent(@PathVariable Long eventId,
                                                        @PathVariable Long participantId) {
        return eventService.removeParticipantFromEvent(eventId, participantId);
    }

    @GetMapping("/{eventId}/participants")
    public ResponseEntity<List<EventParticipantInfoDto>> getEventParticipants(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEventParticipantInfo(eventId));
    }


    @PutMapping("/{eventId}/update-participant-payment/{participantId}")
    public ResponseEntity<?> updateParticipantPayment(@PathVariable Long eventId,
                                                      @PathVariable Long participantId,
                                                      @RequestParam @Positive(message = "payment must be more than 0") double newPayment) {
        return eventService.updateParticipantPayment(eventId, participantId, newPayment);
    }


}
