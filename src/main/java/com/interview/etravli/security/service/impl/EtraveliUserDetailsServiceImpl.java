    package com.interview.etravli.security.service.impl;

    import com.interview.etravli.models.Users;
    import com.interview.etravli.security.service.EtraveliUserDetailsService;
    import com.interview.etravli.service.UsersService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;
    import com.interview.etravli.dto.etraveli.UserPrincipal;

    @Service
    public class EtraveliUserDetailsServiceImpl implements EtraveliUserDetailsService {

        private final UsersService usersService;

        @Autowired
        public EtraveliUserDetailsServiceImpl(UsersService usersService) {
            this.usersService = usersService;
        }

            @Override
            public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
                Users user = usersService.findByUsername(username);
                return UserPrincipal.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .role(user.getRole())
                        .authority(new SimpleGrantedAuthority(user.getRole().toString()))
                        .build();
            }

    }
