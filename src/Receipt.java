/**
 * Represents the final record of a purchase.
 * Container allows the system to pass formatted
 *  strings from different payment methods back
 *  to the user.
 */
public class Receipt {
	private String receiptText;
	
	public Receipt(String receiptText) {
		this.receiptText = receiptText;
	}
	
	@Override
	public String toString() {
		return receiptText;
	}
	
}
