# Assignment 8: Building a Family Tree in Prolog

This project represents a three-generation family using Prolog facts
and rules. It supports queries for children, grandparents, siblings, cousins,
and all descendants of a person.

## Project Files

- `prolog/family_tree.pl`: family facts and relationship rules
- `prolog/sample_queries.pl`: reproducible assignment queries
- `prolog/test_family_tree.pl`: automated checks for every relationship
- `output/`: captured sample-query and test output
- `screenshots/`: code and console-output images for the submission document
- `Makefile`: syntax-check, run, test, capture, and cleanup commands

## Requirements

- A Prolog interpreter that provides the standard `setof/3` predicate
- GNU Make

## Run and Test

Run these commands from `assignment-8`:

```bash
make check
make run
make test
```

Refresh the text files in `output/` with:

```bash
make capture
```

To explore the family interactively, load the facts and rules:

```bash
swipl -s prolog/family_tree.pl
```

Example queries:

```prolog
% Who are James's children?
?- parent(james, Child).

% Who are Emily's siblings?
?- sibling(emily, Sibling).

% Are Emily and Olivia cousins?
?- cousin(emily, olivia).

% Who are all of George's descendants?
?- descendant(george, Person).
```

Use `setof/3` when a complete sorted list is more convenient than displaying
one answer at a time:

```prolog
?- setof(Child, parent(james, Child), Children).
Children = [daniel, emily].
```

## Relationship Direction

The first argument comes before the second in the family tree:

- `parent(Parent, Child)`
- `grandparent(Grandparent, Grandchild)`
- `descendant(Ancestor, Descendant)`

The recursive `descendant/2` rule first handles a direct child as its base case.
Its recursive case follows each child to find later generations.

## GitHub Repository

<https://github.com/furqaankhan/MSCS-632>
