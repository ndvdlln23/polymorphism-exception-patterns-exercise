import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ParcelManager manager = ParcelManager.getInstance();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Parcel Delivery System ---");
            System.out.println("1. Add Parcel");
            System.out.println("2. View All Parcels");
            System.out.println("3. Change Delivery Service");
            System.out.println("4. Mark as Delivered");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1": addParcel(); break;
                case "2": viewParcels(); break;
                case "3": changeDeliveryService(); break;
                case "4": markAsDelivered(); break;
                case "5":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addParcel() {
        int type = getIntInput("Select Type (1: Document, 2: Box, 3: Fragile): ", 1, 3);
        String trackingNo = getTrackingNumberInput();
        String receiver = getStringInput("Receiver Name: ");
        
        double maxWeight = (type == 1) ? 0.5 : ((type == 2) ? 50.0 : 30.0);
        double actualWeight = getDoubleInput("Actual Weight (kg): ", 0.001, maxWeight);

        int pages = 0, length = 0, width = 0, height = 0;
        String note = "";

        if (type == 1) {
            pages = getIntInput("Number of Pages (1-100): ", 1, 100);
        } else {
            length = getIntInput("Length (cm, max 200): ", 1, 200);
            width = getIntInput("Width (cm, max 200): ", 1, 200);
            height = getIntInput("Height (cm, max 200): ", 1, 200);
            if (type == 3) {
                note = getStringInput("Handling Note: ");
            }
        }

        DeliveryStrategy strategy = getDeliveryStrategyInput(type == 1 ? actualWeight : Math.ceil(Math.max(actualWeight, (length * width * height) / 3500.0)));
        if (strategy == null) return; // User cancelled due to Same Day restrictions

        Parcel parcel = null;
        if (type == 1) {
            parcel = new Document(trackingNo, receiver, actualWeight, strategy, pages);
        } else if (type == 2) {
            parcel = new Box(trackingNo, receiver, actualWeight, strategy, length, width, height);
        } else if (type == 3) {
            parcel = new Fragile(trackingNo, receiver, actualWeight, strategy, length, width, height, note);
        }

        manager.addParcel(parcel);
        System.out.printf("Parcel added successfully! Delivery fee: Php %.2f\n", parcel.getTotalFee());
    }

    private static void viewParcels() {
        if (manager.getAllParcels().isEmpty()) {
            System.out.println("No parcels to display.");
            return;
        }
        System.out.printf("%-10s | %-10s | %-15s | %-15s | %-10s | %-10s | %-10s\n", 
            "Tracking", "Type", "Receiver", "Chg. Weight(kg)", "Service", "Fee(Php)", "Status");
        System.out.println("------------------------------------------------------------------------------------------");
        for (Parcel p : manager.getAllParcels()) {
            System.out.printf("%-10s | %-10s | %-15s | %-15.2f | %-10s | %-10.2f | %-10s\n",
                p.getTrackingNumber(), p.getType(), p.getReceiverName(), p.getChargeableWeight(), 
                p.getDeliveryStrategy().getServiceName(), p.getTotalFee(), p.getStatus());
        }
    }

    private static void changeDeliveryService() {
        System.out.print("Enter Tracking Number: ");
        String trackingNo = scanner.nextLine().trim().toUpperCase();
        Parcel p = manager.getParcel(trackingNo);

        if (p == null) {
            System.out.println("Parcel not found!");
            return;
        }
        if ("Delivered".equalsIgnoreCase(p.getStatus())) {
            System.out.println("Parcel already delivered!");
            return;
        }

        DeliveryStrategy newStrategy = getDeliveryStrategyInput(p.getChargeableWeight());
        if (newStrategy != null) {
            p.setDeliveryStrategy(newStrategy);
            System.out.printf("Delivery service changed! New fee: Php %.2f\n", p.getTotalFee());
        }
    }

    private static void markAsDelivered() {
        System.out.print("Enter Tracking Number: ");
        String trackingNo = scanner.nextLine().trim().toUpperCase();
        Parcel p = manager.getParcel(trackingNo);

        if (p == null) {
            System.out.println("Parcel not found!");
            return;
        }
        if ("Delivered".equalsIgnoreCase(p.getStatus())) {
            System.out.println("Parcel was already delivered.");
            return;
        }
        p.setStatus("Delivered");
        System.out.println("Parcel delivered successfully!");
    }

    // --- Validation Helper Methods ---

    private static String getTrackingNumberInput() {
        while (true) {
            System.out.print("Tracking Number (ABC####): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (!input.matches("^ABC\\d{4}$")) {
                System.out.println("Invalid format. Must be 'ABC' followed by 4 digits.");
                continue;
            }
            if (manager.trackingNumberExists(input)) {
                System.out.println("Duplicate tracking number! Try another.");
                continue;
            }
            return input;
        }
    }

    private static String getStringInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty.");
            } else {
                return input;
            }
        }
    }

    private static int getIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val < min || val > max) {
                    System.out.println("Value must be between " + min + " and " + max + ".");
                } else {
                    return val;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter an integer.");
            }
        }
    }

    private static double getDoubleInput(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(scanner.nextLine().trim());
                if (val < min || val > max) {
                    System.out.println("Value must be between " + min + " and " + max + ".");
                } else {
                    return val;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid decimal number.");
            }
        }
    }

    private static DeliveryStrategy getDeliveryStrategyInput(double chargeableWeight) {
        while (true) {
            int svc = getIntInput("Service (1: Standard, 2: Express, 3: Same Day, 0: Cancel): ", 0, 3);
            if (svc == 0) return null;
            if (svc == 3 && chargeableWeight > 10.0) {
                System.out.println("Same Day delivery is only for parcels up to 10 kg!");
                continue;
            }
            if (svc == 1) return new StandardStrategy();
            if (svc == 2) return new ExpressStrategy();
            if (svc == 3) return new SameDayStrategy();
        }
    }
}