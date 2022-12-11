package net.devnguyen.remitanomanager.configuration.security;

import lombok.extern.slf4j.Slf4j;
import net.devnguyen.remitanomanager.exception.ResponseException;
import net.devnguyen.remitanomanager.exception.errorcode.AccessDeniedError;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.regex.Pattern;

@Slf4j
@Component
public class RegexPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        String requiredPermission = permission.toString();

        log.warn("RegexPermissionEvaluator hasPermission {}", permission);

        if (!(authentication instanceof BasicAuthentication)) {
            throw new ResponseException(
                    MessageFormat.format(
                            AccessDeniedError.NOT_SUPPORTED_AUTHENTICATION.getMessage(),
                            authentication.getClass().getName()),
                    AccessDeniedError.NOT_SUPPORTED_AUTHENTICATION, authentication.getClass().getName());
        }
        var basicAuthentication = (BasicAuthentication) authentication;

        if (basicAuthentication.isRoot()) {
            return true;
        }

        boolean isPermitted = basicAuthentication.getAuthority().getScopes().stream()
                .anyMatch(scope -> Pattern.matches(scope, requiredPermission));

        if (!isPermitted) {
            throw new ResponseException(
                    MessageFormat.format(
                            AccessDeniedError.NOT_PERMITTED_ACCESS.getMessage(), permission),
                    AccessDeniedError.NOT_PERMITTED_ACCESS, permission);
        }

        return true;
    }

    @Override
    public boolean hasPermission(
            Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, null, permission);
    }
}
