package com.interview.etravli.dto.etraveli;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ClearingCostDTO {

    private UUID id;

    @NotNull(message = "Card issuing country must not be null.")
    @Size(max = 2, message = "Card issuing country must be exactly 2 characters.")
    private String cardIssuingCountry;

    @NotNull(message = "Clearing cost must not be null.")
    @Digits(integer = 15, fraction = 3, message = "Clearing cost must be a valid number with up to 15 integer digits and 3 decimal places.")
    private BigDecimal clearingCost;

}
