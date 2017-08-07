package co.nz.pizzatent.deliverymanagementsystem.api.resources;

import co.nz.pizzatent.deliverymanagementsystem.domain.order.OrderEntity;
import co.nz.pizzatent.deliverymanagementsystem.service.orders.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Component
@Path("/orders/${id}")
public class OrdersResource {

    private final OrdersService ordersService;

    @Autowired
    public OrdersResource(OrdersService ordersService){
        this.ordersService = ordersService;
    }

    @GET
    public OrderEntity getOrder(@PathParam("id") String orderId){
        return ordersService.getOrder(orderId);
    }

    @DELETE
    public void completeOrder(@PathParam("id") String orderId){
        ordersService.completeOrder(orderId);
    }


}
