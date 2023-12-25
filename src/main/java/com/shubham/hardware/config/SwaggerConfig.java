package com.shubham.hardware.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.*;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.service.contexts.SecurityContext;
//import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
//import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

@Configuration
@SecurityScheme(
        name = "bearerScheme",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@OpenAPIDefinition(
        info = @Info(
                    title="Shubham Hardware Backend : APIS",
                    description="This is a backend api's for creating a online shop for selling products!!",
                    version="1.0v",
                    contact=@Contact(
                            name="Shubham Kumar",
                            email="kumar53shubham@gmail.com",
                            url="https://www.instagram.com/kumar53shubham"
                    ),
                    license=@License(
                            name="Shubham Hardware 1.0",
                            url="https://www.shubham-hardware.com"
                    )
        ),
        externalDocs = @ExternalDocumentation(
                url="https://www.shubham-hardware.com/about",
                description="This is online shopping portal for Hardware!!"
        )
)
public class SwaggerConfig {

//    spring for can be used in spring2.0

//    @Bean
//    public Docket docket(){
//        Docket docket = new Docket(DocumentationType.SWAGGER_2);
//        docket.apiInfo(getApiInfo());
//
////        spring security in swagger
//        docket.securityContexts(Arrays.asList(getSecurityContext()));
//        docket.securitySchemes(Arrays.asList(getSecuritySchemas()));
//
//        ApiSelectorBuilder select = docket.select();
//        select.apis(RequestHandlerSelectors.any());
//        select.paths(PathSelectors.any());
//        Docket securityDocket = select.build();
//
//        return securityDocket;
//    }
//
//
//    private SecurityContext getSecurityContext() {
//        SecurityContext context=SecurityContext.builder()
//                .securityReferences(getSecurityReferences())
//                .build();
//        return context;
//    }
//
//    private ApiKey getSecuritySchemas() {
//        return new ApiKey("JWT","Authorization","header");
//    }
//
//    private List<SecurityReference> getSecurityReferences() {
//        AuthorizationScope[] scopes= {
//                new AuthorizationScope("Global", "Access Every Thing")
//        };
//        return List.of(new SecurityReference("JWT", scopes));
//    }
//
//    private ApiInfo getApiInfo() {
//        ApiInfo apiInfo = new ApiInfo(
//                "Shubham Hardware Backend : APIS",
//                "This is a backend api's for creating a online shop for selling products!!",
//                "1.0.0V",
//                "https://www.shubham-hardware.com",
//                new Contact("Shubham Kumar","https://www.instagram.com/kumsr53shubham","kumar53shubham@gmail.com"),
//                "License of APIS",
//                "https://www.shubham-hardware.com/about",
//                new ArrayDeque<>()
//        );
//        return apiInfo;
//    }


//    spring-doc migration used in spring3.0
//    @Bean
//    public GroupedOpenApi publicApi() {
//        return GroupedOpenApi.builder()
//                .group("com.shubham.hardware.controllers")
//                .pathsToMatch("/auth/**","/shubham-hardware/users/**","/shubham-hardware/products/**","/shubham-hardware/categories/**","/shubham-hardware/carts/**","/shubham-hardware/orders/**","/welcome/shubham-hardware")
//                .build();
//    }
//
////    without annotation
//    @Bean
//    public OpenAPI openAPI(){
//
//        String schemeName="bearerScheme";
//
//        OpenAPI openAPI = new OpenAPI()
//                .addSecurityItem(new SecurityRequirement()
//                        .addList(schemeName)
//                )
//                .components(new Components()
//                        .addSecuritySchemes(schemeName,new SecurityScheme()
//                                .name(schemeName)
//                                .type(SecurityScheme.Type.HTTP)
//                                .bearerFormat("JWT")
//                                .scheme("bearer")
//                        )
//                )
//                .info(new Info().title("Shubham Hardware Backend : APIS")
//                        .description("This is a backend api's for creating a online shop for selling products!!")
//                        .version("1.0v")
//                        .contact(new Contact().name("Shubham Kumar").email("kumar53shubham@gmail.com").url("https://www.instagram.com/kumar53shubham"))
//                        .license(new License().name("Shubham Hardware 1.0").url("https://www.shubham-hardware.com"))
//                )
//                .externalDocs(new ExternalDocumentation()
//                .url("https://www.shubham-hardware.com/about")
//                .description("This is online shopping portal for Hardware!!")
//                );
//        return openAPI;
//    }
}
