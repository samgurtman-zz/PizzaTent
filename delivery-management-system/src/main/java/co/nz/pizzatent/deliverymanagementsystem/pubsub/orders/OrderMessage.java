package co.nz.pizzatent.deliverymanagementsystem.pubsub.orders;

import co.nz.pizzatent.deliverymanagementsystem.util.Location;

public class OrderMessage {
    private String orderId;
    private String storeId;
    private Location deliveryLocation;
    private int size;
    private long timeReady;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Location getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(Location deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTimeReady() {
        return timeReady;
    }

    public void setTimeReady(long timeReady) {
        this.timeReady = timeReady;
    }
}
