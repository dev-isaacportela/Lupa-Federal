package com.lupafederal.core_api.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Lupa Federal API",
                version = "v1",
                description = "API para consulta e ingestao de gastos publicos federais"
        )
)
@Configuration
public class OpenApiConfig {
}
