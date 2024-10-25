package com.interview.etravli.dto.feign;

import lombok.Data;

import java.io.Serializable;

@Data
public class BinListFeignDTO implements Serializable {

    private NumberFeignDTO number;
    private String scheme;
    private String type;
    private String brand;
    private boolean prepaid;
    private CountryFeignDTO country;
    private BankFeignDTO bank;

}
