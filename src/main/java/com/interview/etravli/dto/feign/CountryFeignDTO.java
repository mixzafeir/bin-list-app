package com.interview.etravli.dto.feign;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@Data
public class CountryFeignDTO implements Serializable{

    @JsonProperty("A2")
    private String a2;
    @JsonProperty("A3")
    private String a3;
    @JsonProperty("N3")
    private String n3;
    @JsonProperty("ISD")
    private String isd;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Cont")
    private String cont;
}
