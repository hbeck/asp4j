package asp4j.test.dlv.person.annotated;

import asp4j.lang.AnswerSet;
import asp4j.program.Program;
import asp4j.program.ProgramBuilder;
import asp4j.solver.ReasoningMode;
import asp4j.solver.SolverDLV;
import asp4j.solver.object.FilterBinding;
import asp4j.solver.object.FilterBinding;
import asp4j.solver.object.ObjectSolver;
import asp4j.solver.object.ObjectSolverImpl;
import java.io.File;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author hbeck date May 20, 2013
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

        //<readme example>

        Person person = new Person("id42");

        ObjectSolver solver = new ObjectSolverImpl(new SolverDLV());

        //"person.lp"
        Program<Object> program = new ProgramBuilder().add(new File(rulefile)).add(person).build();
        FilterBinding binding = new FilterBinding().add(Male.class).add(Female.class);
        List<AnswerSet<Object>> answerSets = solver.getAnswerSets(program, binding);

        // ==> answerSets.size() == 2
        assertEquals(2, answerSets.size());

        Set<Object> cautiousConsequence = solver.getConsequence(program, binding, ReasoningMode.CAUTIOUS);
        // ==> cautiousConsequence.isEmpty()
        assertTrue(cautiousConsequence.isEmpty());

        Set<Object> braveConsequence = solver.getConsequence(program, binding, ReasoningMode.BRAVE);
        // ==> braveConsequence.size() == 2
        // ==> braveConsequence.contains(new Female("id42"))
        // ==> braveConsequence.contains(new Male("id42"))
        assertEquals(2, braveConsequence.size());
        assertTrue(braveConsequence.contains(new Female("id42")));
        assertTrue(braveConsequence.contains(new Male("id42")));

        binding.add(Person.class);

        cautiousConsequence = solver.getConsequence(program, binding, ReasoningMode.CAUTIOUS);
        // ==> cautiousConsequence.size() == 1
        // ==> cautiousConsequence.contains(person)
        
        assertEquals(1, cautiousConsequence.size());
        assertTrue(cautiousConsequence.contains(person));
        
        //</readme example>
                
        braveConsequence = solver.getConsequence(program, binding, ReasoningMode.BRAVE);
        assertTrue(braveConsequence.contains(new Female("id42")));
        assertTrue(braveConsequence.contains(new Male("id42")));

        

    }
}
