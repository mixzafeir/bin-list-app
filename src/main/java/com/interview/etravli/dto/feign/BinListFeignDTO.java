package com.interview.etravli.dto.feign;

import lombok.Data;

import java.io.Serializable;

@Data
public class BinListFeignDTO implements Serializable {

    private String Status;
    private String Scheme;
    private String Type;
    private String Issuer;
    private String CardTier;
    private boolean prepaid;
    private CountryFeignDTO country;
    private String Luhn;
}
