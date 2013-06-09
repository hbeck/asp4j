package asp4j.mapping.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Explicit mapping name for enum constants, if their name is not used.
 *
 * @author hbeck June 8, 2013
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MapAs {
    
    /**
     * @return mapping name for enum constant (instead of default name)
     */
    String value();    
    
}
