package com.divisonapp.service;

import com.divisonapp.dto.event.EventDto;
import com.divisonapp.dto.participant.ParticipantDto;
import com.divisonapp.dto.participant.ParticipantRequest;
import com.divisonapp.mappers.EventMapper;
import com.divisonapp.mappers.ParticipantMapper;
import com.divisonapp.model.EventParticipant;
import com.divisonapp.model.Participant;
import com.divisonapp.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;
    private final EventMapper eventMapper;

    public List<ParticipantDto> getAllParticipants() {
        return participantRepository.findAll()
                .stream()
                .map(participantMapper::toDto)
                .toList();
    }

    public ResponseEntity<ParticipantDto> getParticipantById(Long id) {
        return participantRepository.findById(id)
                .map(participantMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ParticipantDto createParticipant(ParticipantRequest request) {
        if (participantRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("participant with this phone number already exists");
        }

        Participant participant = Participant.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .eventLinks(Collections.emptySet())
                .build();

        return participantMapper.toDto(participantRepository.save(participant));
    }


    public ResponseEntity<ParticipantDto> updateParticipant(Long id, ParticipantRequest request) {
        Optional<Participant> optional = participantRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Participant participant = optional.get();
        participant.setName(request.getName());
        participant.setPhone(request.getPhone());

        return ResponseEntity.ok(participantMapper.toDto(participantRepository.save(participant)));
    }

    public ResponseEntity<?> deleteParticipant(Long id) {
        return participantRepository.findById(id)
                .map(participant -> {
                    participantRepository.delete(participant);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<EventDto>> getParticipantEvents(Long participantId) {
        return participantRepository.findById(participantId)
                .map(participant -> {
                    var events = participant.getEventLinks()
                            .stream()
                            .map(EventParticipant::getEvent)
                            .map(eventMapper::toDto)
                            .toList();
                    return ResponseEntity.ok(events);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
