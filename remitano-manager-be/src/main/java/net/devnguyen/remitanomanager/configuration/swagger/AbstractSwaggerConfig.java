package net.devnguyen.remitanomanager.configuration.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@EnableSwagger2
public abstract class AbstractSwaggerConfig {

    protected abstract ApiInfo metadata();

    @Bean
    public Docket api() {
        AuthorizationScope[] authScopes = new AuthorizationScope[0];
        SecurityReference securityReference = SecurityReference.builder()
                .reference("bearer_token")
                .scopes(authScopes)
                .build();

        List<SecurityContext> securityContexts = Collections.singletonList(
                SecurityContext.builder()
                        .securityReferences(Collections.singletonList(securityReference))
                        .build());

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage()))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metadata())
                .useDefaultResponseMessages(false)
//                .globalOperationParameters(defaultParameters())       // tam thoi chu su dung
                .securitySchemes(Collections.singletonList(securityScheme()))
                .securityContexts(securityContexts)
                .genericModelSubstitutes(Optional.class);
    }

    protected String basePackage() {
        return "net.devnguyen.remitano";
    }

    protected List<Parameter> defaultParameters() {
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        Parameter parameter = parameterBuilder.name(HttpHeaders.ACCEPT_LANGUAGE)
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue("vi")
                .required(false)
                .build();
        return Collections.singletonList(parameter);
    }

    private SecurityScheme securityScheme() {
        return new ApiKey("bearer_token", "Authorization", "header");
    }
}
