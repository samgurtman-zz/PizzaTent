package co.nz.pizzatent.deliverymanagementsystem.api;


import co.nz.pizzatent.deliverymanagementsystem.orders.OrderMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Set;

@Path("/orders")
public class OrdersResource {

    private final Set<OrderMessage> ordersReceived;

    @Autowired
    public OrdersResource(Set<OrderMessage> ordersReceived){
        this.ordersReceived = ordersReceived;
    }

    @GET
    @Produces("application/json")
    public Set<OrderMessage> listReceivedOrders() {
        return ordersReceived;
    }



}
