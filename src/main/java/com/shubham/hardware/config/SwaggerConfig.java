package com.shubham.hardware.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket docket(){
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(getApiInfo());

//        spring security in swagger
        docket.securityContexts(Arrays.asList(getSecurityContext()));
        docket.securitySchemes(Arrays.asList(getSecuritySchemas()));

        ApiSelectorBuilder select = docket.select();
        select.apis(RequestHandlerSelectors.any());
        select.paths(PathSelectors.any());
        Docket securityDocket = select.build();
        
        return securityDocket;
    }


    private SecurityContext getSecurityContext() {
        SecurityContext context=SecurityContext.builder()
                .securityReferences(getSecurityReferences())
                .build();
        return context;
    }

    private ApiKey getSecuritySchemas() {
        return new ApiKey("JWT","Authorization","header");
    }

    private List<SecurityReference> getSecurityReferences() {
        AuthorizationScope[] scopes= {
                new AuthorizationScope("Global", "Access Every Thing")
        };
        return List.of(new SecurityReference("JWT", scopes));
    }

    private ApiInfo getApiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "Shubham Hardware Backend : APIS",
                "This is a backend api's for creating a online shop for selling products!!",
                "1.0.0V",
                "https://www.shubham-hardware.com",
                new Contact("Shubham Kumar","https://www.instagram.com/kumsr53shubham","kumar53shubham@gmail.com"),
                "License of APIS",
                "https://www.shubham-hardware.com/about",
                new ArrayDeque<>()
        );
        return apiInfo;
    }
}
