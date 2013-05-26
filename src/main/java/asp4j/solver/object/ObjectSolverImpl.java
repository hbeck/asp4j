package asp4j.solver.object;

import asp4j.lang.Atom;
import asp4j.lang.answerset.AnswerSet;
import asp4j.lang.answerset.AnswerSetImpl;
import asp4j.lang.answerset.AnswerSets;
import asp4j.lang.answerset.AnswerSetsImpl;
import asp4j.mapping.MappingUtils;
import asp4j.program.Program;
import asp4j.program.ProgramBuilder;
import asp4j.solver.Solver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author hbeck date May 20, 2013
 */
public class ObjectSolverImpl implements ObjectSolver {

    private final Solver solver;

    public ObjectSolverImpl(Solver solver) {
        this.solver = solver;
    }

    @Override
    public <T> AnswerSets<Object> getAnswerSets(Program<T> program, FilterBinding binding) throws Exception {
        List<AnswerSet<Atom>> answerSets = lowLevelAnswerSets(program);
        Collection<String> predicateNames = binding.getFilterPredicateNames();
        List<AnswerSet<Object>> list = new ArrayList();
        for (AnswerSet<Atom> answerSet : answerSets) {
            Collection<Object> as = new ArrayList();
            for (Atom atom : answerSet.atoms()) {
                if (predicateNames.contains(atom.predicateName())) {
                    as.add(binding.asObject(atom));
                }
            }
            list.add(new AnswerSetImpl(as));
        }
        return new AnswerSetsImpl(list);
    }

    private <In> List<AnswerSet<Atom>> lowLevelAnswerSets(Program<In> program) throws Exception {
        ProgramBuilder<Atom> builder = new ProgramBuilder();
        builder.addFiles(program.getFiles());
        for (In input : program.getInput()) {
            builder.add(MappingUtils.asAtom(input));
        }
        return solver.getAnswerSets(builder.build()).asList();

    }
}
