/*
 * JWTToken.java
 *
 * Copyright (C) 2021 by Evotek. All right reserved.
 * This software is the confidential and proprietary information of Evotek
 */
package net.devnguyen.remitanomanager.configuration.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * 02/06/2021 - LinhLH: Create new
 *
 * @author LinhLH
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class JWTToken {
    private String accessToken;

    private String refreshToken;

    private String type;

    private Instant expiresIn;
}
