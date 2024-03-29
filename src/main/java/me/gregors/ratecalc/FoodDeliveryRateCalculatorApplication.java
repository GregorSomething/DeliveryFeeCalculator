package me.gregors.ratecalc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FoodDeliveryRateCalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodDeliveryRateCalculatorApplication.class, args);
		// curl http://localhost:8080/api/delivery_fee -X POST -d "{\"city\": \"tallinn\", \"vehicle\": \"car\"}" -H 'Content-Type: application/json'
	}

}
