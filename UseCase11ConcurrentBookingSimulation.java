import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

class BookingRequest {
    private final String reservationId;
    private final String guestName;
    private final String roomType;

    public BookingRequest(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
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
}

class ConfirmedBooking {
    private final String reservationId;
    private final String guestName;
    private final String roomType;
    private final String roomId;
    private final String processedBy;

    public ConfirmedBooking(String reservationId, String guestName, String roomType, String roomId, String processedBy) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.processedBy = processedBy;
    }

    @Override
    public String toString() {
        return reservationId
                + " | Guest: " + guestName
                + " | Room Type: " + roomType
                + " | Room ID: " + roomId
                + " | Processed By: " + processedBy;
    }
}

class SharedBookingQueue {
    private final Queue<BookingRequest> requests = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        requests.offer(request);
        System.out.println("Queued request: " + request.getReservationId() + " for " + request.getGuestName());
    }

    public synchronized BookingRequest getNextRequest() {
        return requests.poll();
    }
}

class ThreadSafeInventory {
    private final Map<String, List<String>> availableRooms = new LinkedHashMap<>();

    public ThreadSafeInventory() {
        availableRooms.put("Single Room", new ArrayList<>(Arrays.asList("S101", "S102")));
        availableRooms.put("Double Room", new ArrayList<>(Arrays.asList("D201")));
        availableRooms.put("Suite Room", new ArrayList<>(Arrays.asList("SU301")));
    }

    public synchronized String allocateRoom(String roomType) {
        List<String> rooms = availableRooms.get(roomType);
        if (rooms == null || rooms.isEmpty()) {
            return null;
        }
        return rooms.remove(0);
    }

    public synchronized void displayInventory() {
        System.out.println("\nRemaining Inventory:");
        for (Map.Entry<String, List<String>> entry : availableRooms.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue().size() + " available " + entry.getValue());
        }
    }
}

class ConcurrentBookingProcessor {
    private final SharedBookingQueue bookingQueue;
    private final ThreadSafeInventory inventory;
    private final List<ConfirmedBooking> confirmedBookings = new ArrayList<>();

    public ConcurrentBookingProcessor(SharedBookingQueue bookingQueue, ThreadSafeInventory inventory) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
    }

    public void processAllRequests(int workerCount) throws InterruptedException {
        List<Thread> workers = new ArrayList<>();

        for (int i = 1; i <= workerCount; i++) {
            Thread worker = new Thread(this::processRequests, "Worker-" + i);
            workers.add(worker);
            worker.start();
        }

        for (Thread worker : workers) {
            worker.join();
        }
    }

    private void processRequests() {
        while (true) {
            BookingRequest request = bookingQueue.getNextRequest();
            if (request == null) {
                return;
            }

            String roomId = inventory.allocateRoom(request.getRoomType());

            if (roomId == null) {
                System.out.println(Thread.currentThread().getName()
                        + " rejected " + request.getReservationId()
                        + " because no " + request.getRoomType() + " was available.");
                continue;
            }

            ConfirmedBooking booking = new ConfirmedBooking(
                    request.getReservationId(),
                    request.getGuestName(),
                    request.getRoomType(),
                    roomId,
                    Thread.currentThread().getName());

            synchronized (confirmedBookings) {
                confirmedBookings.add(booking);
            }

            System.out.println(Thread.currentThread().getName()
                    + " confirmed " + request.getReservationId()
                    + " with room " + roomId);
        }
    }

    public void displayConfirmedBookings() {
        System.out.println("\nConfirmed Bookings:");
        synchronized (confirmedBookings) {
            for (ConfirmedBooking booking : confirmedBookings) {
                System.out.println(booking);
            }
        }
    }
}

public class UseCase11ConcurrentBookingSimulation {
    public static void main(String[] args) {
        System.out.println("======================================================");
        System.out.println(" Book My Stay App - Concurrent Booking Simulation ");
        System.out.println("======================================================");

        SharedBookingQueue bookingQueue = new SharedBookingQueue();
        ThreadSafeInventory inventory = new ThreadSafeInventory();
        ConcurrentBookingProcessor processor = new ConcurrentBookingProcessor(bookingQueue, inventory);

        bookingQueue.addRequest(new BookingRequest("RES201", "Arun", "Single Room"));
        bookingQueue.addRequest(new BookingRequest("RES202", "Priya", "Double Room"));
        bookingQueue.addRequest(new BookingRequest("RES203", "Rahul", "Single Room"));
        bookingQueue.addRequest(new BookingRequest("RES204", "Anita", "Suite Room"));
        bookingQueue.addRequest(new BookingRequest("RES205", "Kiran", "Single Room"));

        try {
            processor.processAllRequests(3);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Processing interrupted: " + e.getMessage());
        }

        processor.displayConfirmedBookings();
        inventory.displayInventory();

        System.out.println("\nConcurrent processing completed without double allocation.");
    }
}
