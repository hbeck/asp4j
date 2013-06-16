package asp4j.test.gender.v1;

import asp4j.mapping.annotations.DefConstantSymbols;
import asp4j.mapping.annotations.MapAs;

/**
 *
 * @author hbeck Jun 9, 2013
 */
@DefConstantSymbols
public enum Gender {
    
    @MapAs("female") FEMALE,
    @MapAs("male") MALE    

}
