package com.likelion.helfoome.global.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

// Swagger 접속 주소
// http://localhost:8080/swagger-ui/index.html#/
// https://43.202.187.24:8080/swagger-ui/index.html
@OpenAPIDefinition(
    servers = {
        @Server(url = "https://metalog.store", description = "개발 서버"),
        @Server(url = "http://localhost:8080", description = "로컬 서버")
    })
@Configuration
public class SwaggerConfig {

  //@Value("${server.servlet.context-path:}")
  //private String contextPath;

  @Bean
  public OpenAPI customOpenAPI() {
    //Server localServer = new Server();
    //localServer.setUrl(contextPath);
    //localServer.setDescription("Local Server");

    //Server prodServer = new Server();
    //prodServer.setUrl(
      //  "https://metalog.store:8080");
    //prodServer.setDescription("Production Server");

    return new OpenAPI()
        //.addServersItem(localServer)
        //.addServersItem(prodServer)
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(
            new Components()
                .addSecuritySchemes(
                    "bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
        .info(new Info().title("헬푸미 API 명세서").version("1.0").description("HelFooMe Swagger"));
  }

  @Bean
  public GroupedOpenApi customGroupedOpenApi() {
    return GroupedOpenApi.builder().group("api").pathsToMatch("/**").build();
  }
}
