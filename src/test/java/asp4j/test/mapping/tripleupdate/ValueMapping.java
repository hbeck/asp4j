package asp4j.test.mapping.tripleupdate;

import asp4j.lang.Constant;
import asp4j.lang.ConstantImpl;
import asp4j.mapping.object.constant.ConstantMappingBase;
import org.openrdf.model.Literal;
import org.openrdf.model.Value;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 *
 * @author hbeck May 30, 2013
 */
public class ValueMapping extends ConstantMappingBase<Value> {

    private final String literalPostfix = "___lit";

    @Override
    public Constant asLangElem(Value value) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(value.stringValue().replaceAll(":", "_"));
        if (value instanceof Literal) {
            sb.append(literalPostfix);
        }
        return new ConstantImpl(sb.toString());
    }

    @Override
    public Value asObject(Constant c) throws Exception {
        String s = c.symbol();
        StringBuilder sb = new StringBuilder();
        if (s.endsWith(literalPostfix)) {
            int idx = s.indexOf(literalPostfix);
            sb.append(s.subSequence(0, idx));
            return ValueFactoryImpl.getInstance().createLiteral(sb.toString());
        } else {
            sb.append(s.replaceAll("_", ":"));
            return new URIImpl(sb.toString());
        }
    }

    @Override
    public Class<Value> clazz() {
        return Value.class;
    }
}
