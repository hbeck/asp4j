# asp4j

Answer Set Programming solver interface for Java

## Example

#### Logic program `person.lp`

    female(X) v male(X) :- person(X).

#### Java classes (beans)
  
    @Predicate("person")
    public class Person {
    
        protected String id;

        public Person() {
        }

        public Person(String id) {
            this.id = id;
        }
    
        @Arg(0)
        public String getId() {
            return id;
        }
    
        public void setId(String id) {
            this.id = id;
        }

        //equals ...
  
    }

    @Predicate("female")
    public class Female extends Person {
    }
  
    @Predicate("male")
    public class Male extends Person {
    }

#### Using a solver ([DLV](http://www.dlvsystem.com/))

    Person person = new Person("id42");
    
    ObjectSolver objectSolver = new ObjectSolverImpl(new SolverDLV());
    
    ProgramBuilder<Object> builder = new ProgramBuilder();
    builder.add(new File("person.lp")).add(person);
    
    FilterBinding binding = new FilterBindingImpl();
    binding.add(Male.class).add(Female.class);
    
    AnswerSets<Object> answerSets = objectSolver.getAnswerSets(builder.build(), binding);

    // ==> answerSets.asList().size()==2
    // ==> answerSets.asList().contains(new Male("id42"))
    // ==> answerSets.asList().contains(new Female("id42"))
    // ==> answerSets.cautiousConsequence().isEmpty()

    binding.add(Person.class);
    answerSets = objectSolver.getAnswerSets(builder.build(), binding);

    // ==> answerSets.cautiousConsequence().size()==1
    // ==> answerSets.cautiousConsequence().contains(person)
