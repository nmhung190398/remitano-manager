package net.devnguyen.remitanomanager.service.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "oauth2.google", ignoreUnknownFields = false)
@Data
public class OAuthGoogleProperty {
    private String googleLinkConnect = "";
    private String googleRedirectUri = "";
    private String googleClientId = "";
    private String googleClientSecret = "";
    private String googleGrantType = "";
    private String googleLinkGetToken = "";
    private String googleLinkGetUserInfo = "";
    private String urlRedirect = "";
}
