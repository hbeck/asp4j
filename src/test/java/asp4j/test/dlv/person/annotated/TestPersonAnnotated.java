package asp4j.test.dlv.person.annotated;

import asp4j.lang.answerset.AnswerSets;
import asp4j.program.ProgramBuilder;
import asp4j.solver.SolverDLV;
import asp4j.solver.object.FilterBindingImpl;
import asp4j.solver.object.ObjectSolver;
import asp4j.solver.object.ObjectSolverImpl;
import java.io.File;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author hbeck
 * date 2013-05-20
 */
public class TestPersonAnnotated {

    private final String rulefile = System.getProperty("user.dir") + "/src/test/dlv/person/person.lp";

    /**
     *
     * @throws Exception
     */
    @Test
    public void test() throws Exception {

        /*
         * RULES:
         * male(X) v female(X) :- person(X).
         * 
         * IN:
         * person(id42).
         * 
         * OUT:
         * 
         * {male(id42). person(id42).}
         * {female(id42). person(id42).}
         */

        final String id42 = "id42";
        Person person = new Person(id42);
        Male male = new Male(id42);
        Female female = new Female(id42);

        ObjectSolver objectSolver = new ObjectSolverImpl(new SolverDLV());

        ProgramBuilder<Object> builder = new ProgramBuilder();
        builder.add(new File(rulefile)).add(person);

        FilterBindingImpl binding = new FilterBindingImpl();
        binding.add(Male.class).add(Female.class);

        AnswerSets<Object> answerSets = objectSolver.getAnswerSets(builder.build(), binding);

        assertTrue(answerSets.cautiousConsequence().isEmpty());
        Set<Object> braveConsequence = answerSets.braveConsequence();
        assertEquals(2, braveConsequence.size());
        assertTrue(braveConsequence.contains(male));
        assertTrue(braveConsequence.contains(female));

        //
        binding.add(Person.class);
        answerSets = objectSolver.getAnswerSets(builder.build(), binding);

        assertTrue(answerSets.cautiousConsequence().contains(person));
        braveConsequence = answerSets.braveConsequence();
        assertTrue(braveConsequence.contains(male));
        assertTrue(braveConsequence.contains(female));

    }
}
