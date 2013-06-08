% future state of database
 future(T) :- db(T), not -future(T).
 future(T) :- add(T).
-future(T) :- del(T).

% inferred for output
del(T) :- db(T), -future(T).
add(T) :- future(T), not db(T).

% high level semantic
 future(T2) :-  future(T1), iff(T1,T2).
-future(T2) :- -future(T1), iff(T1,T2).

iff(T2,T1) :- iff(T1,T2).

pool(T) :- add(T).
pool(T) :- del(T).
pool(T) :- db(T).

iff(t(S,P,O),t(O,Q,S)) :- pool(t(S,P,O)), pool(t(P,owl_inverseOf,Q)).

symmetric(owl_inverseOf).
pool(t(X,P,X))  :- pool(t(X,P,_)), reflexive(P).
pool(t(Q,SP,P)) :- pool(t(P,SP,Q)), symmetric(SP).
pool(t(X,P,Z))  :- pool(t(X,P,Y)), pool(t(Y,P,Z)), transitive(P).

% skos
db(t(skos_broader,owl_inverseOf,skos_narrower)).
db(t(skos_narrower,owl_inverseOf,skos_broader)).

% informal cardinality=1 constraint
confl(single_violation,t(S,P,O1),t(S,P,O2)) :- future(t(S,P,O1)), future(t(S,P,O2)), O1 < O2, future(t(P,rdf_type,ex_single)).
% 
confl(narrower_broader,t(S,skos_narrower,O),t(S,skos_broader,O)) :- future(t(S,skos_narrower,O)), future(t(S,skos_broader,O)), S < O.

conflict :- confl(_,_,_).
skos_conflict :- confl(narrower_broader,_,_).
