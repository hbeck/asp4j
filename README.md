# asp4j

Answer Set Programming solver interface for Java

Version 0.0.6-SNAPSHOT. June 16, 2013

## Example

#### Logic program `reach.lp`

    reach(X,Y) :- edge(X,Y), not blocked(X).
    reach(X,Z) :- reach(X,Y), reach(Y,Z).

#### Java classes (beans)
  
```java
@DefAtom("edge")
public class Edge {
    
    private Node from;
    private Node to;

    public Edge() {
    }

    // other constructors ...
    
    @Arg(0)
    public Node getFrom() {
        return from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    @Arg(1)
    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }

    // hashCode, equals, ...

}

@DefTerm("node")
public class Node {

    @Arg(0)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //...
}

@DefAtom("reach")
public class Reach {

    //like class Edge ...
    
}

@DefAtom("blocked")
public class Blocked {
    
    private Node node;

    public Blocked() {
    }

    // ...
    
    @Arg(0)
    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    // ...
}


```

#### Using a solver

- [dlv](http://www.dlvsystem.com/)
- [clingo](http://potassco.sourceforge.net/)

```java
Edge edge_ab = new Edge("a", "b");
Edge edge_bc = new Edge("b", "c");
Edge edge_cd = new Edge("c", "d");

ObjectSolver solver = new ObjectSolverImpl(externalSolver);
Program<Object> program = new ProgramBuilder<>()
    .add(new File(rulefile))
    .add(edge_ab).add(edge_bc).add(edge_cd)
    .build();

Filter filter = new Filter().add(Reach.class);

List<AnswerSet<Object>> answerSets = solver.getAnswerSets(program, filter);

// ==> answerSets.size() == 1
        
Set<Object> as = answerSets.get(0).atoms();

// ==> as.size() == 6, containing a Reach object for each node pair of:
// (a,b) (b,c) (c,d) (a,c) (a,d) (b,d)

program = new ProgramBuilder<>(program).add(new Blocked("c")).build();
Set<Object> cautiousCons = solver.getConsequence(program, ReasoningMode.CAUTIOUS, filter);
Set<Object> braveCons = solver.getConsequence(program, ReasoningMode.BRAVE, filter);

// ==> cautiousCons equals braveCons, size 3, with Reach objects for node pairs:
// (a,b) (b,c) (a,c)
```
