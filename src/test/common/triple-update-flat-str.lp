% future state of database
 future(S,P,O) :- db(S,P,O), not -future(S,P,O).
 future(S,P,O) :- add(S,P,O).
-future(S,P,O) :- del(S,P,O).

% inferred for output
del(S,P,O) :- db(S,P,O), -future(S,P,O).
add(S,P,O) :- future(S,P,O), not db(S,P,O).

% high level semantics
 future(S2,P2,O2) :-  future(S1,P1,O1), iff(S1,P1,O1,S2,P2,O2).
-future(S2,P2,O2) :- -future(S1,P1,O1), iff(S1,P1,O1,S2,P2,O2).

iff(S2,P2,O2,S1,P1,O1) :- iff(S1,P1,O1,S2,P2,O2).

%
pool(S,P,O) :- add(S,P,O).
pool(S,P,O) :- del(S,P,O).
pool(S,P,O) :- db(S,P,O).

iff(S,P,O,O,Q,S) :- pool(S,P,O), pool(P,inverseOf,Q).

symmetric(inverseOf).
pool(X,P,X)  :- pool(X,P,_), reflexive(P).
pool(Q,SP,P) :- pool(P,SP,Q), symmetric(SP).
pool(X,P,Z)  :- pool(X,P,Y), pool(Y,P,Z), transitive(P).

% informal cardinality=1 constraint
confl(single_violation,S,P,O1,O2) :- future(S,P,O1), future(S,P,O2), O1<O2, future(P,type,single).
conflict :- confl(_,_,_,_,_).