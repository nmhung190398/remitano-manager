package net.devnguyen.remitanomanager.configuration.internationalization;

import java.util.Locale;

public interface LanguageService {
    Locale getCurrentLocale();

    String getMessage(String messageCode, String defaultMessage, Object... params);
}
