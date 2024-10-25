package com.interview.etravli.dto.feign;

import lombok.Data;
import java.io.Serializable;

@Data
public class BankFeignDTO implements Serializable {

    private String name;
    private String url;
    private String phone;
    private String city;

}
