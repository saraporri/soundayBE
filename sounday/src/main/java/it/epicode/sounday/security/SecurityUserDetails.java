package it.epicode.sounday.security;

import it.epicode.sounday.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;

@Data
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class SecurityUserDetails implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    private Collection<? extends GrantedAuthority> authorities;
    private String password;
    private String username;
    @Builder.Default
    private boolean accountNonExpired = true;
    @Builder.Default
    private boolean accountNonLocked = true;
    @Builder.Default
    private boolean credentialsNonExpired = true;
    @Builder.Default
    private boolean enabled = true;

    public static SecurityUserDetails build(User user) {
        var authorities = user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getRoleType())).toList();
        return SecurityUserDetails.builder()
                .withUsername(user.getUsername())
                .withPassword(user.getPassword())
                .withAuthorities(authorities)
                .build();
    }
}
