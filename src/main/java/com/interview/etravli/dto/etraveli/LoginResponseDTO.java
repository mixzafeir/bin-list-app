package com.interview.etravli.dto.etraveli;

import com.interview.etravli.enums.Roles;
import lombok.Data;

@Data
public class LoginResponseDTO {

    private String username;
    private Roles role;
    private String jwt;

}
