package net.devnguyen.remitanomanager.service;

import net.devnguyen.remitanomanager.configuration.security.jwt.JWTToken;
import net.devnguyen.remitanomanager.domain.UserDomain;
import net.devnguyen.remitanomanager.dto.auth.BasicAuthority;
import net.devnguyen.remitanomanager.webapp.request.LoginRequest;
import net.devnguyen.remitanomanager.webapp.request.RegisterRequest;

public interface UserService {
    UserDomain getDetail(String id);

    JWTToken login(LoginRequest request);

    JWTToken loginWithGoogle(String code);

    BasicAuthority getUserAuthority(String username);

    void register(RegisterRequest request);
}
