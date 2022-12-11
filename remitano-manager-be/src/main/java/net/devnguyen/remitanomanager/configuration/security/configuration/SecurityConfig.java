package net.devnguyen.remitanomanager.configuration.security.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.Set;

@Configuration
@ConfigurationProperties(prefix = "security")
@Data
public class SecurityConfig {

    /**
     * Map of client which may contain rest uri (e.x. smch.security.clients.iam.rest.uri
     *
     * @see Client
     * @see RestEndpoint
     */
    private Map<String, Client> clients;

    /**
     * Configure url path which are all allowed without authentication
     */
    private PathMatcherConfig pathMatcher;


    @Data
    public static class PathMatcherConfig {
        /**
         * Set of allowed http method for all paths
         */
        private Set<HttpMethod> permitAllMethods = null;

        /**
         * Set of allowed paths for all http methods
         */
        private Set<String> permitAllPathPatterns = null;

        /**
         * Map of allowed http method and paths pair
         */
        private Map<HttpMethod, Set<String>> permitAllMap = null;
    }

    @Data
    public static class RestEndpoint {
        private String uri;
    }

    @Data
    public static class Client {
        private RestEndpoint rest;
        private Map<String, String> properties;
    }
}
