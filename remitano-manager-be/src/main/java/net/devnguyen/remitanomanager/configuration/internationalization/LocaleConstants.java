package net.devnguyen.remitanomanager.configuration.internationalization;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class LocaleConstants {
    public final static Locale DEFAULT_LOCALE = new Locale("vi");

    public final static Map<String, Locale> SUPPORT_LOCALES = Stream.of(
            Locale.ENGLISH,
            DEFAULT_LOCALE
    ).collect(Collectors.toMap(Locale::getLanguage, locale -> locale));
}
