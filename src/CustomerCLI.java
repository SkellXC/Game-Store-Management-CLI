import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Provides the interactive command-line interface for Customer users.
 * Routes user input to the appropriate ShoppingCart and Stock operations.
 */
public class CustomerCLI {
    public final static String NOT_IMPLEMENTED = "Not implemented";
    public final static String INVALID = "Invalid input";

    public static void run(Scanner consoleInput, Stock stock, Customer currentCustomer) {
        System.out.println("USER VIEW");

        
        /**
         * The primary execution loop for the customer session.
         */
        while (true) {
            printCustomerMenu();
            
            int selection;
            try {
                selection = Integer.parseInt(consoleInput.nextLine().trim());
            } catch (NumberFormatException e) {
                selection = -1;
            }
            
            switch (selection) {
                case 1: 
                    userDisplayItems(consoleInput, stock.getProductList()); 
                    System.out.println("\nPress Enter to return to the main menu...");
                    consoleInput.nextLine(); 
                    break;
                case 2: 
                    userDisplayItems(consoleInput, stock.getProductList()); 
                    handleAdd(consoleInput, currentCustomer, stock);
                    System.out.println("\nPress Enter to return to the main menu...");
                    consoleInput.nextLine();
                    break;
                case 3:
                    handleViewBasket(currentCustomer);
                    System.out.println("\nPress Enter to return to the main menu...");
                    consoleInput.nextLine();
                    break;
                case 4: 
                    handleViewBasket(currentCustomer);
                    handleRemove(consoleInput, currentCustomer);
                    System.out.println("\nPress Enter to return to the main menu...");
                    consoleInput.nextLine();
                    break;
                case 5:
                    handlePayment(consoleInput, currentCustomer, stock);
                    System.out.println("\nPress Enter to return to the main menu...");
                    consoleInput.nextLine();
                    break;
                case 6:
                    clearBasket(consoleInput, currentCustomer);
                    System.out.println("\nPress Enter to return to the main menu...");
                    consoleInput.nextLine();
                    break;
                case 7:
                    handleSearch(consoleInput, stock);
                    System.out.println("\nPress Enter to return to the main menu...");
                    consoleInput.nextLine();
                    break;
                case 0:
                    return;
                default:
                    System.out.println(INVALID);
                    System.out.println();
            }
        }
    }
    
    private static void printCustomerMenu() {
        System.out.println("\nPLEASE SELECT ACTION BY INPUTTING THE CORRESPONDING NUMBER (or 0 for logout)");
        System.out.println("1) View all products");
        System.out.println("2) Add product to shopping basket");
        System.out.println("3) View contents of shopping basket");
        System.out.println("4) Remove product from shopping basket");
        System.out.println("5) Purchase items in the basket");
        System.out.println("6) Cancel shopping basket");
        System.out.println("7) Search");
        System.out.println("0) Log out");
    }

    /**
     * Displays all available products.
     */
    public static void userDisplayItems(Scanner scanner, List<Product> productList) {
        List<Product> sortedList = new ArrayList<>(productList);
        
        sortedList.sort(Comparator.comparingDouble(Product::getPrice).reversed());
        
        System.out.println(String.format("%-6s | %-30s | %-8s | %-7s | %s",
                "ID", "Name", "Price", "Stock", "Extra Info"));
        System.out.println("---------------------------------------------------------------------------------------");
        for (Product p : sortedList) {

            System.out.println(p.toString());
        }
    }
    
    /**
     * Handles the removal of items from the customer's personal basket.
     * Includes a confirmation check if the user attempts to remove all instances of an item.
     */
    public static void handleRemove(Scanner scanner, Customer customer) {
        System.out.println("Enter the ID of the product you want to remove:");
        int productID;
        try {
            productID = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        Product productToRemove = null;
        for (Product p : customer.getBasket().getItems()) {
            if (p.getProductId() == productID) {
                productToRemove = p;
                break;
            }
        }

        if (productToRemove == null) {
            System.out.println("Error: Product with ID " + productID + " is not in your basket.");
            return;
        }

        System.out.println("Enter the quantity to remove:");
        int quantity;
        try {
            quantity = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }
        if(quantity < 0) {
        	System.out.println("Please enter a positive number");
        	return;
        }
        int currentCount = customer.getBasket().getProductCount(productToRemove.getProductId());
        
        if (quantity == currentCount && currentCount > 0) {
            System.out.println("Warning: This will remove ALL instances of this item. Confirm? (1=Yes)");
            String confirm = scanner.nextLine().trim();
            if (!confirm.equals("1")) {
                System.out.println("Cancelled.");
                return;
            }
        }
        
        ServiceResult result = customer.removeItem(productToRemove, quantity);
        
        switch (result) {
            case EXCEEDED_QUANTITY:
                System.out.println("Error: You only have " + currentCount + " in your cart.");
                break;
            case SUCCESS:
                System.out.println("Successfully removed " + quantity + " item(s).");
                break;
            case NOT_FOUND:
                System.out.println("Error: Item not found in cart.");
                break;
            default:
                break;
        }
    }
    
    /**
     * Handles adding items to the basket by retrieving the product from the main stock
     * and passing it to the customer's cart logic.
     */
    public static void handleAdd(Scanner scanner, Customer customer, Stock stock) {
        System.out.println("Enter the ID of the product you want to add:");
        int productID;
        try {
            productID = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        Product productToAdd = stock.findProductById(productID);

        if (productToAdd == null) {
            System.out.println("Error: Product with ID " + productID + " not found.");
            return;
        }

        System.out.println("Enter the quantity to add:");
        int quantity;
        try {
            quantity = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        ServiceResult result = customer.addToBasket(productToAdd, quantity);

        switch (result) {
            case SUCCESS:
                System.out.println("Successfully added " + quantity + " x " + productToAdd.getProductName() + " to your basket.");
                break;
            case INVALID_INPUT:
                System.out.println("Error: Quantity must be at least 1.");
                break;
            case INSUFFICIENT_QUANTITY:
                System.out.println("Error: Not enough stock available. Currently available: " + productToAdd.getQuantityInStock());
                break;
            default:
                System.out.println("Error: Could not add item to basket.");
                break;
        }
    }

    
    /**
     * Aggregates and displays the current contents of the basket, calculating subtotals 
     * for distinct items and the final basket total.
     */
    public static void handleViewBasket(Customer customer) {
        ShoppingCart basket = customer.getBasket();
        List<Product> items = basket.getItems();

        if (items.isEmpty()) {
            System.out.println("\nYour shopping basket is currently empty.");
            return;
        }

        System.out.println("\n--- YOUR SHOPPING BASKET ---");
        System.out.println(String.format("%-5s | %-20s | %-10s | %-8s | %-10s",
                "ID", "Name", "Price", "Quantity", "Subtotal"));
        System.out.println("-------------------------------------------------------------------");

        java.util.ArrayList<Integer> processedIDs = new java.util.ArrayList<>();

        for (Product p : items) {
            if (!processedIDs.contains(p.getProductId())) {
                int quantity = basket.getProductCount(p.getProductId());
                double subtotal = p.getPrice() * quantity;
                
                System.out.println(String.format("%-5d | %-20s | £%-9.2f | %-8d | £%-9.2f",
                        p.getProductId(), p.getProductName(), p.getPrice(), quantity, subtotal));
                        
                processedIDs.add(p.getProductId());
            }
        }

        System.out.println("-------------------------------------------------------------------");
        System.out.println(String.format("TOTAL: £%.2f", basket.calculateTotal()));
        System.out.println("-------------------------------------------------------------------\n");
    }
    
    public static void clearBasket(Scanner scanner, Customer customer) {
        System.out.println("You are about to clear your basket!");
        System.out.println("Press 1 to confirm or enter to cancel.");
        
        String input = scanner.nextLine().trim();
        
        if (input.equals("1")) {
            customer.emptyBasket();
            System.out.println("Basket successfully cleared.");
        } else {
            System.out.println("Operation cancelled. Your basket is unchanged.");
        }
    }
    
    /**
     * Prompts the user for search parameters and routes to the appropriate Stock query method.
     */
    public static void handleSearch(Scanner scanner, Stock stock) {
        System.out.println("What would you like to search by?");
        System.out.println("1) Product ID");
        System.out.println("2) Compatibility");
        
        String searchType = scanner.nextLine().trim();

        if (searchType.equals("1")) {
            System.out.println("Enter Product ID:");
            String inputID = scanner.nextLine().trim();
            
            List<Product> matches = stock.searchByIdMatch(inputID);
            
            if (matches.isEmpty()) {
                System.out.println("No exact or partial matches found.");
            } else {
                System.out.println("Matches found:");
                userDisplayItems(scanner, matches);
            }

        } else if (searchType.equals("2")) {
            System.out.println("Enter Compatibility:");
            String inputComp = scanner.nextLine().trim();
            
            List<Product> matches = stock.searchByCompatibility(inputComp);
            
            if (matches.isEmpty()) {
                System.out.println("No compatibility matches found.");
            } else {
                userDisplayItems(scanner, matches);
            }
        } else {
            System.out.println("Invalid selection.");
        }
    }
    
    
    /**
     * Guides the user through the checkout process, enforcing input validation 
     * for payment details 
     */
    public static void handlePayment(Scanner scanner, Customer customer, Stock stock) {
    	if (customer.isBasketEmpty()) { 
            System.out.println("Your basket is empty. Nothing to checkout.");
            return; 
        }

        System.out.println("Select Payment Method:");
        System.out.println("1) PayPal");
        System.out.println("2) Credit Card");
        System.out.print("Choice: ");
        
        int payChoice;
        try {
            payChoice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Checkout cancelled.");
            return;
        }
        
        PaymentMethod method = null;

        if (payChoice == 1) {
            String email = "";
            while (true) {
                System.out.print("Enter PayPal email address (or 0 to cancel): ");
                email = scanner.nextLine().trim();
                
                if (email.equals("0")) {
                    System.out.println("Checkout cancelled.");
                    return;
                }
                
                if (email.contains("@") && email.contains(".")) {
                    break; 
                } else {
                    System.out.println("Invalid email format. It must contain an '@' and a '.'");
                }
            }
            method = new Paypal(email);
            
        } else if (payChoice == 2) {
            String card = "";
            while (true) {
                System.out.print("Enter 6-digit card number (or 0 to cancel): ");
                card = scanner.nextLine().trim();
                
                if (card.equals("0")) {
                    System.out.println("Checkout cancelled.");
                    return;
                }
                
                if (card.matches("\\d{6}")) {
                    break;
                } else {
                    System.out.println("Invalid input. Card number must be exactly 6 digits.");
                }
            }
            
            String code = "";
            while (true) {
                System.out.print("Enter 3-digit security code (or 0 to cancel): ");
                String codeStr = scanner.nextLine().trim();
                
                if (codeStr.equals("0")) {
                    System.out.println("Checkout cancelled.");
                    return;
                }
                
                if (codeStr.matches("\\d{3}")) {
                    code = codeStr;
                    break;
                } else {
                    System.out.println("Invalid input. Security code must be exactly 3 digits.");
                }
            }
            method = new CreditCard(card, code);
        } 
        else {
            System.out.println("Invalid payment method selected. Checkout cancelled.");
            return; 
        }

        Receipt finalReceipt = customer.checkout(method, stock);
        if (finalReceipt != null) {
            System.out.println("\n--- RECEIPT ---");
            System.out.println(finalReceipt); 
            System.out.println("---------------\n");
        } else {
            System.out.println("Checkout failed: One or more items in your basket"
                    + " have sold out or do not have enough stock remaining.");
        }
    }
}