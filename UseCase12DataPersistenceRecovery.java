import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class PersistedBooking implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String reservationId;
    private final String guestName;
    private final String roomType;
    private final String roomId;

    public PersistedBooking(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return reservationId
                + " | Guest: " + guestName
                + " | Room Type: " + roomType
                + " | Room ID: " + roomId;
    }
}

class SystemSnapshot implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<PersistedBooking> bookings;
    private final Map<String, Integer> inventory;

    public SystemSnapshot(List<PersistedBooking> bookings, Map<String, Integer> inventory) {
        this.bookings = bookings;
        this.inventory = inventory;
    }

    public List<PersistedBooking> getBookings() {
        return bookings;
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }
}

class BookingStore {
    private final List<PersistedBooking> bookings = new ArrayList<>();

    public void addBooking(PersistedBooking booking) {
        bookings.add(booking);
    }

    public List<PersistedBooking> snapshot() {
        return new ArrayList<>(bookings);
    }

    public void restore(List<PersistedBooking> restoredBookings) {
        bookings.clear();
        bookings.addAll(restoredBookings);
    }

    public void displayBookings() {
        System.out.println("\nBooking History:");
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (PersistedBooking booking : bookings) {
            System.out.println(booking);
        }
    }
}

class InventoryStore {
    private final Map<String, Integer> inventory = new LinkedHashMap<>();

    public InventoryStore() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public String allocateRoom(String roomType) {
        Integer available = inventory.get(roomType);
        if (available == null || available == 0) {
            return null;
        }

        inventory.put(roomType, available - 1);
        return generateRoomId(roomType, available);
    }

    private String generateRoomId(String roomType, int availableBeforeAllocation) {
        if ("Single Room".equals(roomType)) {
            return availableBeforeAllocation == 2 ? "S101" : "S102";
        }
        if ("Double Room".equals(roomType)) {
            return "D201";
        }
        return "SU301";
    }

    public Map<String, Integer> snapshot() {
        return new LinkedHashMap<>(inventory);
    }

    public void restore(Map<String, Integer> restoredInventory) {
        inventory.clear();
        inventory.putAll(restoredInventory);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

class PersistenceService {
    private final String fileName;

    public PersistenceService(String fileName) {
        this.fileName = fileName;
    }

    public void save(SystemSnapshot snapshot) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(snapshot);
            System.out.println("\nSystem state saved to " + fileName);
        } catch (IOException e) {
            System.out.println("\nSave failed: " + e.getMessage());
        }
    }

    public SystemSnapshot loadOrDefault() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            Object restoredObject = inputStream.readObject();
            if (restoredObject instanceof SystemSnapshot) {
                System.out.println("\nSystem state recovered from " + fileName);
                return (SystemSnapshot) restoredObject;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\nRecovery fallback activated: " + e.getMessage());
        }

        return new SystemSnapshot(new ArrayList<>(), defaultInventory());
    }

    private Map<String, Integer> defaultInventory() {
        Map<String, Integer> inventory = new LinkedHashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
        return inventory;
    }
}

public class UseCase12DataPersistenceRecovery {
    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println(" Book My Stay App - Data Persistence & Recovery ");
        System.out.println("====================================================");

        String persistenceFile = "usecase12-system-state.ser";
        PersistenceService persistenceService = new PersistenceService(persistenceFile);

        InventoryStore liveInventory = new InventoryStore();
        BookingStore liveBookings = new BookingStore();

        createBooking(liveBookings, liveInventory, "RES301", "Arun", "Single Room");
        createBooking(liveBookings, liveInventory, "RES302", "Priya", "Double Room");

        liveBookings.displayBookings();
        liveInventory.displayInventory();

        SystemSnapshot snapshot = new SystemSnapshot(liveBookings.snapshot(), liveInventory.snapshot());
        persistenceService.save(snapshot);

        System.out.println("\nSimulating system restart...");

        BookingStore recoveredBookings = new BookingStore();
        InventoryStore recoveredInventory = new InventoryStore();
        SystemSnapshot recoveredSnapshot = persistenceService.loadOrDefault();

        recoveredBookings.restore(recoveredSnapshot.getBookings());
        recoveredInventory.restore(recoveredSnapshot.getInventory());

        recoveredBookings.displayBookings();
        recoveredInventory.displayInventory();

        System.out.println("\nSystem resumed safely with recovered state.");
    }

    private static void createBooking(
            BookingStore bookings,
            InventoryStore inventory,
            String reservationId,
            String guestName,
            String roomType) {
        String roomId = inventory.allocateRoom(roomType);

        if (roomId == null) {
            System.out.println("\nBooking Failed for " + guestName + ": no " + roomType + " available.");
            return;
        }

        PersistedBooking booking = new PersistedBooking(reservationId, guestName, roomType, roomId);
        bookings.addBooking(booking);

        System.out.println("\nBooking Confirmed:");
        System.out.println(booking);
    }
}
