package net.devnguyen.remitanomanager.configuration.security;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import net.devnguyen.remitanomanager.dto.auth.BasicAuthority;
import net.devnguyen.remitanomanager.utils.Utils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Getter
public class BasicAuthentication extends AbstractAuthenticationToken {

    private Object principal;

    private Object credentials;

    private Claims claims;

    private BasicAuthority authority;

    private boolean isRoot;

    public BasicAuthentication(BasicAuthority authority, Claims claims) {
        super(Utils.getOrDefault(authority.getScopes(), new HashSet<String>())
                .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
        this.claims = claims;
        this.authority = authority;
        this.isRoot = authority.getIsRoot() != null && authority.getIsRoot();
        this.setAuthenticated(true);
    }

    /**
     * verify account and password when login
     *
     * @param username
     * @param password
     */
    public BasicAuthentication(String username, String password) {
        super(null);
        this.principal = username;
        this.credentials = password;
        setAuthenticated(false);
    }

    private BasicAuthentication(Claims claims, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = claims.getSubject();
        this.credentials = claims.getSubject();
        this.claims = claims;
    }

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */


    private BasicAuthentication(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
