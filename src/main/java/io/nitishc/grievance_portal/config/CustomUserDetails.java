package io.nitishc.grievance_portal.config;

import io.nitishc.grievance_portal.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String password;
    @Getter
    private final String department;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user){
        this.username= user.getEmail();
        this.password=user.getPassword();
        this.department= Objects.toString(user.getDepartment(), null);
        this.authorities= List.of(new SimpleGrantedAuthority(user.getRole().toString()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
