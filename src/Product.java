/**
 * Serves as the foundation for any products to be sold
 * in the store.
 * It holds the most likely attributes and subclasses
 * will have to extend these to add specific ones.
 */

public abstract class Product {
    private int productId;
    private ProductCategory productCategory;
    private String productName;
    private double purchaseCost;
    private int quantityInStock;
    private double price;

    /**
     * Constructs the base properties of a product.
     * @param productId       The unique 4-digit identifier.
     * @param productCategory The high-level category (e.g., BOARDGAME or ACCESSORY).
     * @param productName     The display name of the item.
     * @param purchaseCost    The wholesale cost paid by the store (hidden from customers).
     * @param quantityInStock The number of units currently available to sell.
     * @param price           The retail price the customer pays.
     */
    
    public Product(int productId, ProductCategory productCategory, String productName, 
            double purchaseCost, int quantityInStock, double price) {
        this.productId = productId;
        this.productCategory = productCategory;
        this.productName = productName;
        this.purchaseCost = purchaseCost;
        this.quantityInStock = quantityInStock;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public String getProductName() {
        return productName;
    }

    public double getPurchaseCost() {
        return purchaseCost;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public abstract String toString();
}