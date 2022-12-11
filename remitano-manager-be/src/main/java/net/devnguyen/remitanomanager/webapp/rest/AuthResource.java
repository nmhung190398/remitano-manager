package net.devnguyen.remitanomanager.webapp.rest;

import lombok.RequiredArgsConstructor;
import net.devnguyen.remitanomanager.configuration.security.jwt.JWTToken;
import net.devnguyen.remitanomanager.service.UserService;
import net.devnguyen.remitanomanager.webapp.request.LoginRequest;
import net.devnguyen.remitanomanager.webapp.request.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthResource {

    private final UserService userService;

    @GetMapping("/login-with-google")
    public ResponseEntity<JWTToken> loginWithGoogle(@RequestParam String code) {
        var token = userService.loginWithGoogle(code);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<JWTToken> login(@Valid @RequestBody LoginRequest request) {
        var token = userService.login(request);
        return ResponseEntity.ok(token);
    }
    
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok().build();
    }


}
