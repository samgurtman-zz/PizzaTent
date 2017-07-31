package co.nz.pizzatent.drivermanagementsystem.Orders;

import co.nz.pizzatent.drivermanagementsystem.TypedConsumer;


public class OrderMessageProcessor implements TypedConsumer<OrderMessage> {

    @Override
    public Class<OrderMessage> getType() {
        return OrderMessage.class;
    }

    @Override
    public void accept(OrderMessage orderMessage) {

    }
}
