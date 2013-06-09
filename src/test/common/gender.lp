gender(X,female) :- person(X), not gender(X,male).
gender(X,male)   :- person(X), not gender(X,female).