package asp4j.mapping;

import asp4j.lang.Constant;
import asp4j.lang.ConstantImpl;
import asp4j.mapping.object.constant.ConstantMapping;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;

/**
 *
 * @author hbeck Jun 1, 2013
 */
public class URIMapping implements ConstantMapping<URI> {

    @Override
    public Constant asLangElem(URI uri) throws MappingException {
        String replaced = uri.stringValue().replaceAll("//", "_slsl_").replaceAll("#","_hash_").replaceAll(":","_");
        return new ConstantImpl(replaced);
    }

    @Override
    public URI asObject(Constant term) throws MappingException {
        String replaced = term.symbol().replaceAll("_slsl_","//").replaceAll("_hash_","#").replaceAll("_",":");
        return new URIImpl(replaced);
    }

}
