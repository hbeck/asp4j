package asp4j.test.mapping.tripleupdate;

import asp4j.lang.Constant;
import asp4j.lang.ConstantImpl;
import asp4j.mapping.object.constant.ConstantMappingBase;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;

/**
 *
 * @author hbeck Jun 1, 2013
 */
public class URIMapping extends ConstantMappingBase<URI> {

    @Override
    public Constant asLangElem(URI uri) throws Exception {
        String replaced = uri.stringValue().replaceAll("//", "_slsl_").replaceAll("#","_hash_").replaceAll(":","_");
        return new ConstantImpl(replaced);
    }

    @Override
    public URI asObject(Constant constant) throws Exception {
        String replaced = constant.symbol().replaceAll("_slsl_","//").replaceAll("_hash_","#").replaceAll("_",":");
        return new URIImpl(replaced);
    }

    @Override
    public Class<URI> clazz() {
        return URI.class;
    }

}
