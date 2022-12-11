package net.devnguyen.remitanomanager.configuration.security.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "security.authentication.jwt")
@Data
public class AuthenticationProperties {
    private String secret;

    private Duration tokenDuration;

    private Duration refreshTokenDuration;

    private String issuer;
}
