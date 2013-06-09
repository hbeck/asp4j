# asp4j

Answer Set Programming solver interface for Java

Version 0.0.5-SNAPSHOT. June 9, 2013

## Example

#### Logic program `person.lp`

    gender(X,female) :- person(X), not gender(X,male).
    gender(X,male)   :- person(X), not gender(X,female).

#### Java classes (beans)
  
```java  
@DefAtom("person")
public class Person {
    
    private String id;
    
    public Person() {
    }
    
    public Person(String id) {
        this.id=id;
    }
    
    @Arg(0)
    public String getId() {
        return id;
    }

    //setter, hashCode, equals ...
}

@DefAtom("gender")
public class PersonGender {    
    
    private String id;
    private Gender gender;
    
    @Arg(0)
    public String getId() {
        return id;
    }

    @Arg(1)
    public Gender getGender() {
        return gender;
    }

    //constructors, setter, hashCode, equals ...
}

@DefEnumConstants
public enum Gender {
    
    @MapWith("female") FEMALE,
    @MapWith("male") MALE    

}
```

#### Using a solver

- [dlv](http://www.dlvsystem.com/)
- [clingo](http://potassco.sourceforge.net/)

```java
Person person = new Person("id42");

ObjectSolver solver = new ObjectSolverImpl(externalSolver);
Program<Object> program = new ProgramBuilder().add(new File(rulefile)).add(person).build();
Filter filter = new Filter().add(PersonGender.class);
        
List<AnswerSet<Object>> answerSets = solver.getAnswerSets(program, filter);

// ==> answerSets.size() == 2

Set<Object> cautiousConsequence = solver.getConsequence(program, ReasoningMode.CAUTIOUS);

// ==> cautiousConsequence.size() == 1
// ==> cautiousConsequence.contains(person);

cautiousConsequence = solver.getConsequence(program, ReasoningMode.CAUTIOUS, filter);

// ==> cautiousConsequence.isEmpty()

Set<Object> braveConsequence = solver.getConsequence(program, ReasoningMode.BRAVE, filter);

// ==> braveConsequence.size() == 2
// ==> braveConsequence.contains(new PersonGender("id42", Gender.FEMALE))
// ==> braveConsequence.contains(new PersonGender("id42", Gender.MALE))
```
