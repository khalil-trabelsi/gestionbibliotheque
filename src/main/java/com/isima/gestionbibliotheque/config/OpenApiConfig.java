package com.isima.gestionbibliotheque.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "khtrabelsi",
                        email = "khaliltrabelsi2107@gmail.com"
               ),
                title = "OpenApi specification",
                description = "OpenApi documentation for Personal Library Management",
                version = "V1"
          ),
        servers = {
                @Server(description = "Local Server", url="http://localhost:8080"),
                @Server(description = "Prod Server", url="http://localhost:8080"), // To do: set the prod server url
        }
)
@SecurityScheme(
        name = "BearerAuth",
        description = "JWT Auth Description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
