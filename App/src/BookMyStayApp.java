import java.util.*;

// -------------------- Service Class --------------------

class Service {

    private String serviceName;
    private double cost;

    public Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    public void displayService() {
        System.out.println(serviceName + " - ₹" + cost);
    }
}

// -------------------- Add-On Service Manager --------------------

class AddOnServiceManager {

    // Map: ReservationID -> List of Services
    private Map<String, List<Service>> reservationServices = new HashMap<>();

    // Add service to reservation
    public void addService(String reservationId, Service service) {

        reservationServices.putIfAbsent(reservationId, new ArrayList<>());
        reservationServices.get(reservationId).add(service);

        System.out.println(service.getServiceName() +
                " added to reservation " + reservationId);
    }

    // Display services for reservation
    public void displayServices(String reservationId) {

        System.out.println("\nServices for Reservation: " + reservationId);

        List<Service> services = reservationServices.get(reservationId);

        if (services == null) {
            System.out.println("No services selected.");
            return;
        }

        for (Service s : services) {
            s.displayService();
        }
    }

    // Calculate total service cost
    public double calculateTotalCost(String reservationId) {

        double total = 0;

        List<Service> services = reservationServices.get(reservationId);

        if (services != null) {
            for (Service s : services) {
                total += s.getCost();
            }
        }

        return total;
    }
}

// -------------------- Main Class --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("   Book My Stay App - Add-On Services");
        System.out.println("======================================");

        AddOnServiceManager manager = new AddOnServiceManager();

        // Example reservation ID (from Use Case 6)
        String reservationId = "RES101";

        // Guest selects services
        manager.addService(reservationId, new Service("Breakfast", 500));
        manager.addService(reservationId, new Service("Airport Pickup", 800));
        manager.addService(reservationId, new Service("Spa Access", 1200));

        // Display services
        manager.displayServices(reservationId);

        // Calculate total cost
        double totalCost = manager.calculateTotalCost(reservationId);

        System.out.println("\nTotal Add-On Cost: ₹" + totalCost);

        System.out.println("======================================");
        System.out.println("Add-On services processed successfully!");
    }
}