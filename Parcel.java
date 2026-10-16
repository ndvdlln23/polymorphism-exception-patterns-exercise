public abstract class Parcel {
    private String trackingNumber;
    private String receiverName;
    private double actualWeight;
    private DeliveryStrategy deliveryStrategy;
    private String status;

    public Parcel(String trackingNumber, String receiverName, double actualWeight, DeliveryStrategy deliveryStrategy) {
        this.trackingNumber = trackingNumber;
        this.receiverName = receiverName;
        this.actualWeight = actualWeight;
        this.deliveryStrategy = deliveryStrategy;
        this.status = "Pending";
    }

    public String getTrackingNumber() { return trackingNumber; }
    public String getReceiverName() { return receiverName; }
    public double getActualWeight() { return actualWeight; }
    public String getStatus() { return status; }
    public DeliveryStrategy getDeliveryStrategy() { return deliveryStrategy; }

    public void setStatus(String status) { this.status = status; }
    public void setDeliveryStrategy(DeliveryStrategy deliveryStrategy) { this.deliveryStrategy = deliveryStrategy; }

    public abstract double getChargeableWeight();
    public abstract double calculateBaseFee();
    public abstract String getType();

    public double getTotalFee() {
        return deliveryStrategy.calculateFee(calculateBaseFee());
    }
}