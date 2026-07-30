package com.example.MyBookShopApp.config;

import io.swagger.annotations.Api;
import java.util.ArrayList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringfoxConfig {

  @Bean
  public Docket docket() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        //.apis(RequestHandlerSelectors.any()) //отображает подкапотные методы спринга в документации
        .apis(RequestHandlerSelectors.basePackage("com.example.MyBookShopApp.controller"))  //отображает по выбранному пути
        //.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))  //отображает по аннотации
        .paths(PathSelectors.any())  //отображает все методы
        //.paths(PathSelectors.ant("/api/*")) // в документацию попадают методы, в эндпоинте которых есть приставка api
        .build()
        .apiInfo(apiInfo())
        ;
  }

  public ApiInfo apiInfo() {
    return new ApiInfo("Bookshop API",
        "API for bookshop",
        "1.0",
        "http://termsofservice.org",
        new Contact("API Owner Name", "http://www.ownersite.com", "owner@rmailer.org"),
        "api_license",
        "http://www.lecense.edu.org",
        new ArrayList<>()
    );
  }

}
