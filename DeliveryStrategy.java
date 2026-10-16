public interface DeliveryStrategy {
    double calculateFee(double baseFee);
    String getServiceName();
}