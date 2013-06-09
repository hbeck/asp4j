package asp4j.solver.object;

import asp4j.lang.AnswerSet;
import asp4j.lang.Atom;
import asp4j.mapping.MappingException;
import asp4j.program.Program;
import asp4j.program.ProgramBuilder;
import asp4j.solver.ReasoningMode;
import asp4j.solver.Solver;
import asp4j.solver.SolverException;
import java.util.Collections;
import java.util.HashSet;
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
    public List<AnswerSet<Object>> getAnswerSets(Program<Object> program) throws SolverException {
        return Collections.unmodifiableList(computeAnswerSets(program, new Binding(), new Filter()));
    }

    @Override
    public List<AnswerSet<Object>> getAnswerSets(Program<Object> program, Binding binding) throws SolverException {
        return Collections.unmodifiableList(computeAnswerSets(program, binding, new Filter()));
    }

    @Override
    public List<AnswerSet<Object>> getAnswerSets(Program<Object> program, Filter filter) throws SolverException {
        return Collections.unmodifiableList(computeAnswerSets(program, new Binding(), filter));
    }

    @Override
    public List<AnswerSet<Object>> getAnswerSets(Program<Object> program, Binding binding, Filter filter) throws SolverException {
        return Collections.unmodifiableList(computeAnswerSets(program, binding, filter));
    }

    @Override
    public Set<Object> getConsequence(Program<Object> program, ReasoningMode mode) throws SolverException {
        return getConsequence(program, mode, new Binding(), new Filter());
    }

    @Override
    public Set<Object> getConsequence(Program<Object> program, ReasoningMode mode, Binding binding) throws SolverException {
        return getConsequence(program, mode, binding, new Filter());
    }

    @Override
    public Set<Object> getConsequence(Program<Object> program, ReasoningMode mode, Filter filter) throws SolverException {
        return getConsequence(program, mode, new Binding(), filter);
    }

    @Override
    public Set<Object> getConsequence(Program<Object> program, ReasoningMode mode, Binding binding, Filter filter) throws SolverException {
        Program<Atom> atomProgram = getAtomProgram(program, binding, filter);
        Set<Atom> atoms = solver.getConsequence(atomProgram, mode);
        try {
            return binding.filterAndMap(atoms, filter);
        } catch (MappingException e) {
            throw new SolverException(e);
        }
    }

    private List<AnswerSet<Object>> computeAnswerSets(Program<Object> program, Binding binding, Filter filter) throws SolverException {
        Program<Atom> atomProgram = getAtomProgram(program, binding, filter);
        List<AnswerSet<Atom>> answerSets = solver.getAnswerSets(atomProgram);
        try {
            return binding.filterAndMap(answerSets, filter);
        } catch (MappingException e) {
            throw new SolverException(e);
        }
    }

    private Program<Atom> getAtomProgram(Program<Object> program, Binding binding, Filter filter) throws SolverException {
        prepareIO(program, binding, filter);
        ProgramBuilder<Atom> builder = new ProgramBuilder<>();
        builder.addFiles(program.getFiles());
        try {
            for (Object input : program.getInput()) {
                builder.add((Atom) binding.mapAsLangElem(input));
            }
        } catch (MappingException e) {
            throw new SolverException(e);
        }
        return builder.build();
    }

    private void prepareIO(Program<Object> program, Binding binding, Filter filter) throws SolverException {
        Set<Class<?>> inputClasses = new HashSet<>();
        for (Object object : program.getInput()) {
            inputClasses.add(object.getClass());
        }
        try {
            //input
            binding.addAll(inputClasses);
            //output
            Set<Class<?>> filterClasses = filter.getClasses();
            if (filterClasses.isEmpty()) {
                filter.addAll(inputClasses);
            } else {
                binding.addAll(filterClasses);
            }
        } catch (MappingException e) {
            throw new SolverException(e);
        }
    }
}
