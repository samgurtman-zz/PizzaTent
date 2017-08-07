package co.nz.pizzatent.deliverymanagementsystem.domain.order;

import co.nz.pizzatent.deliverymanagementsystem.domain.store.StoreEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(indexes = { @Index(name = "index_ready_time", columnList = "readyTime") })
public class OrderEntity {

    @JsonIgnore
    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false, unique = true)
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, name="storeId")
    private StoreEntity originStore;

    @Column(nullable = false)
    private Double delvieryLatitude;

    @Column(nullable = false)
    private Double deliveryLongitude;


    @Column(nullable = false)
    private Integer size;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date readyTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StoreEntity getOriginStore() {
        return originStore;
    }

    public void setOriginStore(StoreEntity originStore) {
        this.originStore = originStore;
    }

    public Double getDelvieryLatitude() {
        return delvieryLatitude;
    }

    public void setDelvieryLatitude(Double delvieryLatitude) {
        this.delvieryLatitude = delvieryLatitude;
    }

    public Double getDeliveryLongitude() {
        return deliveryLongitude;
    }

    public void setDeliveryLongitude(Double deliveryLongitude) {
        this.deliveryLongitude = deliveryLongitude;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Date getReadyTime() {
        return readyTime;
    }

    public void setReadyTime(Date readyTime) {
        this.readyTime = readyTime;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
