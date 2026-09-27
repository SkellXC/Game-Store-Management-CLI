/**
 * Represents the payment method paypal.
 * Generates a formatted receipt at the end
 * unique to Paypal transactions.
 */
public class Paypal implements PaymentMethod {
	private String email;
	
	/**
	 * Initializes a new PayPal payment.
	 * @param email The customer's PayPal email address used for the transaction.
	 */
	public Paypal(String email) {
		this.email = email;
	}
	
	@Override
	public Receipt processPayment(double total, Address address) {
		String today = java.time.LocalDate.now().toString();

		String receiptText = String.format("%.2f paid via PayPal using %s on %s . Billing address: %s",
				total, email, today, address.getFullAddress());
		return new Receipt(receiptText);
	}
}

