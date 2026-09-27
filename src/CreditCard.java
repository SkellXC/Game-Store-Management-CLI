/**
 * Represents the credit card payment method
 * Generates a formatted receipt at the end.
 */
	public class CreditCard implements PaymentMethod{
		private String cardNumber;
		private String securityCode;
		
		/**
	     * Initializes a new Credit Card payment.
	     * @param cardNumber   The 6-digit card number.
	     * @param securityCode The 3-digit security code (CVV).
	     */
		
		public CreditCard(String cardNumber, String securityCode) {
			this.cardNumber = cardNumber;
			this.securityCode = securityCode;
			
		}

		@Override
		public Receipt processPayment(double total, Address address) {
		    String today = java.time.LocalDate.now().toString();
		    
		    String receiptText = String.format("%.2f paid by Credit Card %s on %s . Billing address: %s",
		            total, cardNumber, today, address.getFullAddress());
		            
		    return new Receipt(receiptText);
		}
	}

