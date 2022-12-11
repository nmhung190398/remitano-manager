package net.devnguyen.remitanomanager.webapp.request;

import lombok.Data;

import java.math.BigInteger;

@Data
public class EnableOfferRequest {
    BigInteger totalAmount;
}
