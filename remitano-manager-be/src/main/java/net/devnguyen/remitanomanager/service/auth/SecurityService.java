package net.devnguyen.remitanomanager.service.auth;

import net.devnguyen.remitanomanager.configuration.security.BasicAuthentication;
import net.devnguyen.remitanomanager.dto.auth.BasicAuthority;
import net.devnguyen.remitanomanager.exception.ResponseException;
import net.devnguyen.remitanomanager.exception.errorcode.AccessDeniedError;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class SecurityService {

    public BasicAuthority getBasicAuthority() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof BasicAuthentication)) {
            throw new ResponseException(
                    MessageFormat.format(
                            AccessDeniedError.NOT_SUPPORTED_AUTHENTICATION.getMessage(),
                            authentication.getClass().getName()),
                    AccessDeniedError.NOT_SUPPORTED_AUTHENTICATION, authentication.getClass().getName());
        }
        var basicAuthentication = (BasicAuthentication) authentication;
        return basicAuthentication.getAuthority();
    }

    public String getUserIdOrNull() {
        try {
            return getBasicAuthority().getUserId();
        } catch (Exception e) {
            return null;
        }
    }
}
