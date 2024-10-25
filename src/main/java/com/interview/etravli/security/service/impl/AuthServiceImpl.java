package com.interview.etravli.security.service.impl;
import com.interview.etravli.dto.etraveli.LoginDTO;
import com.interview.etravli.dto.etraveli.LoginResponseDTO;
import com.interview.etravli.dto.etraveli.UserPrincipal;
import com.interview.etravli.security.service.AuthService;
import com.interview.etravli.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponseDTO authenticate(LoginDTO loginDto) {
        LOGGER.info("Starting Authentication");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        LOGGER.info("User Principal retrieved");
        LoginResponseDTO response = new LoginResponseDTO();
        response.setJwt(jwtUtil.generateToken(userPrincipal.getUsername(), userPrincipal.getRole().toString()));
        response.setRole(userPrincipal.getRole());
        response.setUsername(userPrincipal.getUsername());
        return response;
    }

}
