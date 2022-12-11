package net.devnguyen.remitanomanager.constants;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public interface Const {
    String TOKEN_TYPE = "bearer";
    ZoneId UTC7 = ZoneId.ofOffset("UTC", ZoneOffset.of("+07"));
    DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
}
