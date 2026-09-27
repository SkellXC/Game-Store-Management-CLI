/**
 * Represents a physical address associated with a Customer.
 * Used primarily to format the billing address for checkout receipts.
 */
public class Address {
	private String postcode;
	private String city;
	private int houseNumber;
	
	public Address(String postcode, String city, int houseNumber) {
		this.postcode = postcode;
		this.city = city;
		this.houseNumber = houseNumber;
		
	}
	public String getPostcode() {
		return postcode;
	}
	public String getCity() {
		return city;
	}
	public int getHouseNumber() {
		return houseNumber;
	}
	public String getFullAddress() {
	    return String.format("%d, %s, %s", houseNumber, city, postcode);
	}
}
