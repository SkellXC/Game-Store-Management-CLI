/**
 * Manages the temporary selection of products for a customer's session
 * Acts as the intermediary between desired purchases and the actual stock.
 */



import java.util.ArrayList;

public class ShoppingCart {
    private ArrayList<Product> items;

    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    /**
     * Validates that the requested quantity does not exceed available physical stock
     * before adding items to the basket.
     */
    public ServiceResult addItemToCart(Product product, int quantity) {
        if (quantity < 1) return ServiceResult.INVALID_INPUT;
        
        int currentInCart = getProductCount(product.getProductId());
        if ((currentInCart + quantity) > product.getQuantityInStock()) {
            return ServiceResult.INSUFFICIENT_QUANTITY;
        }
        
        for (int i = 0; i < quantity; i++) items.add(product);
        return ServiceResult.SUCCESS;
    }

    public int getProductCount(int productId) {
        int count = 0;
        for (Product item : items) {
            if (item.getProductId() == productId) count++;
        }
        return count;
    }

    public ServiceResult removeItemFromCart(Product product, int quantity) {
        int currentCount = getProductCount(product.getProductId());
        if (currentCount == 0) return ServiceResult.NOT_FOUND;
        if (quantity > currentCount) return ServiceResult.EXCEEDED_QUANTITY;
        
        int removedCount = 0;
        java.util.Iterator<Product> iterator = items.iterator();
        while (iterator.hasNext() && removedCount < quantity) {
            Product currentItem = iterator.next();
            if (currentItem.getProductId() == product.getProductId()) {
                iterator.remove();
                removedCount++;
            }
        }
        return ServiceResult.SUCCESS;
    }

    public void clearCart() {
        items.clear();
    }

    public double calculateTotal() {
        double total = 0;
        for(Product item : items) total += item.getPrice();
        return total;
    }

    public ArrayList<Product> getItems() {
        return items;
    }

    /**
     * Executes the checkout process in two phases to prevent stock discrepancies.
     * Phase 1 validates that the main store still holds enough stock for every item in the basket.
     * Phase 2 executes the deductions and generates the receipt.
     */
    public Receipt pay(PaymentMethod paymentMethod, Stock mainStock, Address billingAddress) {
        if (this.items.isEmpty()) return null; 

        ArrayList<Integer> processedIDs = new ArrayList<>();
        
        // Phase 1: Validation
        for (Product basketItem : this.items) {
            int id = basketItem.getProductId();
            
            if (!processedIDs.contains(id)) {
                int totalQtyInBasket = getProductCount(id);
                Product stockItem = mainStock.findProductById(id);
                
                // Exit if the store no longer has the desired amount of stock
                if (stockItem == null || totalQtyInBasket > stockItem.getQuantityInStock()) return null; 
                
                processedIDs.add(id);
            }
        }

        // Phase 2: Processing
        double totalAmount = calculateTotal();
        for (Integer id : processedIDs) {
            int totalQtyInBasket = getProductCount(id);
            mainStock.updateStockValue(id, -totalQtyInBasket);
        }
        
        Receipt finalReceipt = paymentMethod.processPayment(totalAmount, billingAddress);
        this.clearCart();
        return finalReceipt;
    }
}