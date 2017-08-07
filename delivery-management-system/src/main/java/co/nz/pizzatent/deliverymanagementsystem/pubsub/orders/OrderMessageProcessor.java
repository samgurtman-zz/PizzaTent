package co.nz.pizzatent.deliverymanagementsystem.pubsub.orders;

import co.nz.pizzatent.deliverymanagementsystem.pubsub.TypedConsumer;
import co.nz.pizzatent.deliverymanagementsystem.service.orders.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * Processes incoming pubsub order messages
 */
@Component
public class OrderMessageProcessor implements TypedConsumer<OrderMessage> {

    private OrdersService ordersService;

    @Autowired
    public OrderMessageProcessor(OrdersService ordersService){
        this.ordersService = ordersService;
    }

    @Override
    public Class<OrderMessage> getType() {
        return OrderMessage.class;
    }

    @Override
    public void accept(OrderMessage orderMessage) {
        Date timeReady = new Date(orderMessage.getTimeReady() *1000);
        ordersService.addOrder(
                orderMessage.getStoreId(),
                orderMessage.getOrderId(),
                orderMessage.getDeliveryLocation(),
                orderMessage.getSize(),
                timeReady
        );
    }
}
