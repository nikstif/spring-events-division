package com.divisonapp.service;

import com.divisonapp.dto.event.EventDto;
import com.divisonapp.dto.event.EventParticipantInfoDto;
import com.divisonapp.dto.event.EventRequest;
import com.divisonapp.mappers.EventMapper;
import com.divisonapp.model.Event;
import com.divisonapp.model.EventParticipant;
import com.divisonapp.model.Participant;
import com.divisonapp.repository.EventParticipantRepository;
import com.divisonapp.repository.EventRepository;
import com.divisonapp.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final EventParticipantRepository eventParticipantRepository;
    private final EventMapper eventMapper;

    public List<EventDto> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(eventMapper::toDto)
                .toList();
    }

    public ResponseEntity<EventDto> getEventById(Long id) {
        return eventRepository.findById(id)
                .map(eventMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public EventDto createEvent(EventRequest request) {
        if (eventRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("event with this name already exists");
        }

        Event event = Event.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return eventMapper.toDto(eventRepository.save(event));
    }

    public EventDto updateEvent(Long id, EventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("event not found"));

        event.setName(request.getName());
        event.setDescription(request.getDescription());
        return eventMapper.toDto(eventRepository.save(event));
    }

    public ResponseEntity<?> deleteEvent(Long id) {
        return eventRepository.findById(id)
                .map(event -> {
                    eventRepository.delete(event);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> addParticipantToEvent(Long eventId, Long participantId, double payment) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        Optional<Participant> participantOpt = participantRepository.findById(participantId);

        if (eventOpt.isEmpty() || participantOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = eventOpt.get();
        Participant participant = participantOpt.get();

        boolean alreadyAdded = eventParticipantRepository.existsByEventAndParticipant(event, participant);
        if (alreadyAdded) {
            return ResponseEntity.badRequest().body("participant has already been added to event");
        }

        EventParticipant link = EventParticipant.builder()
                .event(event)
                .participant(participant)
                .payment(payment)
                .build();

        eventParticipantRepository.save(link);
        return ResponseEntity.ok("participant has been successfully added to event");
    }

    public ResponseEntity<?> removeParticipantFromEvent(Long eventId, Long participantId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        Optional<Participant> participantOpt = participantRepository.findById(participantId);

        if (eventOpt.isEmpty() || participantOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = eventOpt.get();
        Participant participant = participantOpt.get();

        Optional<EventParticipant> linkOpt = eventParticipantRepository
                .findByEventAndParticipant(event, participant);

        if (linkOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("participant was not found in the event");
        }

        eventParticipantRepository.delete(linkOpt.get());
        return ResponseEntity.ok("participant has been successfully removed from event");
    }

    public List<EventParticipantInfoDto> getEventParticipantInfo(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("event not found"));

        return event.getParticipantLinks()
                .stream()
                .map(link -> EventParticipantInfoDto.builder()
                        .id(link.getParticipant().getId())
                        .name(link.getParticipant().getName())
                        .phone(link.getParticipant().getPhone())
                        .payment(link.getPayment())
                        .build())
                .toList();
    }

    public ResponseEntity<?> updateParticipantPayment(Long eventId, Long participantId, double newPayment) {
        Optional<EventParticipant> linkOpt = eventParticipantRepository.findByEventIdAndParticipantId(eventId, participantId);

        if (linkOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("participant was not found in this event");
        }

        EventParticipant link = linkOpt.get();
        link.setPayment(newPayment);
        eventParticipantRepository.save(link);

        return ResponseEntity.ok("amount has been updated successfully");
    }


}
