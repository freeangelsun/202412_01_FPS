package fps.acc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"fps.cmn", "fps.acc"})
public class AccApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccApplication.class, args);
	}

}
