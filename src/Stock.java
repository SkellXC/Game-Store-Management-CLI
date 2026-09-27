import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.util.List;

/**
 * Manages the store's inventory
 */

public class Stock {
    private ArrayList<Product> productList;
    private final String filename = "Stock.txt";
    
    public Stock() {
        this.productList = new ArrayList<>();
        loadStock();
    }
    
    public List<Product> getProductList(){
        return Collections.unmodifiableList(productList);
    }
    
    
    /**
     * Appends a new product to both the active memory and the text file.
     * @param newProduct The instantiated Product (BoardGame or Accessory) to add.
     */
    public boolean addNewProduct(Product newProduct) {
        try {
            if (findProductById(newProduct.getProductId()) != null) {
                System.out.println("Error: A product with ID " + newProduct.getProductId() + " already exists.");
                return false; 
            }
            
            String category = "";
            String type = "";
            String additional = "";

            if (newProduct instanceof BoardGame) {
                category = "board game";
                type = ((BoardGame) newProduct).getType();
                additional = String.valueOf(((BoardGame) newProduct).getMaxPlayers());
            } else if (newProduct instanceof Accessory) {
                category = "accessory";
                type = ((Accessory) newProduct).getType();
                additional = ((Accessory) newProduct).getCompatibility();
            }
            
            PrintWriter writer = new PrintWriter(new FileWriter(filename, true));
            writer.printf("\n%d; %s; %s; %s; %.2f; %d; %.2f; %s", 
                newProduct.getProductId(), category, type, newProduct.getProductName(), 
                newProduct.getPrice(), newProduct.getQuantityInStock(), newProduct.getPurchaseCost(), additional);
            writer.close();
            
            productList.add(newProduct);
            
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    /**
     * Modifies the stock level of an existing item.
     * This method re-writes the entire file after updating
     * the value
     */
    public boolean updateStockValue(int id, int stockToAdd) {
        boolean itemFound = false;

        for (Product p : productList) {
            if (p.getProductId() == id) {
                int updatedStock = p.getQuantityInStock() + stockToAdd;
                p.setQuantityInStock(updatedStock);
                itemFound = true;
                break;
            }
        }

        if (!itemFound) return false; 

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filename, false));
            
            for (Product p : productList) {
                String category = "";
                String type = "";
                String additional = "";

                if (p instanceof BoardGame) {
                    category = "board game";
                    type = ((BoardGame) p).getType();
                    additional = String.valueOf(((BoardGame) p).getMaxPlayers());
                } else if (p instanceof Accessory) {
                    category = "accessory";
                    type = ((Accessory) p).getType();
                    additional = ((Accessory) p).getCompatibility();
                }

                writer.printf("%d; %s; %s; %s; %.2f; %d; %.2f; %s\n",
                    p.getProductId(), category, type, p.getProductName(), 
                    p.getPrice(), p.getQuantityInStock(), p.getPurchaseCost(), additional);
            }

            writer.close();
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public ArrayList<Product> loadStock() {
        try {
        	// Clear required to prevent duplicating data if loadStock
        	// is called multiple times
            productList.clear();
            File file = new File(filename);
            if (!file.exists()) return productList; 
            
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if(line.isEmpty()) continue;
                
                String[] parts = line.split(";");
                
                int id = Integer.parseInt(parts[0].trim());
                String category = parts[1].trim();
                String type = parts[2].trim(); 
                String name = parts[3].trim();
                double price = Double.parseDouble(parts[4].trim());
                int stock = Integer.parseInt(parts[5].trim());
                double cost = Double.parseDouble(parts[6].trim());
                String additional = parts[7].trim();
                
                if(category.equalsIgnoreCase("board game")) {
                    int maxPlayers = Integer.parseInt(additional);
                    productList.add(new BoardGame(id, ProductCategory.BOARDGAME, type, name, cost, stock, price, maxPlayers));
                }
                else if (category.equalsIgnoreCase("accessory")) {
                    productList.add(new Accessory(id, ProductCategory.ACCESSORY, type, name, cost, stock, price, additional));
                }
            }
            scanner.close();
        } catch(Exception e) {
            System.out.println("Error loading stock: "+ e.getMessage());
        }
        return productList;
    }
    
    public Product findProductById(int id) {
        for (Product p : productList) {
            if (p.getProductId() == id) {
                return p; 
            }
        }
        return null; 
    }
    
    public ArrayList<Product> searchByIdMatch(String idQuery) {
        ArrayList<Product> matches = new ArrayList<>();
        for (Product p : productList) {
            if (String.valueOf(p.getProductId()).contains(idQuery)) {
                matches.add(p);
            }
        }
        return matches;
    }

    
    /**
     * Searches accessory compatibility.
     * Enforces an exact match for queries under 3 characters to prevent 
     * false positive substring matches (e.g., searching "D" matching "D&D").
     */
    public ArrayList<Product> searchByCompatibility(String compQuery) {
        ArrayList<Product> matches = new ArrayList<>();
        String searchLower = compQuery.toLowerCase();
        
        for (Product p : productList) {
            if (p instanceof Accessory) {
                String itemComp = ((Accessory) p).getCompatibility().toLowerCase();
                if (searchLower.length() < 3 && itemComp.equals(searchLower)) {
                    matches.add(p);
                } else if (searchLower.length() >= 3 && itemComp.contains(searchLower)) {
                    matches.add(p);
                }
            }
        }
        return matches;
    }
}