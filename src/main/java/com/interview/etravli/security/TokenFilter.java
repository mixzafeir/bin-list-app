package com.interview.etravli.security;

import com.interview.etravli.dto.etraveli.UserPrincipal;
import com.interview.etravli.enums.ExceptionMessages;
import com.interview.etravli.exceptions.AuthorizationException;
import com.interview.etravli.security.service.impl.EtraveliUserDetailsServiceImpl;
import com.interview.etravli.service.UsersService;
import com.interview.etravli.service.impl.ClearingCostServiceImpl;
import com.interview.etravli.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EtraveliUserDetailsServiceImpl etraveliUserDetails;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        LOGGER.error("Starting Authorization");
        try{
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtil.validateToken(jwt)) {
                String username = jwtUtil.getUsernameFromToken(jwt);
                UserPrincipal userPrincipal = etraveliUserDetails.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }catch(Exception e){
            LOGGER.error("Cannot set Authorization: {}", e.getMessage());
            throw new AuthorizationException(ExceptionMessages.AUTHORIZATION_FAILED);
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer")) {
            return authHeader.substring(7);
        }
        return null;
    }

}
