package co.nz.pizzatent.deliverymanagementsystem.domain.order;

import co.nz.pizzatent.deliverymanagementsystem.domain.store.StoreEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends PagingAndSortingRepository<OrderEntity, Integer> {
    List<OrderEntity> findAllByOriginStoreAndReadyTimeBeforeAndOrderStatusOrderByReadyTime(StoreEntity store, Date readyTime, OrderStatus orderStatus);

    OrderEntity findByOrderId(String orderId);
}
