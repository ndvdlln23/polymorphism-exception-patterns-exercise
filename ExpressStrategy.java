public class ExpressStrategy implements DeliveryStrategy {
    @Override
    public double calculateFee(double baseFee) {
        return baseFee * 1.5;
    }

    @Override
    public String getServiceName() {
        return "Express";
    }
}