package net.devnguyen.remitanomanager.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utils {

    public static  <T> T getOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }
}
