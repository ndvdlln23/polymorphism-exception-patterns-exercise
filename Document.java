public class Document extends Parcel {
    private int numberOfPages;

    public Document(String trackingNumber, String receiverName, double actualWeight, DeliveryStrategy deliveryStrategy, int numberOfPages) {
        super(trackingNumber, receiverName, actualWeight, deliveryStrategy);
        this.numberOfPages = numberOfPages;
    }

    @Override
    public double getChargeableWeight() {
        return getActualWeight();
    }

    @Override
    public double calculateBaseFee() {
        return 50.0;
    }

    @Override
    public String getType() {
        return "Document";
    }
}