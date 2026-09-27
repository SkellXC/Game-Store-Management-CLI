/*
 * Abstract base class representing any person in the system.
 */
public abstract class User{
	private int userID;
	private String name;
	private Address address;
	
	/**
	 * Initializes the core identity of a user.
	 * @param userID  The unique identification number.
	 * @param name    The full name of the user.
	 * @param address The physical address associated with this account.
	 */
	public User(int userID, String name, Address address) {
		this.userID = userID;
		this.name = name;
		this.address = address;
	}
	public int getUserID() {
		return userID;
	}
	public String getName() { 
		return name;
	}
	
	public Address getAddress() {
		return address;
	}
}