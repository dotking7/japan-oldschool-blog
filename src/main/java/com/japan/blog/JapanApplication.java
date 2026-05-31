package com.japan.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Esta es la clase principal que hace que la web se ponga en marcha
@SpringBootApplication
public class JapanApplication {

	public static void main(String[] args) {
		// Con esta linea arrancamos todo el sistema de Spring Boot
		SpringApplication.run(JapanApplication.class, args);
	}

}
