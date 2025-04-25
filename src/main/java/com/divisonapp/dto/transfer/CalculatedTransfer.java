package com.divisonapp.dto.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculatedTransfer {
    private String from;
    private String to;
    private double amount;
}
