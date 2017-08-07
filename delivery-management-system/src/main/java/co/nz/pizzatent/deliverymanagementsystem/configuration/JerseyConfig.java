package co.nz.pizzatent.deliverymanagementsystem.configuration;

import co.nz.pizzatent.deliverymanagementsystem.api.exceptionmappers.IllegalArgumentExceptionMapper;
import co.nz.pizzatent.deliverymanagementsystem.api.exceptionmappers.RoutingExceptionMapper;
import co.nz.pizzatent.deliverymanagementsystem.api.resources.OrdersResource;
import co.nz.pizzatent.deliverymanagementsystem.api.resources.StoresResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
      registerResources();
      registerExceptionMappers();
    }

    private void registerExceptionMappers(){
        register(IllegalArgumentExceptionMapper.class);
        register(RoutingExceptionMapper.class);
    }

    private void registerResources(){
        register(StoresResource.class);
        register(OrdersResource.class);
    }

}