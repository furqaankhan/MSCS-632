# Assignment 7: Multi-Paradigm Problem Solving

This project calculates the mean, median, and mode of the same integer data in three languages. Each implementation emphasizes a different programming paradigm: procedural C, functional OCaml, and object-oriented Python.

## Project Files

- `c/statistics.c`: procedural implementation with functions, arrays, direct counting, and manual memory management
- `ocaml/statistics.ml`: functional implementation with immutable lists, folds, maps, filters, and pipelines
- `python/statistics_calculator.py`: object-oriented implementation centered on a `StatisticsCalculator` class
- `output/`: captured sample output from all three programs
- `screenshots/`: code and sample-output images for C, OCaml, and Python
- `Makefile`: common build, run, test, capture, and cleanup commands

## Requirements

- A C17 compiler such as GCC or Clang
- OCaml 4.13 or newer
- Python 3.9 or newer
- GNU Make

## Build and Run

Run these commands from `assignment-7`.

```bash
make
make run
```

With no arguments, every program uses the same sample list:

```text
[12, 4, 7, 4, 9, 12, 3, 12, 4, 8]
```

You can also supply any nonempty list of integers:

```bash
./build/statistics_c 5 -2 5 8
./build/statistics_ocaml 5 -2 5 8
python3 python/statistics_calculator.py 5 -2 5 8
```

## Verify and Capture Output

```bash
make test
make capture
```

`make test` checks the mean, median, and mode produced by all three languages for a known input. `make capture` refreshes the three text files in `output/` using the shared sample list.

## Statistical Rules

- Mean is the sum divided by the number of integers.
- For an odd-sized list, median is the middle sorted value; for an even-sized list, it is the average of the two middle values.
- Mode includes every value tied for the highest frequency, listed in ascending order.
- At least one valid integer is required.

## GitHub Repository

<https://github.com/furqaankhan/MSCS-632>
