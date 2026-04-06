import java.util.*;

class InvalidCancellationException extends Exception {
    public InvalidCancellationException(String message) {
        super(message);
    }
}

class BookingRecord {
    private final String reservationId;
    private final String guestName;
    private final String roomType;
    private final String roomId;
    private boolean cancelled;

    public BookingRecord(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.cancelled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void markCancelled() {
        cancelled = true;
    }

    public String getStatus() {
        return cancelled ? "Cancelled" : "Confirmed";
    }
}

class HotelInventory {
    private final Map<String, Integer> inventoryCount = new LinkedHashMap<>();
    private final Map<String, Deque<String>> availableRoomIds = new LinkedHashMap<>();

    public HotelInventory() {
        addRoomType("Single Room", "S101", "S102");
        addRoomType("Double Room", "D201");
        addRoomType("Suite Room", "SU301");
    }

    private void addRoomType(String roomType, String... roomIds) {
        Deque<String> rooms = new ArrayDeque<>();
        Collections.addAll(rooms, roomIds);
        availableRoomIds.put(roomType, rooms);
        inventoryCount.put(roomType, rooms.size());
    }

    public String allocateRoom(String roomType) {
        Deque<String> rooms = availableRoomIds.get(roomType);
        if (rooms == null || rooms.isEmpty()) {
            return null;
        }
        String allocatedRoomId = rooms.removeFirst();
        inventoryCount.put(roomType, inventoryCount.get(roomType) - 1);
        return allocatedRoomId;
    }

    public void restoreRoom(String roomType, String roomId) {
        Deque<String> rooms = availableRoomIds.get(roomType);
        if (rooms == null) {
            return;
        }
        rooms.addFirst(roomId);
        inventoryCount.put(roomType, inventoryCount.get(roomType) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventoryCount.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

class BookingHistory {
    private final Map<String, BookingRecord> bookings = new LinkedHashMap<>();

    public void addBooking(BookingRecord booking) {
        bookings.put(booking.getReservationId(), booking);
    }

    public BookingRecord getBooking(String reservationId) {
        return bookings.get(reservationId);
    }

    public void displayBookings() {
        System.out.println("\nBooking History:");
        for (BookingRecord booking : bookings.values()) {
            System.out.println(
                    booking.getReservationId()
                            + " | Guest: " + booking.getGuestName()
                            + " | Room Type: " + booking.getRoomType()
                            + " | Room ID: " + booking.getRoomId()
                            + " | Status: " + booking.getStatus());
        }
    }
}

class BookingService {
    private final HotelInventory inventory;
    private final BookingHistory history;

    public BookingService(HotelInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void confirmBooking(String reservationId, String guestName, String roomType) {
        String roomId = inventory.allocateRoom(roomType);

        if (roomId == null) {
            System.out.println("\nBooking Failed: No rooms available for " + roomType);
            return;
        }

        BookingRecord booking = new BookingRecord(reservationId, guestName, roomType, roomId);
        history.addBooking(booking);

        System.out.println("\nBooking Confirmed:");
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Guest: " + guestName);
        System.out.println("Room Type: " + roomType);
        System.out.println("Allocated Room ID: " + roomId);
    }
}

class CancellationService {
    private final HotelInventory inventory;
    private final BookingHistory history;
    private final Stack<String> rollbackRoomIds = new Stack<>();

    public CancellationService(HotelInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String reservationId) {
        try {
            BookingRecord booking = validateCancellation(reservationId);

            rollbackRoomIds.push(booking.getRoomId());
            inventory.restoreRoom(booking.getRoomType(), rollbackRoomIds.pop());
            booking.markCancelled();

            System.out.println("\nCancellation Successful:");
            System.out.println("Reservation ID: " + booking.getReservationId());
            System.out.println("Released Room ID: " + booking.getRoomId());
            System.out.println("Inventory restored for " + booking.getRoomType());
        } catch (InvalidCancellationException e) {
            System.out.println("\nCancellation Failed: " + e.getMessage());
        }
    }

    private BookingRecord validateCancellation(String reservationId) throws InvalidCancellationException {
        BookingRecord booking = history.getBooking(reservationId);

        if (booking == null) {
            throw new InvalidCancellationException("Reservation does not exist.");
        }

        if (booking.isCancelled()) {
            throw new InvalidCancellationException("Reservation is already cancelled.");
        }

        return booking;
    }
}

public class UseCase10BookingCancellation {
    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println(" Book My Stay App - Booking Cancellation ");
        System.out.println("==============================================");

        HotelInventory inventory = new HotelInventory();
        BookingHistory history = new BookingHistory();
        BookingService bookingService = new BookingService(inventory, history);
        CancellationService cancellationService = new CancellationService(inventory, history);

        bookingService.confirmBooking("RES101", "Arun", "Single Room");
        bookingService.confirmBooking("RES102", "Priya", "Double Room");

        history.displayBookings();
        inventory.displayInventory();

        cancellationService.cancelBooking("RES101");
        cancellationService.cancelBooking("RES999");
        cancellationService.cancelBooking("RES101");

        history.displayBookings();
        inventory.displayInventory();

        System.out.println("\nSystem state restored safely after cancellation attempts.");
    }
}
