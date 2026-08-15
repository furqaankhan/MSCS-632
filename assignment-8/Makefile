SWIPL := swipl
FAMILY_TREE := prolog/family_tree.pl
SAMPLE_QUERIES := prolog/sample_queries.pl
TESTS := prolog/test_family_tree.pl

.PHONY: all check run test capture clean

all: check

check:
	@$(SWIPL) -q -t halt -s $(FAMILY_TREE)

run:
	@$(SWIPL) -q -t halt -s $(SAMPLE_QUERIES) -g main

test:
	@$(SWIPL) -q -t halt -s $(TESTS) -g run_tests

capture:
	mkdir -p output
	$(SWIPL) -q -t halt -s $(SAMPLE_QUERIES) -g main > output/sample_queries.txt
	$(SWIPL) -q -t halt -s $(TESTS) -g run_tests > output/test_results.txt

clean:
	rm -f output/sample_queries.txt output/test_results.txt
