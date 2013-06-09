package asp4j.test.gender;

import asp4j.mapping.annotations.DefEnumConstants;
import asp4j.mapping.annotations.MapWith;

/**
 *
 * @author hbeck Jun 9, 2013
 */
@DefEnumConstants
public enum Gender {
    
    @MapWith("female") FEMALE,
    @MapWith("male") MALE    

}
