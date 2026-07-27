import java.util.*;

/* ============================================================
   PROJECT TITLE: PARKING LOT SYSTEM
   Description: A console-based Java application to manage
   vehicle entry, exit, slot allocation, and fee calculation
   in a parking lot.
   ============================================================ */

// ---------- Enum for Vehicle Type ----------
enum VehicleType {
    CAR, BIKE, TRUCK
}

// ---------- Vehicle Class ----------
class Vehicle {
    private String numberPlate;
    private VehicleType type;

    public Vehicle(String numberPlate, VehicleType type) {
        this.numberPlate = numberPlate;
        this.type = type;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public VehicleType getType() {
        return type;
    }
}

// ---------- Parking Slot Class ----------
class ParkingSlot {
    private int slotNumber;
    private boolean isOccupied;
    private Vehicle vehicle;
    private VehicleType allowedType;
    private long entryTime;

    public ParkingSlot(int slotNumber, VehicleType allowedType) {
        this.slotNumber = slotNumber;
        this.allowedType = allowedType;
        this.isOccupied = false;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public VehicleType getAllowedType() {
        return allowedType;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public long getEntryTime() {
        return entryTime;
    }

    public void parkVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.isOccupied = true;
        this.entryTime = System.currentTimeMillis();
    }

    public void removeVehicle() {
        this.vehicle = null;
        this.isOccupied = false;
        this.entryTime = 0;
    }
}

// ---------- Parking Lot Class (Core Logic) ----------
class ParkingLot {
    private List<ParkingSlot> slots;
    private Map<String, ParkingSlot> activeVehicles; // numberPlate -> slot

    // Rate per hour for each vehicle type
    private static final Map<VehicleType, Double> RATE_PER_HOUR = new HashMap<>();
    static {
        RATE_PER_HOUR.put(VehicleType.BIKE, 10.0);
        RATE_PER_HOUR.put(VehicleType.CAR, 20.0);
        RATE_PER_HOUR.put(VehicleType.TRUCK, 30.0);
    }

    public ParkingLot(int totalSlots) {
        slots = new ArrayList<>();
        activeVehicles = new HashMap<>();

        // Distribute slots: 40% bike, 40% car, 20% truck
        for (int i = 1; i <= totalSlots; i++) {
            VehicleType type;
            if (i <= totalSlots * 0.4) {
                type = VehicleType.BIKE;
            } else if (i <= totalSlots * 0.8) {
                type = VehicleType.CAR;
            } else {
                type = VehicleType.TRUCK;
            }
            slots.add(new ParkingSlot(i, type));
        }
    }

    // Park a vehicle -> returns slot number or -1 if full
    public int parkVehicle(Vehicle vehicle) {
        if (activeVehicles.containsKey(vehicle.getNumberPlate())) {
            System.out.println("Vehicle already parked!");
            return -1;
        }

        for (ParkingSlot slot : slots) {
            if (!slot.isOccupied() && slot.getAllowedType() == vehicle.getType()) {
                slot.parkVehicle(vehicle);
                activeVehicles.put(vehicle.getNumberPlate(), slot);
                return slot.getSlotNumber();
            }
        }
        return -1; // No available slot
    }

    // Remove a vehicle and calculate fee
    public double removeVehicle(String numberPlate) {
        ParkingSlot slot = activeVehicles.get(numberPlate);
        if (slot == null) {
            System.out.println("Vehicle not found in parking lot!");
            return -1;
        }

        long duration = System.currentTimeMillis() - slot.getEntryTime();
        double hours = Math.max(1, Math.ceil(duration / (1000.0 * 60 * 60)));
        // For demo purposes, treat every started minute as billable hour-equivalent
        double fee = hours * RATE_PER_HOUR.get(slot.getVehicle().getType());

        slot.removeVehicle();
        activeVehicles.remove(numberPlate);
        return fee;
    }

    public void displayAvailability() {
        Map<VehicleType, Integer> available = new HashMap<>();
        for (VehicleType type : VehicleType.values()) {
            available.put(type, 0);
        }
        for (ParkingSlot slot : slots) {
            if (!slot.isOccupied()) {
                available.put(slot.getAllowedType(), available.get(slot.getAllowedType()) + 1);
            }
        }
        System.out.println("\n--- Slot Availability ---");
        for (VehicleType type : VehicleType.values()) {
            System.out.println(type + ": " + available.get(type) + " slot(s) available");
        }
    }

    public void displayOccupied() {
        System.out.println("\n--- Currently Parked Vehicles ---");
        if (activeVehicles.isEmpty()) {
            System.out.println("No vehicles currently parked.");
            return;
        }
        for (Map.Entry<String, ParkingSlot> entry : activeVehicles.entrySet()) {
            ParkingSlot slot = entry.getValue();
            System.out.println("Slot " + slot.getSlotNumber() + " -> " + entry.getKey()
                    + " (" + slot.getVehicle().getType() + ")");
        }
    }
}

// ---------- Main Class (Menu Driven Program) ----------
public class ParkingLotSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter total number of parking slots: ");
        int totalSlots = Integer.parseInt(sc.nextLine().trim());
        ParkingLot parkingLot = new ParkingLot(totalSlots);

        boolean running = true;
        while (running) {
            System.out.println("\n===== PARKING LOT SYSTEM =====");
            System.out.println("1. Park a Vehicle");
            System.out.println("2. Remove a Vehicle (Exit)");
            System.out.println("3. Display Slot Availability");
            System.out.println("4. Display Parked Vehicles");
            System.out.println("5. Exit Program");
            System.out.print("Choose an option: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Enter vehicle number plate: ");
                    String plate = sc.nextLine().trim();
                    System.out.print("Enter vehicle type (CAR/BIKE/TRUCK): ");
                    String typeInput = sc.nextLine().trim().toUpperCase();

                    try {
                        VehicleType type = VehicleType.valueOf(typeInput);
                        Vehicle vehicle = new Vehicle(plate, type);
                        int slotNum = parkingLot.parkVehicle(vehicle);
                        if (slotNum != -1) {
                            System.out.println("Vehicle parked successfully at Slot #" + slotNum);
                        } else {
                            System.out.println("Sorry, no available slot for this vehicle type.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid vehicle type entered.");
                    }
                    break;

                case "2":
                    System.out.print("Enter vehicle number plate to remove: ");
                    String exitPlate = sc.nextLine().trim();
                    double fee = parkingLot.removeVehicle(exitPlate);
                    if (fee >= 0) {
                        System.out.printf("Vehicle removed. Total Fee: $%.2f%n", fee);
                    }
                    break;

                case "3":
                    parkingLot.displayAvailability();
                    break;

                case "4":
                    parkingLot.displayOccupied();
                    break;

                case "5":
                    running = false;
                    System.out.println("Exiting Parking Lot System. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        sc.close();
    }
}