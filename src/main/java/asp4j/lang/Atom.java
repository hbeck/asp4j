package asp4j.lang;

/**
 *
 * @author hbeck
 * date May 14, 2013
 */
public interface Atom extends HasPredicateName {   

    public int arity();
    public String getArg(int idx);
            
}
