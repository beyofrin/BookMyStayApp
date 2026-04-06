import java.util.*;

// -------------------- Custom Exception --------------------

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// -------------------- Reservation Class --------------------

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// -------------------- Inventory Class --------------------

class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 0); // unavailable for testing
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, -1);
    }

    public void reduceRoom(String roomType) throws InvalidBookingException {
        int available = getAvailability(roomType);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for " + roomType);
        }

        inventory.put(roomType, available - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }
}

// -------------------- Validator Class --------------------

class BookingValidator {

    private static final List<String> VALID_ROOMS =
            Arrays.asList("Single Room", "Double Room", "Suite Room");

    public static void validate(Reservation r, RoomInventory inventory)
            throws InvalidBookingException {

        if (r.getGuestName() == null || r.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty!");
        }

        if (!VALID_ROOMS.contains(r.getRoomType())) {
            throw new InvalidBookingException("Invalid room type selected!");
        }

        int available = inventory.getAvailability(r.getRoomType());

        if (available == -1) {
            throw new InvalidBookingException("Room type does not exist!");
        }

        if (available == 0) {
            throw new InvalidBookingException("Selected room is fully booked!");
        }
    }
}

// -------------------- Booking Service --------------------

class BookingService {

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation r) {

        try {
            // Step 1: Validate
            BookingValidator.validate(r, inventory);

            // Step 2: Update system
            inventory.reduceRoom(r.getRoomType());

            // Step 3: Success message
            System.out.println("\nBooking Successful!");
            System.out.println("Guest: " + r.getGuestName());
            System.out.println("Room: " + r.getRoomType());

        } catch (InvalidBookingException e) {

            // Graceful failure
            System.out.println("\nBooking Failed: " + e.getMessage());
        }
    }
}

// -------------------- Main Class --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println(" Book My Stay App - Error Handling ");
        System.out.println("======================================");

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        // Test cases

        service.processBooking(new Reservation("Arun", "Single Room"));   // valid
        service.processBooking(new Reservation("Priya", "Luxury Room")); // invalid room
        service.processBooking(new Reservation("", "Double Room"));      // empty name
        service.processBooking(new Reservation("Rahul", "Suite Room"));  // no availability

        inventory.displayInventory();

        System.out.println("\nSystem continues running safely!");
    }
}