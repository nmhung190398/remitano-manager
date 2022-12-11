package net.devnguyen.remitanomanager.configuration.security.jwt;


import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import net.devnguyen.remitanomanager.configuration.security.configuration.AuthenticationProperties;
import net.devnguyen.remitanomanager.constants.Const;
import net.devnguyen.remitanomanager.exception.ResponseException;
import net.devnguyen.remitanomanager.exception.errorcode.UnauthorizedError;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class BasicJWTTokenProvider implements JwtTokenProvider<String> {

    private final AuthenticationProperties authenticationProperties;
    //    private final UserDetailsService userDetailsService;
    private final String secret;

    public BasicJWTTokenProvider(AuthenticationProperties authenticationProperties) {
        this.authenticationProperties = authenticationProperties;
//        this.userDetailsService = userDetailsService;

        secret = authenticationProperties.getSecret();
    }

    interface CLAIM_KEY {
        String USERNAME = "username";
        String SCOPES = "scopes";
    }

    public JWTToken generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY.USERNAME, username);

        Instant now = Instant.now();
        Instant expiryDate = now.plus(authenticationProperties.getTokenDuration());
        var jwt = Jwts.builder()
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiryDate))
                .addClaims(claims)
                .setSubject(username)
                .setIssuer(authenticationProperties.getIssuer())
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return JWTToken.builder()
                .accessToken(jwt)
                .expiresIn(expiryDate)
                .type(Const.TOKEN_TYPE)
                .build();
    }

    public String parseToken(String token) {
        var claims = parseTokenToClaims(token);
        String username = claims.get(CLAIM_KEY.USERNAME, String.class);
        return username;
    }

    public Claims parseTokenToClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secret)
                    .requireIssuer(authenticationProperties.getIssuer())
                    .parseClaimsJws(token).getBody();

        } catch (MalformedJwtException ex) {
            log.error(ex.getMessage(), ex);
            throw new ResponseException(UnauthorizedError.MALFORMED_JWT);
        } catch (ExpiredJwtException ex) {
            log.error(ex.getMessage(), ex);
            throw new ResponseException(UnauthorizedError.EXPIRED_JWT);
        } catch (UnsupportedJwtException ex) {
            log.error(ex.getMessage(), ex);
            throw new ResponseException(UnauthorizedError.UNSUPPORTED_JWT);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new ResponseException(UnauthorizedError.AUTHENTICATION_IS_REQUIRED);
        }
    }
}
