/**
 * Represents a customer
 * Extends the User class by adding a shopping cart
 * to manage purchases during individual sessions
 */
public class Customer extends User{
	private ShoppingCart basket;
	/**
     * Constructs a new Customer and initializes an empty shopping basket.
     * @param userID  The unique identifier for the customer.
     * @param name    The customer's display name.
     * @param address The customer's billing/shipping address.
     */
	public Customer(int userID, String name, Address address){
		super(userID, name, address);
		
		this.basket = new ShoppingCart();
		
	}
	public ShoppingCart getBasket() {
		return basket;
	}
	
	public ServiceResult addToBasket(Product product, int quantity) {
	        return this.basket.addItemToCart(product, quantity);
	}
	
	public ServiceResult removeItem(Product product, int quantity) {
		return this.basket.removeItemFromCart(product, quantity);
	}
	
	public boolean isBasketEmpty() {
		return this.basket.getItems().isEmpty();
	}
	
	public Receipt checkout(PaymentMethod paymentMethod, Stock stock) {
		return this.basket.pay(paymentMethod, stock, this.getAddress());
	}
	
	public void emptyBasket() {
		this.basket.clearCart();
	}
}
