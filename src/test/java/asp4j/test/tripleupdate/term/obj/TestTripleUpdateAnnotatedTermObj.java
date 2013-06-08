package asp4j.test.tripleupdate.term.obj;

import asp4j.lang.AnswerSet;
import asp4j.program.Program;
import asp4j.program.ProgramBuilder;
import asp4j.solver.ReasoningMode;
import asp4j.solver.Solver;
import asp4j.solver.SolverClingo;
import asp4j.solver.SolverDLV;
import asp4j.solver.object.Binding;
import asp4j.solver.object.Filter;
import asp4j.solver.object.ObjectSolver;
import asp4j.solver.object.ObjectSolverImpl;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;

/**
 *
 * Triple Update. Mapping via Annotations, using terms. URIs and enums.
 *
 * @author hbeck June 8, 2013
 */
public class TestTripleUpdateAnnotatedTermObj {

    private final String rulefile_common = System.getProperty("user.dir") + "/src/test/common/triple-update-term-obj.lp";
    
    @Test
    public void test_dlv_common() throws Exception {
        test(new SolverDLV(), rulefile_common);
    }

    @Test
    public void test_clingo_common() throws Exception {
        test(new SolverClingo(), rulefile_common);
    }

    @Test
    public void test_conflict_dlv_common() throws Exception {
        test_conflict(new SolverDLV(), rulefile_common);
    }

    @Test
    public void test_conflict_clingo_common() throws Exception {
        test_conflict(new SolverClingo(), rulefile_common);
    }
    
    @Test
    public void test_conflict_skos_dlv_common() throws Exception {
        test_conflict_skos(new SolverDLV(), rulefile_common);
    }

    @Test
    public void test_conflict_skos_clingo_common() throws Exception {
        test_conflict_skos(new SolverClingo(), rulefile_common);
    }

    public void test(Solver externalSolver, String rulefile) throws Exception {

        Triple car_hasColor_blue = triple("urn:car", "urn:hasColor", "urn:blue");
        Triple blue_colorOf_car = triple("urn:blue", "urn:colorOf", "urn:car");
        Triple hasColor_inverseOf_colorOf = triple("urn:hasColor", "owl:inverseOf", "urn:colorOf");

        ObjectSolver solver = new ObjectSolverImpl(externalSolver);
        Binding binding = new Binding().add(URI.class, new URIMapping());

        Program<Object> program = new ProgramBuilder<>()
                .add(new File(rulefile))
                .add(new Addition(car_hasColor_blue))
                .add(new Addition(hasColor_inverseOf_colorOf))
                .build();

        List<AnswerSet<Object>> as = solver.getAnswerSets(program, binding);

        assertEquals(1, as.size());
        assertTrue(as.get(0).atoms().contains(new Addition(blue_colorOf_car)));

        Set<Object> cautiousConsequence = solver.getConsequence(program, ReasoningMode.CAUTIOUS, binding);
        Set<Object> braveConsequence = solver.getConsequence(program, ReasoningMode.BRAVE, binding);
        assertTrue(CollectionUtils.isEqualCollection(cautiousConsequence, braveConsequence));
        assertEquals(3,cautiousConsequence.size());

    }

    public void test_conflict(Solver externalSolver, String rulefile) throws Exception {
        /*
         * db(hasColor,inverseOf,colorOf).
         * db(hasColor,type,single).
         * db(car,hasColor,blue).
         * db(blue,colorOf,car).
         * add(red,colorOf,car).
         *
         * --> add(car,hasColor,red).
         * --> confl(single_violation(car,hasColor,blue,red))
         */

        Triple hasColor_inverseOf_colorOf = triple("urn:hasColor", "owl:inverseOf", "urn:colorOf");
        Triple hasColor_type_single = triple("urn:hasColor", "rdf:type", "ex:single");
        Triple car_hasColor_blue = triple("urn:car", "urn:hasColor", "urn:blue");
        Triple blue_colorOf_car = triple("urn:blue", "urn:colorOf", "urn:car");
        Triple red_colorOf_car = triple("urn:red", "urn:colorOf", "urn:car");
        Triple car_hasColor_red = triple("urn:car", "urn:hasColor", "urn:red");

        ObjectSolver solver = new ObjectSolverImpl(externalSolver);
        Binding binding = new Binding().add(URI.class, new URIMapping());

        Program<Object> program = new ProgramBuilder<>()
                .add(new File(rulefile))
                .add(new InDatabase(hasColor_inverseOf_colorOf))
                .add(new InDatabase(hasColor_type_single))
                .add(new InDatabase(car_hasColor_blue))
                .add(new InDatabase(blue_colorOf_car))
                .add(new Addition(red_colorOf_car))
                .build();

        Filter filter = new Filter(Conflict.class);

        Set<Object> cautiousConsequence = solver.getConsequence(program, ReasoningMode.CAUTIOUS, binding, filter);
        Set<Object> braveConsequence = solver.getConsequence(program, ReasoningMode.BRAVE, binding, filter);
        assertEquals(1,cautiousConsequence.size());
        assertTrue(CollectionUtils.isEqualCollection(cautiousConsequence, braveConsequence));

        Conflict expected = new Conflict();
        //confl(single_violation,car,hasColor,blue,red))
        expected.setType(ConflictType.single_violation);
        expected.setT1(car_hasColor_blue);
        expected.setT2(car_hasColor_red);

        List<AnswerSet<Object>> as = solver.getAnswerSets(program, binding, filter);
        
        assertEquals(1, as.size());        
        assertEquals(1, as.get(0).atoms().size());
        assertTrue(as.get(0).atoms().contains(expected));
        assertEquals(expected,cautiousConsequence.iterator().next());

        //
        filter = new Filter(ConflictCategory.class);
        Set<Object> cons = solver.getConsequence(program, ReasoningMode.CAUTIOUS, binding, filter);
        assertEquals(1, cons.size());
        assertEquals(ConflictCategory.conflict, (ConflictCategory) cons.iterator().next());
        
        //
//        filter = new Filter(ConflictCategory.some_conflict);
//        cons = solver.getConsequence(program, ReasoningMode.CAUTIOUS, binding, filter);
//        assertEquals(1, cons.size());
//        assertEquals(ConflictCategory.some_conflict, (ConflictCategory) cons.iterator().next());

    }
    
    public void test_conflict_skos(Solver externalSolver, String rulefile) throws Exception {
        
        /*
         * db(t(vienna,skos_broader,austria)).  % (1)
         * add(t(vienna,skos_narrower,austria)). % (2)
         * 
         * --> add(t(austria,skos_narrower,vienna))  (1)
         * --> add(t(austria,skos_broader,vienna))  (2)
         * --> confl(narrower_broader,t(austria,skos_narrower,vienna),t(austria,skos_broader,vienna))  
         * --> some_conflict
         * --> some_skos_conflict
         */

        Triple vienna_broader_austria = triple("urn:vienna", "skos:broader", "urn:austria");
        Triple vienna_narrower_austria = triple("urn:vienna", "skos:narrower", "urn:austria");

        ObjectSolver solver = new ObjectSolverImpl(externalSolver);
        Binding binding = new Binding().add(URI.class, new URIMapping());

        Program<Object> program = new ProgramBuilder<>()
                .add(new File(rulefile))
                .add(new InDatabase(vienna_broader_austria))
                .add(new Addition(vienna_narrower_austria))
                .build();

        Filter filter = new Filter(Conflict.class);

        Set<Object> cautiousConsequence = solver.getConsequence(program, ReasoningMode.CAUTIOUS, binding, filter);
        Set<Object> braveConsequence = solver.getConsequence(program, ReasoningMode.BRAVE, binding, filter);
        assertTrue(CollectionUtils.isEqualCollection(cautiousConsequence, braveConsequence));
        assertEquals(1,cautiousConsequence.size());
        
        Triple austria_narrower_vienna = triple("urn:austria", "skos:narrower", "urn:vienna");
        Triple austria_broader_vienna = triple("urn:austria", "skos:broader", "urn:vienna");
        Conflict expected = new Conflict();
        //confl(single_violation,car,hasColor,blue,red))
        expected.setType(ConflictType.narrower_broader_clash);
        expected.setT1(austria_narrower_vienna);
        expected.setT2(austria_broader_vienna);

        List<AnswerSet<Object>> as = solver.getAnswerSets(program, binding, filter);
        assertTrue(as.get(0).atoms().contains(expected));
        assertEquals(1, as.size());
        assertEquals(1, as.get(0).atoms().size());

        //
        filter = new Filter(ConflictCategory.class);
        Set<Object> cons = solver.getConsequence(program, ReasoningMode.CAUTIOUS, binding, filter);
        assertEquals(2, cons.size());        
        Iterator<Object> it = cons.iterator();
        ConflictCategory category = (ConflictCategory) it.next();
        if (category.equals(ConflictCategory.conflict)) {
            assertEquals(ConflictCategory.skos,(ConflictCategory)it.next());
        } else {
            assertEquals(ConflictCategory.skos,category);
            assertEquals(ConflictCategory.conflict,(ConflictCategory)it.next());
        }        
        
        //
//        filter = new Filter(ConflictCategory.some_skos_conflict);
//        cons = solver.getConsequence(program, ReasoningMode.CAUTIOUS, binding, filter);
//        assertEquals(1, cons.size());
//        assertEquals(ConflictCategory.some_conflict, (ConflictCategory) cons.iterator().next());

    }

    private static Triple triple(String subject, String predicate, String object) {
        return new Triple(new URIImpl(subject), new URIImpl(predicate), new URIImpl(object));
    }
}
