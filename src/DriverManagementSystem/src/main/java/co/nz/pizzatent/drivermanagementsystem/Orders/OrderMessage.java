package co.nz.pizzatent.drivermanagementsystem.Orders;

public class OrderMessage {
    private String orderId;
    private String storeAddress;
    private String customerAddress;
    private int size;
    private long timeReady;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
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
