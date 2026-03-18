import java.util.HashMap;
import java.util.Map;

// Inventory Management Class
class RoomInventory {

    // HashMap to store room type and availability
    private Map<String, Integer> inventory;

    // Constructor to initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Initialize room availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Get availability of a specific room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability (increase or decrease)
    public void updateAvailability(String roomType, int count) {
        if (inventory.containsKey(roomType)) {
            int current = inventory.get(roomType);
            inventory.put(roomType, current + count);
        } else {
            System.out.println("Room type not found!");
        }
    }

    // Display full inventory
    public void displayInventory() {
        System.out.println("Current Room Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("   Book My Stay App - Version 3.0");
        System.out.println("======================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        System.out.println("--------------------------------------");

        // Simulate booking (reduce availability)
        System.out.println("Booking 1 Single Room...");
        inventory.updateAvailability("Single Room", -1);

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("======================================");
        System.out.println("Inventory updated successfully!");
    }
}