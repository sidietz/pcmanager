package com.oberamsystems.ai.pcmanager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.oberamsystems.ai.pcmanager.model.ComponentType;
import com.oberamsystems.ai.pcmanager.repository.ComponentTypeRepository;

@SpringBootApplication
public class PcmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PcmanagerApplication.class, args);
	}

	@Bean
	public CommandLineRunner initProcs(ComponentTypeRepository repository) {
		return (args) -> {
			/*
			 * repository.save(new ComponentType("CPU"));
			 * repository.save(new ComponentType("Mainboard"));
			 * repository.save(new ComponentType("RAM"));
			 * repository.save(new ComponentType("PSU"));
			 * repository.save(new ComponentType("SSD"));
			 * repository.save(new ComponentType("HDD"));
			 * repository.save(new ComponentType("Case"));
			 * repository.save(new ComponentType("other"));
			 */
		};
	}

}
