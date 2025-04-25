package com.divisonapp.repository;

import com.divisonapp.model.Event;
import com.divisonapp.model.EventParticipant;
import com.divisonapp.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {
    boolean existsByEventAndParticipant(Event event, Participant participant);
    Optional<EventParticipant> findByEventAndParticipant(Event event, Participant participant);
    List<EventParticipant> findByEvent(Event event);
    Optional<EventParticipant> findByEventIdAndParticipantId(Long eventId, Long participantId);
}