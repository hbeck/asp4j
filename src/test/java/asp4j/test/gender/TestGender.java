package asp4j.test.gender;

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
 * @author hbeck Jun 9, 2013
 */
public class TestGender {

    private final String rulefile_common = System.getProperty("user.dir") + "/src/test/common/gender.lp";
    private final String rulefile_dlv = System.getProperty("user.dir") + "/src/test/dlv/gender.lp";
    private final String rulefile_clingo = System.getProperty("user.dir") + "/src/test/clingo/gender.lp";

    @Test
    public void test1() throws Exception {
        test(new SolverDLV(), rulefile_dlv);
        test(new SolverClingo(), rulefile_clingo);
        test(new SolverDLV(), rulefile_common);
        test(new SolverClingo(), rulefile_common);
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
     * gender(X,female) :- person(X), not gender(X,male).
     * gender(X,male)   :- person(X), not gender(X,female).
     * 
     * IN:
     * person(id42).
     * 
     * OUT:
     * 
     * {gender(id42,female). person(id42).}
     * {gender(id42,male). person(id42).}
     */
    public void test(Solver externalSolver, String rulefile) throws Exception {
        Person person = new Person("id42");

        ObjectSolver solver = new ObjectSolverImpl(externalSolver);
        Program<Object> program = new ProgramBuilder().add(new File(rulefile)).add(person).build();
        Filter filter = new Filter().add(PersonGender.class);
        
        List<AnswerSet<Object>> answerSets = solver.getAnswerSets(program, filter);

        // ==> answerSets.size() == 2
        assertEquals(2, answerSets.size());

        Set<Object> cautiousConsequence = solver.getConsequence(program, ReasoningMode.CAUTIOUS);

        // ==> cautiousConsequence.size() == 1
        assertEquals(1, cautiousConsequence.size());
        // ==> cautiousConsequence.contains(person);
        assertTrue(cautiousConsequence.contains(person));

        cautiousConsequence = solver.getConsequence(program, ReasoningMode.CAUTIOUS, filter);

        // ==> cautiousConsequence.isEmpty()
        assertTrue(cautiousConsequence.isEmpty());

        Set<Object> braveConsequence = solver.getConsequence(program, ReasoningMode.BRAVE, filter);

        // ==> braveConsequence.size() == 2
        assertEquals(2, braveConsequence.size());
        // ==> braveConsequence.contains(new PersonGender("id42", Gender.FEMALE))
        assertTrue(braveConsequence.contains(new PersonGender("id42", Gender.FEMALE)));
        // ==> braveConsequence.contains(new PersonGender("id42", Gender.MALE))
        assertTrue(braveConsequence.contains(new PersonGender("id42", Gender.MALE)));

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

        Filter filter = new Filter().add(PersonGender.class);
        answerSets = solver.getAnswerSets(program, filter);

        PersonGender female = new PersonGender("id42", Gender.FEMALE);
        PersonGender male = new PersonGender("id42", Gender.MALE);

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

        Filter filter = new Filter().add(PersonGender.class);
        cons = solver.getConsequence(program, ReasoningMode.CAUTIOUS, filter);
        assertTrue(cons.isEmpty());

        //

        filter.add(Person.class);
        cons = solver.getConsequence(program, ReasoningMode.CAUTIOUS, filter);
        assertEquals(1, cons.size());
        assertEquals(person, cons.iterator().next());

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

        Filter filter = new Filter().add(PersonGender.class);
        cons = solver.getConsequence(program, ReasoningMode.BRAVE, filter);
        
        PersonGender female = new PersonGender("id42", Gender.FEMALE);
        PersonGender male = new PersonGender("id42", Gender.MALE);

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

    }
}