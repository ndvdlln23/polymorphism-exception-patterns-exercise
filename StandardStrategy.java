public class StandardStrategy implements DeliveryStrategy {
    @Override
    public double calculateFee(double baseFee) {
        return baseFee;
    }

    @Override
    public String getServiceName() {
        return "Standard";
    }
}