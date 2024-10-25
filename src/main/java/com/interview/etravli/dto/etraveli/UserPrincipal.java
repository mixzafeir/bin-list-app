package com.interview.etravli.dto.etraveli;

import com.interview.etravli.enums.Roles;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Builder
@Getter
public class UserPrincipal implements UserDetails {

    private String username;
    private String password;
    private Roles role;
    private GrantedAuthority authority;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(authority);
    }
}
