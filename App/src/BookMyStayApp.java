import java.util.*;

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

// -------------------- Booking Request Queue --------------------

class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
        System.out.println("Booking request added for " + r.getGuestName());
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// -------------------- Room Allocation Service --------------------

class RoomAllocationService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> roomTypeMap = new HashMap<>();
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomAllocationService() {

        // Initial inventory
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public void allocateRoom(Reservation reservation) {

        String type = reservation.getRoomType();

        if (!inventory.containsKey(type) || inventory.get(type) == 0) {
            System.out.println("No rooms available for " + type);
            return;
        }

        String roomId = generateRoomId(type);

        // Ensure uniqueness using Set
        if (!allocatedRoomIds.contains(roomId)) {

            allocatedRoomIds.add(roomId);

            roomTypeMap.putIfAbsent(type, new HashSet<>());
            roomTypeMap.get(type).add(roomId);

            // Update inventory
            inventory.put(type, inventory.get(type) - 1);

            System.out.println("Reservation Confirmed!");
            System.out.println("Guest: " + reservation.getGuestName());
            System.out.println("Room Type: " + type);
            System.out.println("Allocated Room ID: " + roomId);
            System.out.println();
        }
    }

    private String generateRoomId(String roomType) {

        String prefix = roomType.substring(0,2).toUpperCase();
        return prefix + (allocatedRoomIds.size() + 1);
    }
}

// -------------------- Main Class --------------------

public class BookMyStayApp{

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println(" Book My Stay App - Room Allocation ");
        System.out.println("======================================");

        BookingRequestQueue queue = new BookingRequestQueue();

        // Simulated booking requests
        queue.addRequest(new Reservation("Arun", "Single Room"));
        queue.addRequest(new Reservation("Priya", "Double Room"));
        queue.addRequest(new Reservation("Rahul", "Suite Room"));

        RoomAllocationService allocator = new RoomAllocationService();

        System.out.println("\nProcessing Booking Requests...\n");

        while (!queue.isEmpty()) {

            Reservation r = queue.getNextRequest();
            allocator.allocateRoom(r);
        }

        System.out.println("======================================");
        System.out.println("All bookings processed successfully!");
    }
}