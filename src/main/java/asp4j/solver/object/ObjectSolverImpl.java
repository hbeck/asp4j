package asp4j.solver.object;

import asp4j.lang.AnswerSet;
import asp4j.lang.Atom;
import asp4j.program.Program;
import asp4j.program.ProgramBuilder;
import asp4j.solver.ReasoningMode;
import asp4j.solver.Solver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 *
 * @author hbeck May 25, 2013
 */
public class ObjectSolverImpl implements ObjectSolver {

    private final Solver solver;

    public ObjectSolverImpl(Solver solver) {
        this.solver = solver;
    }

    @Override
    public List<AnswerSet<Object>> getAnswerSets(Program<?> program, Binding binding) throws Exception {
        return Collections.unmodifiableList(getAnswerSets(program, binding, null));
    }

    @Override
    public List<AnswerSet<Object>> getAnswerSets(Program<?> program, Binding binding, Filter filter) throws Exception {
        return Collections.unmodifiableList(computeAnswerSets(program, binding, filter));
    }

    @Override
    public Set<Object> getConsequence(Program<?> program, Binding binding, ReasoningMode mode) throws Exception {
        return getConsequence(program,binding,mode,null);
    }

    @Override
    public Set<Object> getConsequence(Program<?> program, Binding binding, ReasoningMode mode, Filter filter) throws Exception {
        Set<Atom> atoms = solver.getConsequence(computeLowLevelProgram(program, binding), mode);
        return binding.filterAndMap(atoms,filter);
    }

    private List<AnswerSet<Object>> computeAnswerSets(Program<?> program, Binding binding, Filter filter) throws Exception {
        List<AnswerSet<Atom>> answerSets = solver.getAnswerSets(computeLowLevelProgram(program, binding));
        List<AnswerSet<Object>> list = new ArrayList<>();
        for (AnswerSet<Atom> answerSet : answerSets) {
            list.add(binding.filterAndMap(answerSet,filter));
        }
        return list;
    }

    //
    private Program<Atom> computeLowLevelProgram(Program<?> program, Binding binding) throws Exception {
        ProgramBuilder<Atom> builder = new ProgramBuilder<>();
        builder.addFiles(program.getFiles());
        for (Object input : program.getInput()) {
            builder.add(binding.asAtom(input));
        }
        return builder.build();
    }
}
