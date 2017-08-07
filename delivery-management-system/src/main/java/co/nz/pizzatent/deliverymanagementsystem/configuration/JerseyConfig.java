package co.nz.pizzatent.deliverymanagementsystem.configuration;

import co.nz.pizzatent.deliverymanagementsystem.api.OrdersResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(OrdersResource.class);

    }

}