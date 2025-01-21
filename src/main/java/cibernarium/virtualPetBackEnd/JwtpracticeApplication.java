package cibernarium.virtualPetBackEnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JwtpracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtpracticeApplication.class, args);
	}

	// TODO MIrar las dependencias para JPA


	// Next:

	//TODO create pet, hay que hacer que la pet se guarde en la database, ir poco a poco.

	// TODO Swagger
	// TODO Testear el aspecto y el correcto funcionamiento del front en browser


}
