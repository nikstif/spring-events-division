package com.divisonapp.dto.participant;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ParticipantRequest {
    @NotBlank(message = "name should not be empty")
    private String name;

    @NotBlank(message = "phone should not be empty")
    private String phone;
}
