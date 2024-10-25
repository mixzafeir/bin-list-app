package com.interview.etravli.dto.etraveli;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class EtraveliExceptionDTO {

    private String message;
    private int code;
    private HttpStatus status;

}
