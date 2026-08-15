% Assignment 8: Family Tree in Prolog
%
% parent(Parent, Child) records a direct parent-child relationship.
% The family contains three generations: grandparents, their children,
% and their grandchildren.


% Gender facts

male(george).
male(james).
male(robert).
male(michael).
male(daniel).
male(ethan).
male(lucas).

female(martha).
female(linda).
female(susan).
female(patricia).
female(emily).
female(olivia).
female(sophia).


% Parent facts

parent(george, james).
parent(martha, james).
parent(george, susan).
parent(martha, susan).
parent(george, michael).
parent(martha, michael).

parent(james, emily).
parent(linda, emily).
parent(james, daniel).
parent(linda, daniel).

parent(susan, olivia).
parent(robert, olivia).
parent(susan, ethan).
parent(robert, ethan).

parent(michael, sophia).
parent(patricia, sophia).
parent(michael, lucas).
parent(patricia, lucas).
% Derived relationships

% Child is a child of Parent.
child(Child, Parent) :-
    parent(Parent, Child).

% Grandparent is a grandparent of Grandchild through an intermediate Parent.
grandparent(Grandparent, Grandchild) :-
    parent(Grandparent, Parent),
    parent(Parent, Grandchild).

% Person and Sibling share a parent and are different people.
sibling(Person, Sibling) :-
    parent(SharedParent, Person),
    parent(SharedParent, Sibling),
    Person \= Sibling.

% Person and Cousin have parents who are siblings.
cousin(Person, Cousin) :-
    parent(PersonParent, Person),
    parent(CousinParent, Cousin),
    sibling(PersonParent, CousinParent),
    Person \= Cousin.

% Descendant is a direct child of Ancestor (base case).
descendant(Ancestor, Descendant) :-
    parent(Ancestor, Descendant).

% Descendant is below one of Ancestor's children (recursive case).
descendant(Ancestor, Descendant) :-
    parent(Ancestor, Child),
    descendant(Child, Descendant).
