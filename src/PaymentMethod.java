/**
 * This interface allows the system to accept
 * different payment types without having to
 * modify the checkout logic for each type.
 */
public interface PaymentMethod {
	/**
     * Processes a payment transaction and generates a formatted receipt.
     * @param total   The final total amount to be charged to the customer.
     * @param address The billing address to be printed on the receipt.
     * @return A Receipt object representing the successful transaction.
     */
	Receipt processPayment(double total, Address address);
}
