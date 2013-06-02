package asp4j.test.annotated.person;

import asp4j.lang.AnswerSet;
import asp4j.program.Program;
import asp4j.program.ProgramBuilder;
import asp4j.solver.ReasoningMode;
import asp4j.solver.Solver;
import asp4j.solver.SolverClingo;
import asp4j.solver.SolverDLV;
import asp4j.solver.object.Filter;
import asp4j.solver.object.ObjectSolver;
import asp4j.solver.object.ObjectSolverImpl;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author hbeck date May 20, 2013
 */
public class TestPersonAnnotated {

    private final String rulefile_common = System.getProperty("user.dir") + "/src/test/common/person.lp";
    private final String rulefile_dlv = System.getProperty("user.dir") + "/src/test/dlv/person.lp";
    private final String rulefile_clingo = System.getProperty("user.dir") + "/src/test/clingo/person.lp";

    @Test
    public void test_readme() throws Exception {
        readme(new SolverDLV(), rulefile_dlv);
        readme(new SolverClingo(), rulefile_clingo);
        readme(new SolverDLV(), rulefile_common);
        readme(new SolverClingo(), rulefile_common);
    }

    @Test
    public void test_dlv() throws Exception {
        test_answersets(new SolverDLV(), rulefile_dlv);
        test_cautiouscons(new SolverDLV(), rulefile_dlv);
        test_bravecons(new SolverDLV(), rulefile_dlv);
    }

    @Test
    public void test_clingo() throws Exception {
        test_answersets(new SolverClingo(), rulefile_clingo);
        test_cautiouscons(new SolverClingo(), rulefile_clingo);
        test_bravecons(new SolverClingo(), rulefile_clingo);
    }

    @Test
    public void test_dlv_common() throws Exception {
        test_answersets(new SolverDLV(), rulefile_common);
        test_cautiouscons(new SolverDLV(), rulefile_common);
        test_bravecons(new SolverDLV(), rulefile_common);

    }

    @Test
    public void test_clingo_common() throws Exception {
        test_answersets(new SolverClingo(), rulefile_common);
        test_cautiouscons(new SolverClingo(), rulefile_common);
        test_bravecons(new SolverClingo(), rulefile_common);
    }

    /*
     * RULES (common; dlv and clingo similar)
     * 
     * female(X) :- person(X), not male(X).
     * male(X)   :- person(X), not female(X).
     * 
     * IN:
     * person(id42).
     * 
     * OUT:
     * 
     * {male(id42). person(id42).}
     * {female(id42). person(id42).}
     */
    public void readme(Solver externalSolver, String rulefile) throws Exception {
        Person person = new Person("id42");


        ObjectSolver solver = new ObjectSolverImpl(externalSolver);

        Program<Object> program = new ProgramBuilder().add(new File(rulefile)).add(person).build();
        Filter filter = new Filter().add(Male.class).add(Female.class);
        List<AnswerSet<Object>> answerSets = solver.getAnswerSets(program, filter);

        // ==> answerSets.size() == 2
        assertEquals(2,answerSets.size());

        Set<Object> cautiousConsequence = solver.getConsequence(program, ReasoningMode.CAUTIOUS);

        // ==> cautiousConsequence.size() == 1
        assertEquals(1,cautiousConsequence.size());
        // ==> cautiousConsequence.contains(person);
        assertTrue(cautiousConsequence.contains(person));

        cautiousConsequence = solver.getConsequence(program, ReasoningMode.CAUTIOUS, filter);

        // ==> cautiousConsequence.isEmpty()
        assertTrue(cautiousConsequence.isEmpty());

        Set<Object> braveConsequence = solver.getConsequence(program, ReasoningMode.BRAVE, filter);

        // ==> braveConsequence.size() == 2
        assertEquals(2,braveConsequence.size());
        // ==> braveConsequence.contains(new Female("id42"))
        assertTrue(braveConsequence.contains(new Female("id42")));
        // ==> braveConsequence.contains(new Male("id42"))
        assertTrue(braveConsequence.contains(new Male("id42")));

    }

    public void test_answersets(Solver externalSolver, String rulefile) throws Exception {

        Person person = new Person("id42");
        ObjectSolver solver = new ObjectSolverImpl(externalSolver);
        Program<Object> program = new ProgramBuilder<>().add(new File(rulefile)).add(person).build();

        //

        List<AnswerSet<Object>> answerSets = solver.getAnswerSets(program);

        assertEquals(2, answerSets.size());
        Set<Object> as0 = answerSets.get(0).atoms();
        Set<Object> as1 = answerSets.get(1).atoms();
        assertTrue(CollectionUtils.isEqualCollection(as0, as1));
        assertEquals(1, as0.size());
        assertEquals(person, as0.iterator().next());

        //

        Filter filter = new Filter().add(Male.class).add(Female.class);
        answerSets = solver.getAnswerSets(program, filter);

        Male male = new Male("id42");
        Female female = new Female("id42");

        assertEquals(2, answerSets.size());
        as0 = answerSets.get(0).atoms();
        as1 = answerSets.get(1).atoms();
        assertFalse(CollectionUtils.isEqualCollection(as0, as1));
        assertEquals(1, as0.size());
        assertEquals(1, as1.size());
        Object obj0 = as0.iterator().next();
        Object obj1 = as1.iterator().next();
        if (obj0.equals(male)) {
            assertEquals(obj1, female);
        } else {
            assertEquals(obj1, male);
            assertEquals(obj0, female);
        }

        //

        filter.add(Person.class);
        answerSets = solver.getAnswerSets(program, filter);
        as0 = answerSets.get(0).atoms();
        as1 = answerSets.get(1).atoms();
        assertFalse(CollectionUtils.isEqualCollection(as0, as1));
        assertTrue(as0.contains(person));
        assertTrue(as1.contains(person));
        assertEquals(2, as0.size());
        assertEquals(2, as1.size());

        //

        filter = new Filter(Female.class);
        answerSets = solver.getAnswerSets(program, filter);

        assertEquals(2, answerSets.size());
        as0 = answerSets.get(0).atoms();
        as1 = answerSets.get(1).atoms();
        assertFalse(CollectionUtils.isEqualCollection(as0, as1));
        if (as0.isEmpty()) {
            assertEquals(1, as1.size());
            assertEquals(female, as1.iterator().next());
        } else {
            assertTrue(as1.isEmpty());
            assertEquals(1, as0.size());
            assertEquals(female, as0.iterator().next());
        }

    }

    public void test_cautiouscons(Solver externalSolver, String rulefile) throws Exception {

        Person person = new Person("id42");
        ObjectSolver solver = new ObjectSolverImpl(externalSolver);
        Program<Object> program = new ProgramBuilder<>().add(new File(rulefile)).add(person).build();

        //

        Set<Object> cons = solver.getConsequence(program, ReasoningMode.CAUTIOUS);

        assertEquals(1, cons.size());
        assertEquals(person, cons.iterator().next());

        //

        Filter filter = new Filter().add(Male.class).add(Female.class);
        cons = solver.getConsequence(program, ReasoningMode.CAUTIOUS, filter);
        assertTrue(cons.isEmpty());

        //

        filter.add(Person.class);
        cons = solver.getConsequence(program, ReasoningMode.CAUTIOUS, filter);
        assertEquals(1, cons.size());
        assertEquals(person, cons.iterator().next());

        //

        filter = new Filter(Female.class);
        cons = solver.getConsequence(program, ReasoningMode.CAUTIOUS, filter);
        assertTrue(cons.isEmpty());

    }

    public void test_bravecons(Solver externalSolver, String rulefile) throws Exception {

        Person person = new Person("id42");
        ObjectSolver solver = new ObjectSolverImpl(externalSolver);
        Program<Object> program = new ProgramBuilder<>().add(new File(rulefile)).add(person).build();

        //

        Set<Object> cons = solver.getConsequence(program, ReasoningMode.BRAVE);

        assertEquals(1, cons.size());
        assertEquals(person, cons.iterator().next());

        //

        Filter filter = new Filter().add(Male.class).add(Female.class);
        cons = solver.getConsequence(program, ReasoningMode.BRAVE, filter);
        Male male = new Male("id42");
        Female female = new Female("id42");
        assertEquals(2, cons.size());
        Iterator<?> it = cons.iterator();
        Object first = it.next();
        if (first.equals(male)) {
            assertEquals(female, it.next());
        } else {
            assertEquals(female, first);
            assertEquals(male, it.next());
        }

        //

        filter.add(Person.class);
        cons = solver.getConsequence(program, ReasoningMode.BRAVE, filter);
        assertEquals(3, cons.size());
        assertTrue(cons.contains(person));
        assertTrue(cons.contains(female));
        assertTrue(cons.contains(male));

        //

        filter = new Filter(Female.class);
        cons = solver.getConsequence(program, ReasoningMode.BRAVE, filter);
        assertEquals(1, cons.size());
        assertEquals(female, cons.iterator().next());

    }
}