package com.interview.etravli.dto.etraveli;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClearingCostResponseDTO {

    private String country;
    private BigDecimal cost;

}
