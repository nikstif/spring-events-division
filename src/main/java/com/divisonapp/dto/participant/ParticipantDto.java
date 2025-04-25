package com.divisonapp.dto.participant;

import lombok.Data;

import java.util.List;

@Data
public class ParticipantDto {
    private Long id;
    private String name;
    private String phone;
    private List<Long> events;
}
