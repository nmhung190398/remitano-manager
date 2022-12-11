package net.devnguyen.remitanomanager.webapp.rest;

import lombok.RequiredArgsConstructor;
import net.devnguyen.remitanomanager.service.UserService;
import net.devnguyen.remitanomanager.service.auth.SecurityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserResource {
    private final UserService userService;
    private final SecurityService securityService;

    @GetMapping("/{userId}")
    public Object getById(@PathVariable String userId) {
        return userService;
    }

    @GetMapping("/me")
    public Object currentUser() {
        return securityService.getBasicAuthority();
    }
}
