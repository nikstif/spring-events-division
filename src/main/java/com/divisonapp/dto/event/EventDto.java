package com.divisonapp.dto.event;

import lombok.Data;

import java.util.List;

@Data
public class EventDto {
    private Long id;
    private String name;
    private String description;
    private List<Long> participantIds;
}
