package co.nz.pizzatent.drivermanagementsystem;

import co.nz.pizzatent.drivermanagementsystem.api.RestInteface;
import co.nz.pizzatent.drivermanagementsystem.orders.OrderMessage;
import co.nz.pizzatent.drivermanagementsystem.orders.OrderMessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class Application {

    @Value("${GCE_PROJECT_ID}")
    private String gceProjectId;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
    public Set<OrderMessage> ordersReceived(){
	    return ConcurrentHashMap.newKeySet();
    }

    @Autowired
	@Bean
	public MessageListener orderListener(OrderMessageProcessor orderMessageProcessor){
        return new MessageListener(gceProjectId, "orders-subscription", orderMessageProcessor);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
