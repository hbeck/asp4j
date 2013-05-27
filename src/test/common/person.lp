female(X) :- person(X), not male(X).
male(X)   :- person(X), not female(X).
