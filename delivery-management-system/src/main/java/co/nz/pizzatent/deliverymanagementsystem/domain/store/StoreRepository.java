package co.nz.pizzatent.deliverymanagementsystem.domain.store;

import org.springframework.data.repository.CrudRepository;

public interface StoreRepository extends CrudRepository<StoreEntity, Integer> {
    StoreEntity findByStoreId(String storeId);
}
