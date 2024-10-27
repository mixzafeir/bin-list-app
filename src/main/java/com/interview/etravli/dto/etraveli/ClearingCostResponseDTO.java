package com.interview.etravli.dto.etraveli;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClearingCostResponseDTO {

    private String country;
    private BigDecimal cost;

}
