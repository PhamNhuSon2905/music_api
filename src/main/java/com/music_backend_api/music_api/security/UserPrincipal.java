package com.music_backend_api.music_api.security;

import com.music_backend_api.music_api.model.User;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class UserPrincipal implements UserDetails {

    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.getEnabled());
    }


    public String getDisplayAvatar() {
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            return user.getAvatar();
        } else {
            return "/assets/images/user2-160x160.jpg";
        }
    }

    public static void updateAuthentication(User updatedUser) {
        UserPrincipal updatedPrincipal = new UserPrincipal(updatedUser);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(updatedPrincipal, null, updatedPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
