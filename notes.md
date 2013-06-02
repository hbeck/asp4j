implementation via mapping handler.

additinonal mapping, e.g. 'direct' and annotation based -> map internally to mapping handlers?


#### Logic program `reach.lp`

    reach(X,Y) :- edge(X,Y), not blocked(X,Y).
    reach(X,Z) :- reach(X,Y), reach(Y,Z).

#### Java classes (beans)
  
```java
@DefAtom("edge")
public class Edge {

    private Node from;
    private Node to;
    private boolean blocked;

    public Edge() {
    }
    
    public Edge(Node from, Node to) {
       this.from=from;
       this.to=to;
    }
    
    @Arg(0)
    public Node getFrom() {
        return this.from;
    }
    
    public void setFrom(Node node) {
        this.node = node;
    }
    
    @Arg(1)
    public Node getTo() {
        return this.to;
    }
    
    public void setTo(Node node) {
        this.node=node;
    }

    @BooleanQuery("blocked($fromNode()$,$toNode()$)?")
    public boolean isBlocked {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked=blocked;
    }

    @Describe
    public Blocking getBlocking() {
        if (isBlocked()) {
            return new Blocked(getFrom(),getTo());
        }
    }

    public void setBlocking(Blocking blocking) {
        if (blocking.getFrom().equals(getFrom()) TT blocking.getTo().equals(getTo())) {
            this.blocked=true;
        }
        this.blocked=false;
    }

    @TupleQuery("reach($fromNode()$,Y)?"
    public List<Node> reachableNodes(); //interface?

    @Query("reach($fromNode()$,Y)?")
    public List<Reach> reachaleNodes(); //interface?
}

@DefAtom("reach")
public class Reach --not--extends Edge {
    //...
}

@DefTerm("node")
public class Node {
    // ...
    @Arg(0)
    private String getId();
}

```

#### Using a solver

- [dlv](http://www.dlvsystem.com/)
- [clingo](http://potassco.sourceforge.net/)

```java
Edge edge1 = new Edge(new Node("a","b"));
Edge edge2 = new Edge(new Node("b","c1"));
Edge edge3 = new Edge(new Node("b","c2)));
edge3.setBlocked(true);

Solver externalSolver = new SolverDLV(); //or new SolverClingo();  
ObjectSolver solver = new ObjectSolverImpl(externalSolver); 

Program<Object> program = new ProgramBuilder()
        .add(new File("reach.lp"))
        .add(edge1)
        .add(edge2)
        .add(edge3)
        .build();
        
FilterBinding binding = new FilterBinding().add(Reach.class);
List<AnswerSet<Object>> answerSets = solver.getAnswerSets(program, binding);

// ==> answerSets.size() == 1

Set<Object> cons = solver.getConsequence(program, binding); //? ReasoningMode may be ommited if size()==1; else throw Exception

// ==> cons.size() == 3
// ==> cons.contains(new Reach("a","b"));
// ==> cons.contains(new Reach("b","c1"));
// ==> cons.contains(new Reach("a","c1"));

boolean test = solver.booleanQuery("reach(a,c2)?", ReasoningMode.CAUTIOUS); //similar omitting

// ==> test == false;

List<QueryResultBinding> tupleQueryResult = externalSolver.tupleQuery("reach(a,X)?");

// ==> tupleQueryResult.size() == 2
// ==> tupleQueryResult.contains(new QueryResultBinding("X","b"))
// ==> tupleQueryResult.contains(new QueryResultBinding("X","c1"))

List<Atom> queryResult = externalSolver.query("reach(a,X)?");

// ==> queryResult.size() == 2
// ==> queryResult.contains(ParseUtils.parseAtom("reach(a,b)"))
// ==> queryResult.contains(ParseUtils.parseAtom("reach(a,c1)"))

List<Reach> objectQueryResult = solver.query("reach(a,Y)?",binding);
//List<Reach> objectQueryResult = solver.query("reach(a,Y)?",Reach.class);

// ==> objectQueryResult.size() == 2
// ==> objectQueryResult.contains(new Reach("a","b"));
// ==> objectQueryResult.contains(new Reach("a","c1"));

List<QueryResultBinding> queryResult = externalSolver.tupleQuery("via(X,Y,Z) :- reach(X,Y), reach(Y,Z). via(X,Y,Z)?");

// ==> queryResult.contains(new QueryResultBinding("X","a","Y","b","Z","c1"))

List<QueryResultBinding> queryResult = externalSolver.tupleQuery("-via(X,Y,Z) :- reach(X,Y), not reach(Y,Z). -via(X,Y,Z)?");

// ==> queryResult.contains(new QueryResultBinding("X","a","Y","b","Z","2"))
```

