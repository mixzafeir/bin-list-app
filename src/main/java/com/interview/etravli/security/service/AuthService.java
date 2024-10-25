package com.interview.etravli.security.service;

import com.interview.etravli.dto.etraveli.LoginDTO;
import com.interview.etravli.dto.etraveli.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO authenticate(LoginDTO loginDto);

}
