package com.interview.etravli.dto.feign;

import lombok.Data;
import java.io.Serializable;

@Data
public class CountryFeignDTO implements Serializable{

    private String numeric;
    private String alpha2;
    private String name;
    private String emoji;
    private String currency;
    private int latitude;
    private int longitude;

}
