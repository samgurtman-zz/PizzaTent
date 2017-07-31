package co.nz.pizzatent.drivermanagementsystem;

import co.nz.pizzatent.drivermanagementsystem.Orders.OrderMessage;
import co.nz.pizzatent.drivermanagementsystem.Orders.OrderMessageProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@SpringBootApplication
public class DriverManagementSystemApplication {

    @Value("${GCE_PROJECT_ID}")
    private String gceProjectId;

	public static void main(String[] args) {
		SpringApplication.run(DriverManagementSystemApplication.class, args);
	}



	@Bean
	public MessageListener orderListener(){
        return new MessageListener(gceProjectId, "orders", new OrderMessageProcessor());
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
