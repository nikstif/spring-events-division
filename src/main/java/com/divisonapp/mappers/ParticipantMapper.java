package com.divisonapp.mappers;

import com.divisonapp.dto.participant.ParticipantDto;
import com.divisonapp.model.EventParticipant;
import com.divisonapp.model.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {
    @Mapping(source = "eventLinks", target = "events")
    ParticipantDto toDto(Participant participant);

    default List<Long> map(Set<EventParticipant> links) {
        if (links == null) return List.of();
        return links.stream()
                .map(link -> link.getEvent().getId())
                .toList();
    }
}
