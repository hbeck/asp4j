package asp4j.solver.object;

import asp4j.lang.AnswerSet;
import asp4j.lang.AnswerSetImpl;
import asp4j.lang.Atom;
import asp4j.mapping.MappingUtils;
import asp4j.program.Program;
import asp4j.program.ProgramBuilder;
import asp4j.solver.ReasoningMode;
import asp4j.solver.Solver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author hbeck 
 * date May 25, 2013
 */
public class ObjectSolverImpl implements ObjectSolver {

    private final Solver solver;

    public ObjectSolverImpl(Solver solver) {
        this.solver = solver;
    }

    @Override
    public List<AnswerSet<Object>> getAnswerSets(Program<?> program, FilterBinding binding) throws Exception {
        List<AnswerSet<Atom>> answerSets = solver.getAnswerSets(getLowLevelProgram(program));
        List<AnswerSet<Object>> list = new ArrayList();
        for (AnswerSet<Atom> answerSet : answerSets) {
            list.add(filterAndMap(answerSet,binding));
        }
        return Collections.unmodifiableList(list);
    }

    private AnswerSet<Object> filterAndMap(AnswerSet<Atom> answerSet, FilterBinding binding) throws Exception {
        Set<Object> as = new HashSet();
        for (Atom atom : answerSet.atoms()) {
            if (binding.getFilterPredicateNames().contains(atom.predicateName())) {
                as.add(binding.asObject(atom));
            }
        }
        return new AnswerSetImpl(as);
    }

    private <In> Program<Atom> getLowLevelProgram(Program<In> program) throws Exception {
        ProgramBuilder<Atom> builder = new ProgramBuilder();
        builder.addFiles(program.getFiles());
        for (In input : program.getInput()) {
            builder.add(MappingUtils.asAtom(input));
        }
        return builder.build();
    }

    @Override
    public Set<Object> getConsequence(Program<?> program, FilterBinding binding, ReasoningMode mode) throws Exception {
        Set<Atom> atoms = solver.getConsequence(getLowLevelProgram(program), mode);
        return filterAndMap(atoms,binding);
    }
    
    private Set<Object> filterAndMap(Set<Atom> atoms, FilterBinding binding) throws Exception {
        Set<Object> set = new HashSet();
        for (Atom atom : atoms) {
            if (binding.getFilterPredicateNames().contains(atom.predicateName())) {
                set.add(binding.asObject(atom));
            }
        }
        return Collections.unmodifiableSet(set);
    }
}
