package com.interview.etravli.dto.feign;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BinListFeignDTO implements Serializable {

    @JsonProperty("Status")
    private String status;
    @JsonProperty("Scheme")
    private String scheme;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Issuer")
    private String issuer;
    @JsonProperty("CardTier")
    private String cardTier;
    @JsonProperty("Country")
    private CountryFeignDTO country;
    @JsonProperty("Luhn")
    private boolean luhn;
}
