package co.nz.pizzatent.deliverymanagementsystem.domain.order;

import co.nz.pizzatent.deliverymanagementsystem.domain.store.StoreEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends CrudRepository<OrderEntity, Integer> {
    List<OrderEntity> findAllByOriginStoreAndReadyTimeAfterAndOrderStatusOrderByReadyTime(StoreEntity store, Date readyTime, OrderStatus orderStatus, Pageable pageable);
    List<OrderEntity> findAllByOrderStatus(OrderStatus orderStatus);
    OrderEntity findByOrderId(String orderId);
}
