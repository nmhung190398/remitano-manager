package net.devnguyen.remitanomanager.configuration.security.jwt;

public interface JwtTokenProvider<Playload> {

    Playload parseToken(String token);

    JWTToken generateToken(Playload playload);
}
