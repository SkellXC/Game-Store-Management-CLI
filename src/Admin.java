/**
 * Represents a staff member with elevated privileges.
 * It currently shares the same structure as User.
 * However, the class is used to differentiate
 * roles at the login stage to send them to AdminCLI
 */
public class Admin extends User {
	
	public Admin(int userID, String name, Address address) {
		super(userID, name, address);
	}
}
