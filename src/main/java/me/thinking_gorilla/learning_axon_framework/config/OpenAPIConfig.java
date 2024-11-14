package me.thinking_gorilla.learning_axon_framework.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Axon framework API")
                        .description("Axon Sample application")
                        .version("0.0.1")
                        .license(new License().name("").url("https://springdoc.org/"))
                ).externalDocs(new ExternalDocumentation()
                        .description("About Application")
                        .url("https://github.com/thinkingGorilla/learning_axon_framework")
                );
    }
}