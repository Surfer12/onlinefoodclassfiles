public class Order {
    private String id;
    private String deliveryAddress;
    private String status;

    public Order(String id, String deliveryAddress) {
        this.id = id;
        this.deliveryAddress = deliveryAddress;
        this.status = "PENDING";
    }

    public String getId() {
        return id;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
