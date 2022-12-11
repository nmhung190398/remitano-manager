package net.devnguyen.remitanomanager.configuration.security;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devnguyen.remitanomanager.configuration.security.jwt.BasicJWTTokenProvider;
import net.devnguyen.remitanomanager.constants.Const;
import net.devnguyen.remitanomanager.dto.auth.BasicAuthority;
import net.devnguyen.remitanomanager.exception.ErrorResponseWriter;
import net.devnguyen.remitanomanager.exception.ResponseException;
import net.devnguyen.remitanomanager.exception.errorcode.InternalServerError;
import net.devnguyen.remitanomanager.service.impl.UserServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final ErrorResponseWriter errorResponseWriter;
    private final BasicJWTTokenProvider basicJWTTokenProvider;
    private final UserServiceImpl userServiceImpl;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String token = getJwtFromRequest(request);
        if (!StringUtils.hasLength(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            var claimsJwt = basicJWTTokenProvider.parseTokenToClaims(token);
            var username = claimsJwt.getSubject();
            var authority = getAuthority(username);

            BasicAuthentication authentication = new BasicAuthentication(authority, claimsJwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (ResponseException e) {
            errorResponseWriter.writeErrorResponse(response, MediaType.APPLICATION_JSON_VALUE, e.getError());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorResponseWriter.writeErrorResponse(response, MediaType.APPLICATION_JSON_VALUE, InternalServerError.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * query in db by username
     *
     * @param username
     * @return
     */
    private BasicAuthority getAuthority(String username) {
        return userServiceImpl.getUserAuthority(username);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // Kiểm tra xem header Authorization có chứa thông tin jwt không
        if (StringUtils.hasText(bearerToken) && bearerToken.toLowerCase(Locale.ROOT).startsWith(Const.TOKEN_TYPE.toLowerCase(Locale.ROOT) + " ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return false;
    }
}
