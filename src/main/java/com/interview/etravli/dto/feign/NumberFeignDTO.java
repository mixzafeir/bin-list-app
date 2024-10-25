package com.interview.etravli.dto.feign;

import lombok.Data;
import java.io.Serializable;

@Data
public class NumberFeignDTO implements Serializable{

    private int length;
    private boolean luhn;

}
