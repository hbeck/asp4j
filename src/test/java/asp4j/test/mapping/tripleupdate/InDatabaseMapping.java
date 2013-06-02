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
public class InDatabaseMapping extends AtomMappingBase<InDatabase> {

    @Override
    public Atom asLangElem(InDatabase db) throws Exception {
        Term statementTerm = getBinding().asTerm(db.getStatement());
        return new AtomImpl(symbol(),new Term[]{statementTerm});
    }

    @Override
    public InDatabase asObject(Atom atom) throws Exception {
        Statement statement = (Statement)getBinding().termAsObject(atom.getArg(0),Statement.class);
        return new InDatabase(statement);
    }

    @Override
    public Class<InDatabase> clazz() {
        return InDatabase.class;
    }

    @Override
    public String symbol() {
        return "db";
    }

}
