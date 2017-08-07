package co.nz.pizzatent.deliverymanagementsystem.domain.store;

import co.nz.pizzatent.deliverymanagementsystem.domain.order.OrderEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface StoreRepository extends CrudRepository<StoreEntity, Integer> {
    StoreEntity findByStoreId(String storeId);
}
