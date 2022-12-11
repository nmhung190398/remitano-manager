package net.devnguyen.remitanomanager.configuration.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devnguyen.remitanomanager.configuration.security.configuration.SecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebResourceServerConfigurer extends WebSecurityConfigurerAdapter {

    private final SecurityConfig securityConfig;
    private final CustomAuthenticationFilter customAuthenticationFilter;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        configureAuthorizeRequests(http)
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;
//        http.addFilterBefore(addTokenInParamAuthenticationFilter, BearerTokenAuthenticationFilter.class);
        http.addFilterAfter(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    private HttpSecurity configureAuthorizeRequests(HttpSecurity http) throws Exception {
        AtomicReference<ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry> authorizeRequests =
                new AtomicReference<>(http.authorizeRequests());
        applyMatcherConfig(
                method -> authorizeRequests.set(authorizeRequests.get().antMatchers(method).permitAll()),
                paths -> authorizeRequests.set(authorizeRequests.get().antMatchers(
                        paths.toArray(new String[0])).permitAll()),
                (method, paths) -> authorizeRequests.set(authorizeRequests.get().antMatchers(
                        method, paths.toArray(new String[0])).permitAll())
        );

        return authorizeRequests.get()
                .antMatchers("/**").authenticated()
                .and();
    }

    private void applyMatcherConfig(
            Consumer<HttpMethod> applyPermitAllMethod,
            Consumer<List<String>> applyPermitAllPaths,
            BiConsumer<HttpMethod, List<String>> applyMethodWithPaths
    ) {
        SecurityConfig.PathMatcherConfig pathMatcherConfig = securityConfig.getPathMatcher();
        if (pathMatcherConfig != null) {
            log.warn("pathMatcherConfig not null");
            if (pathMatcherConfig.getPermitAllMethods() != null) {
                pathMatcherConfig.getPermitAllMethods()
                        .forEach(applyPermitAllMethod);
            }
            if (pathMatcherConfig.getPermitAllPathPatterns() != null) {
                List<String> normalizedPermitAllPatterns = pathMatcherConfig.getPermitAllPathPatterns().stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                applyPermitAllPaths.accept(normalizedPermitAllPatterns);
            }
            if (pathMatcherConfig.getPermitAllMap() != null) {
                pathMatcherConfig.getPermitAllMap().forEach(((httpMethod, patterns) -> {
                    List<String> normalizedPatterns = patterns.stream()
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    applyMethodWithPaths.accept(httpMethod, normalizedPatterns);
                }));
            }
        } else {
            log.warn("pathMatcherConfig null");
        }
    }
}
