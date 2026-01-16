package messages;

import java.io.Serializable;

/**
 * Represents a request message to retrieve the current settings of the restaurant.
 * This request is typically sent to fetch configuration data such as opening hours, 
 * contact information, or general system policies.
 */
@SuppressWarnings("serial")
public class GetRestaurantSettingsRequest implements Serializable {
    
    /**
     * Constructs a new GetRestaurantSettingsRequest.
     */
    public GetRestaurantSettingsRequest() {
        // Default constructor
    }
}