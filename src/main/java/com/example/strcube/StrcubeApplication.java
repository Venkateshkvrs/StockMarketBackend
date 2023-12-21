package com.example.strcube;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class StrcubeApplication {

	public static void main(String[] args) {
		SpringApplication.run(StrcubeApplication.class, args);
	}
	@Bean
	public WebMvcConfigurer configureWeb() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry reg) {
				reg.addMapping("/**").allowedOrigins("*");
			}
		};
	}
}
