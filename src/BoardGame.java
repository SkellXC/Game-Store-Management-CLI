/**
 * Represents a product sold in the store
 * Extends from the base product class to include
 * additional properties (type & max players)
 */

public class BoardGame extends Product {
    private String type;
    private int maxPlayers;
    
    /**
     * Constructs a new Board Game item.
     * * @param productId       The unique 4-digit identifier.
     * @param productCategory The category (BOARDGAME).
     * @param type            The genre of the board game .
     * @param productName     The display name of the game.
     * @param purchaseCost    The wholesale cost paid by the store.
     * @param quantityInStock The number of units currently available.
     * @param price           The retail price the customer pays.
     * @param maxPlayers      The maximum number of people that can play the game.
     */
    
    public BoardGame(int productId, ProductCategory productCategory, String type, String productName, 
            double purchaseCost, int quantityInStock, double price, int maxPlayers) {
        super(productId, productCategory, productName, purchaseCost, quantityInStock, price);
        this.type = type;
        this.maxPlayers = maxPlayers;
    }

    public String getType() {
        return type;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public String toString() {
        return String.format("%-6d | %-30s | £%-7.2f | %-7d | Max Players: %d", 
                getProductId(), getProductName(), getPrice(), getQuantityInStock(), maxPlayers);
    }
}