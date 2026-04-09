package com.lupafederal.core_api.config;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Bean(initMethod = "migrate")
    Flyway flyway(
            DataSource dataSource,
            @Value("${spring.flyway.locations:classpath:db/migration}") String[] locations,
            @Value("${spring.jpa.properties.hibernate.default_schema:lupa}") String defaultSchema,
            @Value("${spring.flyway.create-schemas:true}") boolean createSchemas) {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations(locations)
                .defaultSchema(defaultSchema)
                .schemas(defaultSchema)
                .createSchemas(createSchemas)
                .load();
    }
}
