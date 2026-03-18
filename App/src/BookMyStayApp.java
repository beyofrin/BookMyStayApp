import java.util.LinkedList;
import java.util.Queue;

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

    public void displayReservation() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// -------------------- Booking Request Queue --------------------

class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request to queue
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all requests (without removing)
    public void displayQueue() {
        System.out.println("\nCurrent Booking Requests (FIFO Order):");

        for (Reservation r : queue) {
            r.displayReservation();
        }
    }

    // Peek next request (no removal)
    public Reservation peekNextRequest() {
        return queue.peek();
    }
}

// -------------------- Main Class --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("   Book My Stay App - Version 5.0");
        System.out.println("======================================");

        // Initialize booking queue
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Simulate incoming booking requests
        requestQueue.addRequest(new Reservation("Arun", "Single Room"));
        requestQueue.addRequest(new Reservation("Priya", "Double Room"));
        requestQueue.addRequest(new Reservation("Rahul", "Suite Room"));

        // Display queue (FIFO order)
        requestQueue.displayQueue();

        System.out.println("\nNext request to process (peek):");
        Reservation next = requestQueue.peekNextRequest();
        if (next != null) {
            next.displayReservation();
        }

        System.out.println("======================================");
        System.out.println("All requests stored successfully!");
        System.out.println("No booking or inventory update performed.");
    }
}