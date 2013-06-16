reach(X,Y) :- edge(X,Y), not blocked(X,Y).
reach(X,Z) :- reach(X,Y), reach(Y,Z).