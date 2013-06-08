package asp4j.lang;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
 * @author hbeck 
 *  Apr 14, 2013
 */
public class AnswerSetImpl<T> implements AnswerSet<T> {

    private final Set<T> atoms;

    public AnswerSetImpl(Set<T> atoms) {
        this.atoms=Collections.unmodifiableSet(atoms);
    }
    
    @Override
    public Set<T> atoms() {
        return atoms;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        Iterator<T> iterator = atoms.iterator();
        if (iterator.hasNext()) {
            sb.append(iterator.next());
        }
        while (iterator.hasNext()) {
            sb.append(',').append(iterator.next());
        }
        sb.append('}');
        return sb.toString();
    }    
    
}
