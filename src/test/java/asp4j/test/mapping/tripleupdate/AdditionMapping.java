package asp4j.test.mapping.tripleupdate;

import asp4j.lang.Atom;
import asp4j.lang.AtomImpl;
import asp4j.lang.Term;
import asp4j.mapping.object.atom.AtomMappingBase;
import org.openrdf.model.Statement;

/**
 *
 * @author hbeck Jun 1, 2013
 */
public class AdditionMapping extends AtomMappingBase<Addition> {

    @Override
    public Atom asLangElem(Addition addition) throws Exception {
        Term statementTerm = getBinding().asTerm(addition.getStatement());
        return new AtomImpl(symbol(),new Term[]{statementTerm});
    }

    @Override
    public Addition asObject(Atom atom) throws Exception {
        Statement statement = (Statement)getBinding().termAsObject(atom.getArg(0),Statement.class);
        return new Addition(statement);
    }

    @Override
    public Class<Addition> clazz() {
        return Addition.class;
    }

    @Override
    public String symbol() {
        return "add";
    }

}
