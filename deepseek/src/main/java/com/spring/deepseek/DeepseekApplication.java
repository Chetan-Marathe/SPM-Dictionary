package com.spring.deepseek;


import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DeepseekApplication{

	public static void main(String[] args) {
		SpringApplication.run(DeepseekApplication.class, args);
	}


}