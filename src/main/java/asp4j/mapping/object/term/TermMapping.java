package asp4j.mapping.object.term;

import asp4j.lang.Term;
import asp4j.mapping.object.Mapping;

/**
 *
 * @author hbeck May 31, 2013
 */
public interface TermMapping<T> extends TermInputMapping<T>, TermOutputMapping<T>, Mapping<T,Term> {

}
