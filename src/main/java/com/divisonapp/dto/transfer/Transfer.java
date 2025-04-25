package com.divisonapp.dto.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {
    private Long eventId;
    private String from;
    private String to;
    private double amount;
}
