package asp4j.test.tripleupdate.term.obj;

import asp4j.mapping.annotations.DefEnumConstants;
import asp4j.mapping.annotations.MapWith;

/**
 * predicate in a rdf triple. used to test input enum
 *
 * @author hbeck Jun 9, 2013
 */
@DefEnumConstants
public enum Predicate {
    
    owl_inverseOf("owl:inverseOf"),
    rdf_type("rdf:type"),
    urn_hasColor("urn:hasColor"),
    urn_colorOf("urn:colorOf"),
    @MapWith("skos_broader") broader("skos:broader"),
    @MapWith("skos_narrower") narrower("skos:narrower");
    
    private String s;    
    
    private Predicate(String s) {
        this.s=s;
    }
    
    @Override
    public String toString() {
        return s;
    }

}
