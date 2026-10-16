public class Box extends Parcel {
    private int length;
    private int width;
    private int height;

    public Box(String trackingNumber, String receiverName, double actualWeight, DeliveryStrategy deliveryStrategy, int length, int width, int height) {
        super(trackingNumber, receiverName, actualWeight, deliveryStrategy);
        this.length = length;
        this.width = width;
        this.height = height;
    }

    @Override
    public double getChargeableWeight() {
        double volumetricWeight = (length * width * height) / 3500.0;
        return Math.ceil(Math.max(getActualWeight(), volumetricWeight));
    }

    @Override
    public double calculateBaseFee() {
        return 40.0 * getChargeableWeight();
    }

    @Override
    public String getType() {
        return "Box";
    }
}