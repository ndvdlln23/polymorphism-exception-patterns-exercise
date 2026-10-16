public class Fragile extends Box {
    private String handlingNote;

    public Fragile(String trackingNumber, String receiverName, double actualWeight, DeliveryStrategy deliveryStrategy, int length, int width, int height, String handlingNote) {
        super(trackingNumber, receiverName, actualWeight, deliveryStrategy, length, width, height);
        this.handlingNote = handlingNote;
    }

    @Override
    public double calculateBaseFee() {
        return super.calculateBaseFee() + 100.0;
    }

    @Override
    public String getType() {
        return "Fragile";
    }
}