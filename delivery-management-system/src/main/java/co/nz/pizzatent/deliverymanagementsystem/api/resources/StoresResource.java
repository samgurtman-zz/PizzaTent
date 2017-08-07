package co.nz.pizzatent.deliverymanagementsystem.api.resources;


import co.nz.pizzatent.deliverymanagementsystem.api.entities.TripRequest;
import co.nz.pizzatent.deliverymanagementsystem.domain.order.OrderEntity;
import co.nz.pizzatent.deliverymanagementsystem.service.orders.OrdersService;
import co.nz.pizzatent.deliverymanagementsystem.service.routes.RoutingException;
import co.nz.pizzatent.deliverymanagementsystem.util.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import java.util.List;

@Component
@Path("/stores/${id}")
public class StoresResource {

    private final OrdersService ordersService;

    @Autowired
    public StoresResource(OrdersService ordersService){
        this.ordersService = ordersService;
    }

    @Path("trips")
    @POST
    @Produces("application/json")
    public List<Location> generateNextTrip(@PathParam("id") String storeId, TripRequest tripRequest) throws RoutingException {
        return ordersService.createNextTrip(storeId, tripRequest.getMaxSize());
    }

    @Path("orders")
    @GET
    @Produces("application/json")
    public List<OrderEntity> getPendingOrders(@PathParam("id") String storeId) {
        return ordersService.getPendingOrders(storeId);
    }


}
