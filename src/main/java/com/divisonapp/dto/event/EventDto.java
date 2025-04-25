package com.divisonapp.dto.event;

import com.divisonapp.dto.transfer.Transfer;
import lombok.Data;

import java.util.List;

@Data
public class EventDto {
    private Long id;
    private String name;
    private String description;
    private List<Long> participantIds;
    private List<Transfer> transfers;
}
