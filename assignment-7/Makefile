CC := cc
CFLAGS := -std=c17 -Wall -Wextra -Wpedantic -Werror
OCAMLC := ocamlc
PYTHON := python3
BUILD_DIR := build
C_PROGRAM := $(BUILD_DIR)/statistics_c
OCAML_OBJECT := $(BUILD_DIR)/statistics.cmo
OCAML_PROGRAM := $(BUILD_DIR)/statistics_ocaml

.PHONY: all run capture test clean

all: $(C_PROGRAM) $(OCAML_PROGRAM)

$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

$(C_PROGRAM): c/statistics.c | $(BUILD_DIR)
	$(CC) $(CFLAGS) $< -o $@

$(OCAML_OBJECT): ocaml/statistics.ml | $(BUILD_DIR)
	$(OCAMLC) -w +a-70 -warn-error +a-70 -c -o $@ $<

$(OCAML_PROGRAM): $(OCAML_OBJECT)
	$(OCAMLC) -o $@ $<

run: all
	@$(C_PROGRAM)
	@echo
	@$(OCAML_PROGRAM)
	@echo
	@$(PYTHON) python/statistics_calculator.py

capture: all
	mkdir -p output
	$(C_PROGRAM) > output/c_output.txt
	$(OCAML_PROGRAM) > output/ocaml_output.txt
	$(PYTHON) python/statistics_calculator.py > output/python_output.txt

test: all
	@set -eu; \
	for command in \
	  "$(C_PROGRAM) 1 2 2 3" \
	  "$(OCAML_PROGRAM) 1 2 2 3" \
	  "$(PYTHON) python/statistics_calculator.py 1 2 2 3"; do \
	  output=`$$command`; \
	  echo "$$output" | grep -q "Mean:   2.00"; \
	  echo "$$output" | grep -q "Median: 2.00"; \
	  echo "$$output" | grep -q "Mode(s): \[2\]"; \
	done
	@echo "All C, OCaml, and Python checks passed."

clean:
	rm -rf $(BUILD_DIR)
