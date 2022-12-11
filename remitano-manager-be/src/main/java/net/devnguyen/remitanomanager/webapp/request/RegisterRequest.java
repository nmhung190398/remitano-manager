package net.devnguyen.remitanomanager.webapp.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class RegisterRequest {
    @NotNull(message = "REQUIRE")
    @Pattern(regexp = "^(.+)@(.+)$", message = "EMAIL_PATTERN_WRONG")
    private String email;

    @NotNull(message = "REQUIRE")
    private String fullName;

    @NotNull(message = "REQUIRE")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$", message = "PASSWORD_WEAK")
    private String password;
}
