/**
 * Unified set of status codes for the program
 * These results allow the ShoppingCart and Customer
 * classes to communicate more specific outcomes.
 */
public enum ServiceResult {
	/** The operation completed as expected. */
	SUCCESS,
	
	/** The requested item could not be located. */
    NOT_FOUND,
    
    /** User tried to remove more items than in their basket*/
    EXCEEDED_QUANTITY,
    
    /** Store does not have enough stock*/
    INSUFFICIENT_QUANTITY,
    
    /** Input was invalid, such as a negative value*/
    INVALID_INPUT,
    
    /** The user intentionally aborted the operation*/
    CANCELLED
}
