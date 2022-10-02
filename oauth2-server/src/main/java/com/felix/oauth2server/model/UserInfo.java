package com.felix.oauth2server.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class UserInfo extends User {
    private static final long serialVersionUID = -1682227070901462452L;
    private String accountOpenCode;
    private String nickname;

    public UserInfo(String accountOpenCode, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(accountOpenCode, username, password, true, true, true, true, authorities);
    }

    public UserInfo(String accountOpenCode, String username, String password, boolean enabled,
                    boolean accountNonExpired, boolean credentialsNonExpired,
                    boolean accountNonLocked,
                    Collection<? extends GrantedAuthority> authorities)
        throws IllegalArgumentException {
        super(username, password, enabled, accountNonExpired,
            credentialsNonExpired, accountNonLocked, authorities);
        this.accountOpenCode = accountOpenCode;
    }

}
