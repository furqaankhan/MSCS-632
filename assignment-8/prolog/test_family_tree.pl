:- consult('family_tree.pl').


check(Goal, Description) :-
    (   call(Goal)
    ->  format('[PASS] ~w~n', [Description])
    ;   format('[FAIL] ~w~n', [Description]),
        fail
    ).

children_test :-
    setof(Child, parent(james, Child), Children),
    Children == [daniel, emily].

siblings_test :-
    setof(Sibling, sibling(emily, Sibling), Siblings),
    Siblings == [daniel].

grandchildren_test :-
    setof(
        Grandchild,
        grandparent(george, Grandchild),
        Grandchildren
    ),
    Grandchildren == [daniel, emily, ethan, lucas, olivia, sophia].

cousins_test :-
    setof(Cousin, cousin(emily, Cousin), Cousins),
    Cousins == [ethan, lucas, olivia, sophia].

descendants_test :-
    setof(Person, descendant(george, Person), Descendants),
    Descendants == [daniel, emily, ethan, james, lucas, michael, olivia,
                    sophia, susan].

run_tests :-
    check(male(george), 'male/1 fact'),
    check(female(emily), 'female/1 fact'),
    check(parent(james, emily), 'parent/2 fact'),
    check(child(emily, james), 'child/2 rule'),
    check(grandparent(george, sophia), 'grandparent/2 rule'),
    check(sibling(emily, daniel), 'sibling/2 rule'),
    check(\+ sibling(emily, emily), 'a person is not their own sibling'),
    check(cousin(emily, olivia), 'cousin/2 positive query'),
    check(\+ cousin(emily, daniel), 'siblings are not cousins'),
    check(descendant(george, sophia), 'recursive descendant/2 rule'),
    check(children_test, 'children query returns the expected set'),
    check(siblings_test, 'sibling query returns the expected set'),
    check(grandchildren_test, 'grandparent query returns all grandchildren'),
    check(cousins_test, 'cousin query returns all cousins'),
    check(descendants_test, 'descendant query returns both generations'),
    writeln('All family tree checks passed.').
