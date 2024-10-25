package com.interview.etravli.controllers;

import com.interview.etravli.dto.etraveli.LoginDTO;
import com.interview.etravli.dto.etraveli.LoginResponseDTO;
import com.interview.etravli.security.service.AuthService;
import com.interview.etravli.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDto) {
        LoginResponseDTO loggedIn = authService.authenticate(loginDto);
        return new ResponseEntity<>(loggedIn, HttpStatus.OK);
    }

}
