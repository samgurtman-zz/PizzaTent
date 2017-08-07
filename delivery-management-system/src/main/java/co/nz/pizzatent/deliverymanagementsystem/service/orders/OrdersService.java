package co.nz.pizzatent.deliverymanagementsystem.service.orders;

import co.nz.pizzatent.deliverymanagementsystem.domain.order.OrderEntity;
import co.nz.pizzatent.deliverymanagementsystem.domain.order.OrderRepository;
import co.nz.pizzatent.deliverymanagementsystem.domain.order.OrderStatus;
import co.nz.pizzatent.deliverymanagementsystem.domain.store.StoreEntity;
import co.nz.pizzatent.deliverymanagementsystem.domain.store.StoreRepository;
import co.nz.pizzatent.deliverymanagementsystem.service.routes.RoutingException;
import co.nz.pizzatent.deliverymanagementsystem.service.routes.RoutingService;
import co.nz.pizzatent.deliverymanagementsystem.util.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service to manipulate orders
 */
@Component
public class OrdersService {


    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final RoutingService routingService;

    @Autowired
    public OrdersService(OrderRepository orderRepository, StoreRepository storeRepository, RoutingService routingService){
        this.routingService = routingService;
        this.orderRepository = orderRepository;
        this.storeRepository = storeRepository;
    }


    public OrderEntity getOrder(String orderId){
        return orderRepository.findByOrderId(orderId);
    }

    /**
     * Mark an order as delivered
     * @param orderId id of order
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void completeOrder(String orderId){
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        if(orderEntity == null){
            throw new IllegalArgumentException("Order id does not exist!");
        }
        if(orderEntity.getOrderStatus() == OrderStatus.AT_STORE || orderEntity.getOrderStatus() == OrderStatus.OUT_FOR_DELIVERY) {
            orderEntity.setOrderStatus(OrderStatus.DELIVERED);
            orderRepository.save(orderEntity);
        }else{
            throw new IllegalArgumentException("Order specified can not be completed, has status " + orderEntity.getOrderStatus());
        }
    }

    /**
     * Add pending order to store
     * @param storeId id of store
     * @param orderId id of order
     * @param deliveryLocation location where order should be delivered
     * @param size standard size of order
     * @param timeReady when the order is ready
     */
    public void addOrder(String storeId, String orderId, Location deliveryLocation, int size, Date timeReady){
        StoreEntity store = storeRepository.findByStoreId(storeId);
        if(store == null){
            throw new IllegalArgumentException("Store id specified does not exist!");
        }
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setDeliveryLongitude(deliveryLocation.getLongitude());
        orderEntity.setDelvieryLatitude(deliveryLocation.getLatitude());
        orderEntity.setOriginStore(store);
        orderEntity.setReadyTime(timeReady);
        orderEntity.setSize(size);
        orderEntity.setOrderStatus(OrderStatus.AT_STORE);
        orderEntity.setOrderId(orderId);
        orderRepository.save(orderEntity);
    }


    /**
     * Get pending orders for given store
     * @param storeId id of store
     * @return all orders with status {@link OrderStatus#AT_STORE}
     */
    public List<OrderEntity> getPendingOrders(String storeId){
        StoreEntity store = storeRepository.findByStoreId(storeId);
        if(store == null){
            throw new IllegalArgumentException("Store id specified does not exist!");
        }
        return orderRepository.findAllByOriginStoreAndReadyTimeBeforeAndOrderStatusOrderByReadyTime(store, new Date(), OrderStatus.AT_STORE);


    }

    /**
     * Generate the next required delivery trip for a given store
     * @param storeId id of store
     * @param maxSize total standard size units the delivery vehicle can accommodate
     * @return Locations in order they should be visted or null if no pending orders could be packed into route
     * @throws RoutingException
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<Location> createNextTrip(String storeId, int maxSize) throws RoutingException {
        StoreEntity store = storeRepository.findByStoreId(storeId);
        if(store == null){
            throw new IllegalArgumentException("Store id specified does not exist!");
        }
        List<OrderEntity> orders = orderRepository.findAllByOriginStoreAndReadyTimeBeforeAndOrderStatusOrderByReadyTime(
                store, new Date(), OrderStatus.AT_STORE);


        int sizeUsed = 0;
        Set<Location> locationsOnTrip = new HashSet<>();
        for(OrderEntity order : orders){
            if(sizeUsed + order.getSize() <= maxSize){
                locationsOnTrip.add(new Location(order.getDelvieryLatitude(), order.getDeliveryLongitude()));
                order.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
                orderRepository.save(order);
                sizeUsed += order.getSize();
            }
        }
        if(locationsOnTrip.isEmpty()){
            return null;
        }
        Location storeLocation = new Location(store.getLatitude(), store.getLongitude());
        return routingService.getRoute(storeLocation, locationsOnTrip);
    }

}
