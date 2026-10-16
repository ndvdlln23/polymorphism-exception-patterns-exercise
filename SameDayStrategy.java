public class SameDayStrategy implements DeliveryStrategy {
    @Override
    public double calculateFee(double baseFee) {
        return (baseFee * 2) + 100.0;
    }

    @Override
    public String getServiceName() {
        return "Same Day";
    }
}