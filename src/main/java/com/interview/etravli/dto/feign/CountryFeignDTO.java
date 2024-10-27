package com.interview.etravli.dto.feign;

import lombok.Data;
import java.io.Serializable;

@Data
public class CountryFeignDTO implements Serializable{

    private String A2;
    private String A3;
    private String N3;
    private String ISD;
    private String name;
    private String cont;

}
