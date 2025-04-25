package com.divisonapp.dto.event;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EventRequest {
    @NotBlank(message = "name should not be empty")
    private String name;

    @NotBlank(message = "description should not be empty")
    private String description;
}
