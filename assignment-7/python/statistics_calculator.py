"""Object-oriented mean, median, and mode calculator."""

from __future__ import annotations

import sys
from collections.abc import Iterable


SAMPLE_NUMBERS = [12, 4, 7, 4, 9, 12, 3, 12, 4, 8]


class StatisticsCalculator:
    """Calculate basic statistics for a nonempty collection of integers."""

    def __init__(self, numbers: Iterable[int]) -> None:
        self._numbers = list(numbers)
        if not self._numbers:
            raise ValueError("at least one integer is required")
        if any(type(number) is not int for number in self._numbers):
            raise TypeError("all values must be integers")

    @property
    def numbers(self) -> list[int]:
        """Return a copy so callers cannot change the calculator's data."""
        return self._numbers.copy()

    def sorted_numbers(self) -> list[int]:
        """Return the values in ascending order."""
        return sorted(self._numbers)

    def mean(self) -> float:
        """Return the arithmetic average."""
        return sum(self._numbers) / len(self._numbers)

    def median(self) -> float:
        """Return the middle value, averaging two values for an even count."""
        ordered = self.sorted_numbers()
        middle = len(ordered) // 2
        if len(ordered) % 2 == 1:
            return float(ordered[middle])
        return (ordered[middle - 1] + ordered[middle]) / 2

    def mode(self) -> list[int]:
        """Return all most-frequent values in ascending order."""
        frequencies: dict[int, int] = {}
        for number in self._numbers:
            frequencies[number] = frequencies.get(number, 0) + 1

        highest_frequency = max(frequencies.values())
        return sorted(
            number
            for number, frequency in frequencies.items()
            if frequency == highest_frequency
        )

    def display(self) -> None:
        """Print the input and its calculated statistics."""
        print("STATISTICS CALCULATOR - PYTHON (OBJECT-ORIENTED)")
        print("================================================")
        print(f"Input:  {self._numbers}")
        print(f"Sorted: {self.sorted_numbers()}")
        print(f"Mean:   {self.mean():.2f}")
        print(f"Median: {self.median():.2f}")
        print(f"Mode(s): {self.mode()}")


def parse_arguments(arguments: list[str]) -> list[int]:
    """Convert command-line values to integers or use the shared sample data."""
    if not arguments:
        return SAMPLE_NUMBERS.copy()

    numbers: list[int] = []
    for argument in arguments:
        try:
            numbers.append(int(argument))
        except ValueError as error:
            raise ValueError(f"{argument!r} is not a valid integer") from error
    return numbers


def main() -> int:
    """Run the command-line demonstration."""
    try:
        calculator = StatisticsCalculator(parse_arguments(sys.argv[1:]))
    except (TypeError, ValueError) as error:
        print(f"Error: {error}.", file=sys.stderr)
        return 1

    calculator.display()
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
