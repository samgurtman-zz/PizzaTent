package co.nz.pizzatent.deliverymanagementsystem.domain.store;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface StoreRepository extends PagingAndSortingRepository<StoreEntity, Integer> {
    StoreEntity findByStoreId(String storeId);
}
