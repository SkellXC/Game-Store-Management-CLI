import java.util.Scanner;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.ArrayList;


/**
 * The entry point for the application.
 * Loads the users and then routes them
 * to their designated CLI (based on roles).
 */
public class Main {

    public static void main(String[] args) {
        
        Stock stock = new Stock();
        
        List<User> users = loadUsersFromFile("UserAccounts.txt");
        Scanner consoleInput = new Scanner(System.in);
        System.out.println("WELCOME");
        
        while (true) {
            printWelcomeMenu(users);

            try {
                String line = consoleInput.nextLine().trim();
                int selection = Integer.parseInt(line);

                if (selection == 0) {
                    System.out.println("Goodbye");
                    System.out.println("Closing program...");
                    System.out.println();
                    consoleInput.close(); 
                    return;
                }

                // Converts the 1-based menu selection to a 0-based array index
                if (selection < 1 || selection > users.size()) {
                    System.out.println("Invalid user selection. Try again.");
                    continue; 
                }

                User selectedUser = users.get(selection - 1);

                // Route to the correct CLI based on the object type
                if (selectedUser instanceof Admin) {
                    AdminCLI.run(consoleInput, stock); 
                } else if (selectedUser instanceof Customer) {
                    CustomerCLI.run(consoleInput, stock, (Customer) selectedUser);
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static void printWelcomeMenu(List<User> users) {
        System.out.println("PLEASE SELECT USER BY INPUTTING THE CORRESPONDING NUMBER (or 0 for exit)");
        
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            String role = (u instanceof Customer) ? "customer" : "admin";
            System.out.println((i + 1) + ") " + u.getUserID() + " | " + u.getName() + " | " + role);
        }

        System.out.println("0) Exit");
    }
    
    /**
     * Parses the flat text file to populate the system's user registry.
     * Expects a specific 6-part format delimited by semicolons:
     * [ID; Name; HouseNumber; Postcode; City; Role]
     */
    private static List<User> loadUsersFromFile(String filename) {
        List<User> users = new ArrayList<>();
        File file = new File(filename);

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                // Split by semicolon and remove leading/trailing spaces from each part
                String[] data = line.split(";");
                
                int id = Integer.parseInt(data[0].trim());
                String name = data[1].trim();
                
                int houseNumber = Integer.parseInt(data[2].trim());
                String postcode = data[3].trim();
                String city = data[4].trim();
                Address address = new Address(postcode, city, houseNumber);

                String role = data[5].trim().toLowerCase();

                if (role.equals("admin")) {
                    users.add(new Admin(id, name, address)); 
                } else if (role.equals("customer")) {
                    users.add(new Customer(id, name, address)); 
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File '" + filename + "' not found. Make sure it is in your project root.");
        } catch (Exception e) {
            System.out.println("Error parsing the user file: " + e.getMessage());
        }

        return users;
    }
}