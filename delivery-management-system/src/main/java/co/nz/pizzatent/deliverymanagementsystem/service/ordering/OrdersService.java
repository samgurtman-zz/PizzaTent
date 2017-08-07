package co.nz.pizzatent.deliverymanagementsystem.service.ordering;

import co.nz.pizzatent.deliverymanagementsystem.domain.order.OrderEntity;
import co.nz.pizzatent.deliverymanagementsystem.domain.order.OrderRepository;
import co.nz.pizzatent.deliverymanagementsystem.domain.order.OrderStatus;
import co.nz.pizzatent.deliverymanagementsystem.domain.store.StoreEntity;
import co.nz.pizzatent.deliverymanagementsystem.domain.store.StoreRepository;
import co.nz.pizzatent.deliverymanagementsystem.service.routing.RoutingException;
import co.nz.pizzatent.deliverymanagementsystem.service.routing.RoutingService;
import co.nz.pizzatent.deliverymanagementsystem.util.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class OrdersService {

    private static final int MAX_DELIVERIES_PER_TRIP = 5;

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

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void completeOrder(String orderId){
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        if(orderEntity == null){
            throw new IllegalArgumentException("Order id does not exist!");
        }
        orderEntity.setOrderStatus(OrderStatus.DELIVERED);
        orderRepository.save(orderEntity);
    }

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


    public List<OrderEntity> getPendingOrders(String storeId){
        StoreEntity store = storeRepository.findByStoreId(storeId);
        if(store == null){
            throw new IllegalArgumentException("Store id specified does not exist!");
        }
        return orderRepository.findAllByOrderStatus(OrderStatus.AT_STORE);


    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<Location> createNextTrip(String storeId, int maxSize) throws RoutingException {
        StoreEntity store = storeRepository.findByStoreId(storeId);
        if(store == null){
            throw new IllegalArgumentException("Store id specified does not exist!");
        }
        PageRequest maxEntities = new PageRequest(0, MAX_DELIVERIES_PER_TRIP);
        List<OrderEntity> orders = orderRepository.findAllByOriginStoreAndReadyTimeAfterAndOrderStatusOrderByReadyTime(
                store, new Date(), OrderStatus.AT_STORE, maxEntities
        );

        int sizeUsed = 0;
        Set<Location> locationsOnTrip = new HashSet<>();
        for(OrderEntity order : orders){
            sizeUsed += order.getSize();
            if(sizeUsed < maxSize){
                locationsOnTrip.add(new Location(order.getDelvieryLatitude(), order.getDeliveryLongitude()));
                order.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
                orderRepository.save(order);
            }else{
                break;
            }
        }
        Location storeLocation = new Location(store.getLatitude(), store.getLongitude());
        return routingService.getRoute(storeLocation, locationsOnTrip);
    }
}
