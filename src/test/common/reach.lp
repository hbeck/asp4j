reach(X,Y) :- edge(X,Y), not blocked(X).
reach(X,Z) :- reach(X,Y), reach(Y,Z).