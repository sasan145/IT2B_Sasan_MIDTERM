package com.hotel.ui;

import com.hotel.dao.ScenarioDao;
import com.hotel.model.ScenarioEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Interactive console menu. No SQL or JDBC here.
 */
public class ScenarioMenu {

    private static final String LINE = "=".repeat(52);
    private static final String WIDE_LINE = "=".repeat(80);
    private static final String BORDER =
            "+----+--------------------+------------+--------------+--------------+";

    private final ScenarioDao dao = new ScenarioDao();
    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            System.out.println();
            switch (choice) {
                case "1" -> addReservation();
                case "2" -> viewReservations();
                case "3" -> {
                    System.out.println("Exiting system. Goodbye!");
                    running = false;
                }
                default -> System.out.println("ERROR: Invalid choice. Please enter 1, 2, or 3.");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println();
        System.out.println(LINE);
        System.out.println("          HOTEL ROOM RESERVATION SYSTEM");
        System.out.println(LINE);
        System.out.println("[1] Add New Reservation");
        System.out.println("[2] View All Reservations");
        System.out.println("[3] Exit System");
        System.out.println(LINE);
        System.out.print("Enter choice: ");
    }

    private void addReservation() {
        System.out.println("--- ADD NEW RESERVATION ---");

        String name = promptNonEmpty("Enter Guest Name: ");
        String roomType = promptOneOf("Enter Room Type (SINGLE / DOUBLE / SUITE): ",
                "SINGLE", "DOUBLE", "SUITE");
        double rate = promptRate("Enter Nightly Rate: ");
        String payment = promptOneOf("Enter Payment Status (PAID / PENDING): ",
                "PAID", "PENDING");

        // id = 0 is a placeholder; SQLite assigns the real one (starts at 1).
        ScenarioEntity entity = new ScenarioEntity(0, name, roomType, rate, payment);
        try {
            if (dao.insert(entity)) {
                System.out.println("\nSUCCESS: Reservation recorded in database!");
            } else {
                System.out.println("\nERROR: Reservation was not saved.");
            }
        } catch (SQLException e) {
            System.out.println("\nDATABASE ERROR: " + e.getMessage());
        }
    }

    private void viewReservations() {
        try {
            List<ScenarioEntity> records = dao.findAll();
            System.out.println(WIDE_LINE);
            System.out.println("                              ALL ROOM RESERVATIONS");
            System.out.println(WIDE_LINE);

            if (records.isEmpty()) {
                System.out.println("No reservations found.");
            } else {
                System.out.println(BORDER);
                System.out.printf("| %-2s | %-18s | %-10s | %-12s | %-12s |%n",
                        "ID", "GUEST NAME", "ROOM TYPE", "NIGHTLY RATE", "PAYMENT");
                System.out.println(BORDER);
                for (ScenarioEntity r : records) {
                    System.out.printf("| %-2d | %-18s | %-10s | %-12.2f | %-12s |%n",
                            r.id(), r.guestName(), r.roomType(), r.nightlyRate(), r.payment());
                }
                System.out.println(BORDER);
            }
            System.out.println("Total Records Displayed: " + records.size());
        } catch (SQLException e) {
            System.out.println("DATABASE ERROR: " + e.getMessage());
        }
    }

    // ---------- input helpers (nextLine only, so no leftover line breaks) ----------

    private String promptNonEmpty(String label) {
        while (true) {
            System.out.print(label);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Try again.");
        }
    }

    private String promptOneOf(String label, String... allowed) {
        while (true) {
            System.out.print(label);
            String input = scanner.nextLine().trim().toUpperCase();
            for (String option : allowed) {
                if (option.equals(input)) {
                    return input;
                }
            }
            System.out.println("Invalid value. Allowed: " + String.join(", ", allowed));
        }
    }

    private double promptRate(String label) {
        while (true) {
            System.out.print(label);
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value > 0) {
                    return Math.round(value * 100.0) / 100.0;
                }
                System.out.println("Rate must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Example: 2500.50");
            }
        }
    }
}
