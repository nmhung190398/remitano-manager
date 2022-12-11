package net.devnguyen.remitanomanager.configuration.swagger;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;

@Configuration
//@ConditionalOnExpression(value = "${spring.swagger.enable:false}")
public class SwaggerConfig extends AbstractSwaggerConfig {

    @Override
    protected ApiInfo metadata() {
        return new ApiInfoBuilder()
                .title("devnguyen.net")
                .description(
                        "devnguyen.net")
                .build();
    }
}
