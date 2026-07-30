package com.example.MyBookShopApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//configuration to show Spring where are located our new static sources (outside of application)

@Configuration
public class MvcConfig implements WebMvcConfigurer {

  @Value("${upload.path}")
  private String uploadPath;


  //setup path to outside directory where are located uploaded files
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/book-covers/**")
        .addResourceLocations("file:" + uploadPath + "/");

  }

  @Bean
  public RestTemplate getRestTemplate() {
    return new RestTemplate();
  }
}
