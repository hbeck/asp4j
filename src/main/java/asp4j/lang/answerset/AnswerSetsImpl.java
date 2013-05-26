package asp4j.lang.answerset;

import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author hbeck
 * date 2013-05-19
 */
public class AnswerSetsImpl<T> implements AnswerSets<T> {

    private final List<AnswerSet<T>> answerSets;

    public AnswerSetsImpl(List<AnswerSet<T>> answerSets) {
        this.answerSets=ImmutableList.<AnswerSet<T>>builder().addAll(answerSets).build();
    }

    @Override
    public Set<T> cautiousConsequence() {
        Iterator<AnswerSet<T>> it = answerSets.iterator();
        Set<T> intersection = new HashSet();
        if (it.hasNext()) {
            intersection.addAll(it.next().atoms());
            while (it.hasNext()) {
                intersection.retainAll(it.next().atoms());
            }
        }
        return Collections.unmodifiableSet(intersection);
    }

    @Override
    public Set<T> braveConsequence() {
        Set<T> set = new HashSet();
        for (AnswerSet<T> answerSet : answerSets) {
            set.addAll(answerSet.atoms());
        }
        return set;
    }

    @Override
    public List<AnswerSet<T>> asList() {
        return answerSets;
    }
}
