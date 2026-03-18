
// Abstract class
abstract class Room {
    protected String roomType;
    protected int numberOfBeds;
    protected double price;

    // Constructor
    public Room(String roomType, int numberOfBeds, double price) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.price = price;
    }

    // Abstract method
    public abstract void displayRoomDetails();
}

// Single Room class
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 1000.0);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type   : " + roomType);
        System.out.println("Beds        : " + numberOfBeds);
        System.out.println("Price       : ₹" + price);
    }
}

// Double Room class
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 2000.0);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type   : " + roomType);
        System.out.println("Beds        : " + numberOfBeds);
        System.out.println("Price       : ₹" + price);
    }
}

// Suite Room class
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 5000.0);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type   : " + roomType);
        System.out.println("Beds        : " + numberOfBeds);
        System.out.println("Price       : ₹" + price);
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Static availability variables
        int singleRoomAvailability = 5;
        int doubleRoomAvailability = 3;
        int suiteRoomAvailability = 2;

        // Polymorphism (using Room reference)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        System.out.println("======================================");
        System.out.println("     Book My Stay App - Version 2.0");
        System.out.println("======================================");

        // Display Single Room details
        single.displayRoomDetails();
        System.out.println("Availability: " + singleRoomAvailability);
        System.out.println("--------------------------------------");

        // Display Double Room details
        doubleRoom.displayRoomDetails();
        System.out.println("Availability: " + doubleRoomAvailability);
        System.out.println("--------------------------------------");

        // Display Suite Room details
        suite.displayRoomDetails();
        System.out.println("Availability: " + suiteRoomAvailability);
        System.out.println("======================================");

        System.out.println("Room information displayed successfully!");
    }
}