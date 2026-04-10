package com.lupafederal.core_api.controller;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Lupa Federal API",
                version = "v1",
                description = "API para consulta e ingestão de gastos públicos federais"
        )
)
@Configuration
public class OpenApiConfig {
}
