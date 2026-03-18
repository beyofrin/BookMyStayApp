import java.util.*;

// -------------------- Reservation Class --------------------

class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
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

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType);
    }
}

// -------------------- Booking History --------------------

class BookingHistory {

    // List to store confirmed bookings in order
    private List<Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void addBooking(Reservation reservation) {
        history.add(reservation);
        System.out.println("Booking stored in history for " + reservation.getGuestName());
    }

    // Display booking history
    public void displayHistory() {

        System.out.println("\n------ Booking History ------");

        if (history.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : history) {
            r.displayReservation();
        }
    }

    public List<Reservation> getHistory() {
        return history;
    }
}

// -------------------- Booking Report Service --------------------

class BookingReportService {

    public void generateReport(List<Reservation> bookings) {

        System.out.println("\n------ Booking Report ------");

        int totalBookings = bookings.size();

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : bookings) {

            String type = r.getRoomType();

            roomTypeCount.put(type,
                    roomTypeCount.getOrDefault(type, 0) + 1);
        }

        System.out.println("Total Confirmed Bookings: " + totalBookings);

        System.out.println("\nBookings by Room Type:");

        for (String type : roomTypeCount.keySet()) {
            System.out.println(type + " : " + roomTypeCount.get(type));
        }
    }
}

// -------------------- Main Class --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println(" Book My Stay App - Booking History ");
        System.out.println("======================================");

        BookingHistory history = new BookingHistory();

        // Simulated confirmed reservations
        history.addBooking(new Reservation("RES101", "Arun", "Single Room"));
        history.addBooking(new Reservation("RES102", "Priya", "Double Room"));
        history.addBooking(new Reservation("RES103", "Rahul", "Suite Room"));
        history.addBooking(new Reservation("RES104", "Anita", "Single Room"));

        // Display booking history
        history.displayHistory();

        // Generate report
        BookingReportService reportService = new BookingReportService();
        reportService.generateReport(history.getHistory());

        System.out.println("\n======================================");
        System.out.println("Report generated successfully!");
    }
}