package asp4j.lang.answerset;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
import java.util.Set;

/**
 * @author hbeck 
 * date Apr 14, 2013
 */
public class AnswerSetImpl<T> implements AnswerSet<T> {

    private final ImmutableSet<T> atoms;

    public AnswerSetImpl(Collection<T> atoms) {
        this.atoms=ImmutableSet.<T>builder().addAll(atoms).build();
    }
    
    @Override
    public Set<T> atoms() {
        return atoms;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        UnmodifiableIterator<T> iterator = atoms.iterator();
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
