:- consult('family_tree.pl').


print_set(QueryText, Label, Template, Goal) :-
    format('?- ~w~n', [QueryText]),
    setof(Template, Goal, Results),
    format('~w = ~w.~n~n', [Label, Results]).

print_yes_no(QueryText, Goal) :-
    format('?- ~w~n', [QueryText]),
    (   call(Goal)
    ->  writeln('true.')
    ;   writeln('false.')
    ),
    nl.

main :-
    writeln('ASSIGNMENT 8 - FAMILY TREE SAMPLE QUERIES'),
    writeln('========================================='),
    nl,
    print_set(
        'setof(Child, parent(james, Child), Children).',
        'Children',
        Child,
        parent(james, Child)
    ),
    print_set(
        'setof(Sibling, sibling(emily, Sibling), Siblings).',
        'Siblings',
        Sibling,
        sibling(emily, Sibling)
    ),
    print_set(
        'setof(Grandchild, grandparent(george, Grandchild), Grandchildren).',
        'Grandchildren',
        Grandchild,
        grandparent(george, Grandchild)
    ),
    print_yes_no(
        'cousin(emily, olivia).',
        cousin(emily, olivia)
    ),
    print_yes_no(
        'cousin(emily, daniel).',
        cousin(emily, daniel)
    ),
    print_set(
        'setof(Cousin, cousin(emily, Cousin), Cousins).',
        'Cousins',
        Cousin,
        cousin(emily, Cousin)
    ),
    print_set(
        'setof(Person, descendant(george, Person), Descendants).',
        'Descendants',
        Person,
        descendant(george, Person)
    ).
