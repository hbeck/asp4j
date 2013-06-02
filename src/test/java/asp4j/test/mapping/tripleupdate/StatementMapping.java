package asp4j.test.mapping.tripleupdate;

import asp4j.lang.Constant;
import asp4j.lang.Term;
import asp4j.lang.TermImpl;
import asp4j.mapping.object.term.TermMappingBase;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.StatementImpl;

/**
 *
 * @author hbeck May 30, 2013
 */
public class StatementMapping extends TermMappingBase<Statement> {
    
    @Override
    public Term asLangElem(Statement t) throws Exception {
        Resource subject = t.getSubject();
        URI predicate = t.getPredicate();
        Value object = t.getObject(); 
        Constant subjectConst = getBinding().asConstant(subject);
        Constant predicateConst = getBinding().asConstant(predicate);
        Constant objectConst = getBinding().asConstant(object);
        return new TermImpl("t",new Term[]{subjectConst,predicateConst,objectConst});
    }

    @Override
    public Statement asObject(Term t) throws Exception {
        Constant subjectConst = (Constant)t.getArg(0);
        Constant predicateConst = (Constant)t.getArg(1);
        Constant objectConst = (Constant)t.getArg(2);
        URI subject = (URI)getBinding().constantAsObject(subjectConst,URI.class);
        URI predicate = (URI)getBinding().constantAsObject(predicateConst,URI.class);
        Value object = (URI)getBinding().constantAsObject(objectConst,Value.class);
        return new StatementImpl(subject,predicate,object);
    }

    @Override
    public String symbol() {
        return "t";
    }

    @Override
    public Class<Statement> clazz() {
        return Statement.class;
    }

}
