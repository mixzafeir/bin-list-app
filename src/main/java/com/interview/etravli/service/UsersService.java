package com.interview.etravli.service;

import com.interview.etravli.dto.etraveli.LoginDTO;
import com.interview.etravli.dto.etraveli.LoginResponseDTO;
import com.interview.etravli.models.Users;

public interface UsersService {

    Users findByUsername(String username);

}
