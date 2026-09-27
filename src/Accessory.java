/**
 * An accessory is a type of product sold in the store
 * This class extends base class Product by
 * adding specific properties relevant to accessories
 * such as type and compatibility.
 * */

public class Accessory extends Product {
    private String type;
    private String compatibility;
    /**
     * Constructs a new Accessory item.
     * * @param productId       The unique 4-digit identifier for the product.
     * @param productCategory The category of the product (ACCESSORY).
     * @param type            The specific type of accessory (e.g., dice, miniature).
     * @param productName     The display name of the accessory.
     * @param purchaseCost    The wholesale cost paid by the store.
     * @param quantityInStock The number of units currently available.
     * @param price           The retail price the customer pays.
     * @param compatibility   The game systems or product lines this accessory works with.
     */

    public Accessory(int productId, ProductCategory productCategory, String type, String productName, 
            double purchaseCost, int quantityInStock, double price, String compatibility) {
        super(productId, productCategory, productName, purchaseCost, quantityInStock, price);
        this.type = type;
        this.compatibility = compatibility;
    }

    public String getType() {
        return type;
    }


    public String getCompatibility() {
        return compatibility;
    }

    @Override
    public String toString() {
        return String.format("%-6d | %-30s | £%-7.2f | %-7d | Compatibility: %s", 
                getProductId(), getProductName(), getPrice(), getQuantityInStock(), compatibility);
    }
}