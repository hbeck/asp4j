# asp4j

Answer Set Programming solver interface for Java

**Version 0.0.2. May 30, 2013**

## Example

#### Logic program `person.lp`

    female(X) :- person(X), not male(X).
    male(X)   :- person(X), not female(X).

#### Java classes (beans)
  
```java  
@DefAtom("person")
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

@DefAtom("female")
public class Female extends Person {
    //constructors, equals ...
}

@DefAtom("male")
public class Male extends Person {
  //constructors, equals ...
}
```

#### Using a solver

- [dlv](http://www.dlvsystem.com/)
- [clingo](http://potassco.sourceforge.net/)

```java
Person person = new Person("id42");

Solver externalSolver = new SolverDLV(); //or new SolverClingo();  
ObjectSolver solver = new ObjectSolverImpl(externalSolver); 

Program<Object> program = new ProgramBuilder().add(new File("person.lp")).add(person).build();
FilterBinding binding = new FilterBinding().add(Male.class).add(Female.class);
List<AnswerSet<Object>> answerSets = solver.getAnswerSets(program, binding);

// ==> answerSets.size() == 2

Set<Object> cautiousConsequence = solver.getConsequence(program, binding, ReasoningMode.CAUTIOUS);

// ==> cautiousConsequence.isEmpty()

Set<Object> braveConsequence = solver.getConsequence(program, binding, ReasoningMode.BRAVE);

// ==> braveConsequence.size() == 2
// ==> braveConsequence.contains(new Female("id42"))
// ==> braveConsequence.contains(new Male("id42"))

binding.add(Person.class);
cautiousConsequence = solver.getConsequence(program, binding, ReasoningMode.CAUTIOUS);

// ==> cautiousConsequence.size() == 1
// ==> cautiousConsequence.contains(person)
```
