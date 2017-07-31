package co.nz.pizzatent.deliverymanagementsystem.orders;

import co.nz.pizzatent.deliverymanagementsystem.TypedConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
public class OrderMessageProcessor implements TypedConsumer<OrderMessage> {

    private Set<OrderMessage> ordersReceived;

    @Autowired
    public OrderMessageProcessor(Set<OrderMessage> ordersReceived){
        this.ordersReceived = ordersReceived;
    }

    @Override
    public Class<OrderMessage> getType() {
        return OrderMessage.class;
    }

    @Override
    public void accept(OrderMessage orderMessage) {
        this.ordersReceived.add(orderMessage);
    }
}
