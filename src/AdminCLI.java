import java.util.Scanner;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

/**
 * Provides the interactive command-line interface for Admin users.
 * Handles elevated inventory management tasks such as viewing wholesale costs, 
 * adding new products, and updating existing stock levels.
 */
public class AdminCLI {
    public final static String NOT_IMPLEMENTED = "Not implemented";
    public final static String INVALID = "Invalid input";

    public static void run(Scanner consoleInput, Stock stock) {
        System.out.println("ADMIN VIEW");

        while (true) {
            printAdminMenu();
            
            int selection;
            try {
                selection = Integer.parseInt(consoleInput.nextLine().trim());
            } catch (NumberFormatException e) {
                selection = -1; 
            }
            
            switch (selection) {
                case 1: 
                    adminDisplayItems(stock.getProductList(), consoleInput); 
                    break;
                case 2: 
                    System.out.println("Add New Product to Stock:");
                    takeProductDetails(consoleInput, stock); 
                    break;
                case 3: 
                    updateStockAmount(consoleInput, stock);
                    break;
                case 0:
                    return;
                default:
                    System.out.println(INVALID);
                    System.out.println();
            }
        }
    }
    
    /**
     * Prompts the admin for stock adjustments and requires explicit confirmation 
     * before delegating to the Stock class to overwrite the file.
     */
    public static void updateStockAmount(Scanner scanner, Stock stock) {
        try {
            System.out.println("Enter the Product ID:");
            int productId = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.println("Enter the amount to add:");
            int amountAdded = Integer.parseInt(scanner.nextLine().trim());
            
            while (true) {
                System.out.println("You are about to add " + amountAdded + " items to the stock.");
                System.out.println("Enter 1 to confirm or 0 to cancel");
                int accept = Integer.parseInt(scanner.nextLine().trim());
                
                if (accept == 1) {
                    boolean success = stock.updateStockValue(productId, amountAdded); 
                    
                    if (success) {
                        System.out.println("Stock updated successfully.");
                    } else {
                        System.out.println("Failed to update stock. Please ensure the Product ID is correct.");
                    }
                    break;
                } else if (accept == 0) {
                    System.out.println("Operation cancelled.");
                    break;
                } else {
                    System.out.println(INVALID);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Must be a number.");
        }
    }
    
    private static void printAdminMenu() {
        System.out.println("\nPLEASE SELECT ACTION BY INPUTTING THE CORRESPONDING NUMBER (or 0 for logout)");
        System.out.println("1) View all products");
        System.out.println("2) Add new product");
        System.out.println("3) Update Stock Amounts");
        System.out.println("0) Log out");
    }
    
    /**
     * Gathers product attributes sequentially via CLI prompts, instantiates the 
     * specific Product object, and passes it to Stock.addNewProduct().
     */
    public static void takeProductDetails(Scanner scanner, Stock stock) {
        System.out.println("What type of product is this?");
        System.out.println("Enter '0' to cancel this operation");
        
        ProductCategory[] categories = ProductCategory.values();
        for (int i = 0; i < categories.length; i++) {
            String displayName = (categories[i] == ProductCategory.BOARDGAME) ? "board game" : "accessory";
            System.out.println("Enter '" + (i + 1) + "' for " + displayName);
        }
        
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println(INVALID);
            return;
        }

        if (choice == 0) {
            System.out.println("Operation cancelled.");
            return;
        }
        
        if (choice < 1 || choice > categories.length) {
            System.out.println(INVALID);
            return;
        }

        ProductCategory selectedCat = categories[choice - 1];
        String categoryName = (selectedCat == ProductCategory.BOARDGAME) ? "board game" : "accessory";

        System.out.println("Enter the Product ID (Whole Number): ");
        int id = Integer.parseInt(getValidIntString(scanner));
        
        System.out.println("Enter type:");
        String type = scanner.nextLine().trim();
        
        System.out.println("Enter name:");
        String name = scanner.nextLine().trim();      
        
        System.out.println("Enter selling price:");
        double price = Double.parseDouble(getValidDoubleString(scanner));

        System.out.println("Enter initial stock amount:");
        int stockAmount = Integer.parseInt(getValidIntString(scanner));
        
        System.out.println("Enter wholesale unit price:");
        double cost = Double.parseDouble(getValidDoubleString(scanner));
        
        Product newProduct = null;
        String extraLabel = "";
        String extraValue = "";

        if (selectedCat == ProductCategory.BOARDGAME) {
            System.out.println("Enter max number of players:");
            int maxPlayers = Integer.parseInt(getValidIntString(scanner));
            newProduct = new BoardGame(id, selectedCat, type, name, cost, stockAmount, price, maxPlayers);
            extraLabel = "Max Players";
            extraValue = String.valueOf(maxPlayers);
        } else {
            System.out.println("Enter compatibility:");
            String compatibility = scanner.nextLine().trim();
            newProduct = new Accessory(id, selectedCat, type, name, cost, stockAmount, price, compatibility);
            extraLabel = "Compatibility";
            extraValue = compatibility;
        }
        
        System.out.println("\nThe product you are adding has the following details:");
        System.out.printf("ID: %d\nCategory: %s\nType: %s\nName: %s\nPrice: £%.2f\nStock: %d\nWholesale Price: £%.2f\n%s: %s\n",
            newProduct.getProductId(), categoryName, type, newProduct.getProductName(), newProduct.getPrice(),
            newProduct.getQuantityInStock(), newProduct.getPurchaseCost(), extraLabel, extraValue);
        
        while (true) {
            System.out.println("Enter 1 to add product and 0 to cancel");
            try {
                int accept = Integer.parseInt(scanner.nextLine().trim());
                if (accept == 1) {
                    boolean success = stock.addNewProduct(newProduct);
                    if (success) {
                        System.out.println("Item successfully added to stock.");
                    } else {
                        System.out.println("Failed to add item. Ensure numbers were entered correctly for ID, prices, and stock.");
                    }
                    break;
                } else if (accept == 0) {
                    System.out.println("Operation cancelled.");
                    break;
                } else {
                    System.out.println(INVALID);
                }
            } catch (NumberFormatException e) {
                System.out.println(INVALID);
            }
        }
    }
    
    
    /**
     * Displays the inventory sorted by price descending.
     * Unlike the customer view, this exposes the confidential purchase cost.
     */
    public static void adminDisplayItems(List<Product> productList, Scanner scanner) {
        List<Product> sortedList = new ArrayList<>(productList);
        sortedList.sort(Comparator.comparingDouble(Product::getPrice).reversed());

        System.out.println(String.format("%-6s | %-30s | %-8s | %-15s | %-7s | %s",
                "ID", "Name", "Price", "Purchase Cost", "Stock", "Extra Info"));
        System.out.println("--------------------------------------------------------------------------------------------------------");

        for (Product p : sortedList) {
            String extraInfo = "";
            if (p instanceof BoardGame) {
                extraInfo = "Max Players: " + ((BoardGame) p).getMaxPlayers();
            } else if (p instanceof Accessory) {
                extraInfo = "Compatibility: " + ((Accessory) p).getCompatibility();
            }

            System.out.println(String.format("%-6d | %-30s | £%-7.2f | £%-14.2f | %-7d | %s", 
                                p.getProductId(), p.getProductName(), p.getPrice(), p.getPurchaseCost(), p.getQuantityInStock(), extraInfo));
        }
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }
    
    private static String getValidIntString(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                Integer.parseInt(input);
                return input; 
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number:");
            }
        }
    }

    /**
     * Forces the user to input a valid decimal number before continuing.
     */
    private static String getValidDoubleString(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                Double.parseDouble(input);
                return input; 
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid decimal number:");
            }
        }
    }
}