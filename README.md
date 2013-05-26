# asp4j

Answer Set Programming solver interface for Java

## Example

#### Logic program `person.lp`

    female(X) v male(X) :- person(X).

#### Java classes (beans)
  
```java  
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
    //constructors, equals ...
}

@Predicate("male")
public class Male extends Person {
  //constructors, equals ...
}
```

#### Using a solver ([DLV](http://www.dlvsystem.com/))

```java
Person person = new Person("id42");

ObjectSolver objectSolver = new ObjectSolverImpl(new SolverDLV());
       
Program<Object> program = new ProgramBuilder().add(new File(rulefile)).add(person).build();
FilterBinding binding = new FilterBindingImpl().add(Male.class).add(Female.class);

List<AnswerSet<Object>> answerSets = objectSolver.getAnswerSets(program, binding);

// ==> answerSets.asList().size() == 2  

Set<Object> cautiousCons = objectSolver.getConsequence(program, binding, ReasoningMode.CAUTIOUS);

// ==> cautiousConsequence.isEmpty()

Set<Object> braveConsequence = objectSolver.getConsequence(program, binding, ReasoningMode.BRAVE);

// ==> braveConsequence.size() == 2
// ==> braveConsequence.contains(new Female("id42"));    
// ==> braveConsequence.contains(new Male("id42"));  
  
binding.add(Person.class);
cautiousConsequence = objectSolver.getConsequence(program, binding, ReasoningMode.CAUTIOUS);

// ==> cautiousConsequence.size() == 1
// ==> cautiousConsequence.contains(person)
```