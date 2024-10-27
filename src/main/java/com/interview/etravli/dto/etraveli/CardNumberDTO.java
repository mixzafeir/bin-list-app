package com.interview.etravli.dto.etraveli;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CardNumberDTO {

    @NotNull(message = "Card issuing country must not be null.")
    @Size(min = 6, message = "Card issuing country must be between 6 and 18 characters.")
    String card_number;

}
