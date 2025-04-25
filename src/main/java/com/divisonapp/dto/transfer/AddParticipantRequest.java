package com.divisonapp.dto.transfer;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddParticipantRequest {
    @NotNull
    private Long participantId;

    @NotNull
    private Double payment;
}
