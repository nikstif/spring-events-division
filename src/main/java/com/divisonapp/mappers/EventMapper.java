package com.divisonapp.mappers;

import com.divisonapp.dto.event.EventDto;
import com.divisonapp.model.Event;
import com.divisonapp.model.EventParticipant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(source = "participantLinks", target = "participantIds")
    EventDto toDto(Event event);

    default List<Long> map(Set<EventParticipant> links) {
        if (links == null) return List.of();
        return links.stream()
                .map(link -> link.getParticipant().getId())
                .toList();
    }
}
