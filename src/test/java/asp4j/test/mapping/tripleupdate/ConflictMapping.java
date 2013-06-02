package asp4j.test.mapping.tripleupdate;

import asp4j.lang.Atom;
import asp4j.mapping.object.atom.AtomOutputMappingBase;
import org.openrdf.model.Statement;

/**
 *
 * @author hbeck Jun 1, 2013
 */
public class ConflictMapping extends AtomOutputMappingBase<Conflict> {

//    @Override
//    public Atom asLangElem(Conflict conflict) throws Exception {
//        Constant typeConstant = new ConstantImpl(conflict.getType());        
//        Term stTerm1 = getBinding().asTerm(conflict.getStatement1());
//        Term stTerm2 = getBinding().asTerm(conflict.getStatement2());
//        return new AtomImpl(symbol(),new Term[]{typeConstant,stTerm1,stTerm2});
//    }

    @Override
    public Conflict asObject(Atom atom) throws Exception {
        Conflict conflict = new Conflict();
        conflict.setType(atom.getArg(0).symbol());
        conflict.setStatement1((Statement)getBinding().termAsObject(atom.getArg(1),Statement.class));
        conflict.setStatement2((Statement)getBinding().termAsObject(atom.getArg(2),Statement.class));
        return conflict;
    }

    @Override
    public Class<Conflict> clazz() {
        return Conflict.class;
    }

    @Override
    public String symbol() {
        return "confl";
    }

}
