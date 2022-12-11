package net.devnguyen.remitanomanager.webapp.request;


import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class LoginRequest {
    @NotNull
    private String email;
    @NotNull
    private String password;
}
