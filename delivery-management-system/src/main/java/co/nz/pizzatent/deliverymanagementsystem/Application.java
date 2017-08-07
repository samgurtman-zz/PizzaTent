package co.nz.pizzatent.deliverymanagementsystem;

import co.nz.pizzatent.deliverymanagementsystem.orders.OrderMessage;
import co.nz.pizzatent.deliverymanagementsystem.orders.OrderMessageProcessor;
import co.nz.pizzatent.deliverymanagementsystem.pubsub.MessageListener;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.core.GoogleCredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.pubsub.v1.SubscriptionName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class Application {



	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
    public Set<OrderMessage> ordersReceived(){
	    return ConcurrentHashMap.newKeySet();
    }


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


}
