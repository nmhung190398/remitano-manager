package net.devnguyen.remitanomanager.configuration.internationalization.impl;

import lombok.extern.slf4j.Slf4j;
import net.devnguyen.remitanomanager.configuration.internationalization.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Locale;

@Component
@Slf4j
public class HttpRequestLanguageService implements LanguageService {
    private final MessageSource messageSource;

    @Autowired
    public HttpRequestLanguageService(
            MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Locale getCurrentLocale() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return Locale.getDefault();
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return request.getLocale();
    }

    @Override
    public String getMessage(String messageCode, String defaultMessage, Object... params) {
        Locale currentLocale = getCurrentLocale();
        try {
            return messageSource.getMessage(messageCode, params, currentLocale);
        } catch (Exception e) {
//            log.error(e.getMessage(), e);
            if (e instanceof NoSuchMessageException) {
                log.warn("Could not find message {} for locale {}. Using default message", messageCode, currentLocale);
            } else {
                log.warn("Failed to get message {} for locale {}. Using default message", messageCode, currentLocale, e);
            }
            try {
                return MessageFormat.format(defaultMessage, params);
            } catch (Exception e1) {
//                log.error(e1.getMessage(), e1);
                return defaultMessage;
            }
        }
    }
}
