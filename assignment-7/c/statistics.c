#include <errno.h>
#include <limits.h>
#include <stdio.h>
#include <stdlib.h>

static int compare_integers(const void *left, const void *right) {
    const int left_value = *(const int *)left;
    const int right_value = *(const int *)right;

    return (left_value > right_value) - (left_value < right_value);
}

static double calculate_mean(const int numbers[], size_t count) {
    long double sum = 0.0L;

    for (size_t index = 0; index < count; index++) {
        sum += numbers[index];
    }

    return (double)(sum / (long double)count);
}

static double calculate_median(const int sorted_numbers[], size_t count) {
    const size_t middle = count / 2;

    if (count % 2 != 0) {
        return sorted_numbers[middle];
    }

    return ((double)sorted_numbers[middle - 1] +
            (double)sorted_numbers[middle]) / 2.0;
}

static size_t calculate_modes(const int sorted_numbers[], size_t count,
                              int modes[]) {
    size_t mode_count = 0;
    size_t highest_frequency = 0;
    size_t run_start = 0;

    while (run_start < count) {
        size_t run_end = run_start + 1;

        while (run_end < count &&
               sorted_numbers[run_end] == sorted_numbers[run_start]) {
            run_end++;
        }

        const size_t frequency = run_end - run_start;
        if (frequency > highest_frequency) {
            highest_frequency = frequency;
            mode_count = 0;
            modes[mode_count++] = sorted_numbers[run_start];
        } else if (frequency == highest_frequency) {
            modes[mode_count++] = sorted_numbers[run_start];
        }

        run_start = run_end;
    }

    return mode_count;
}

static int parse_integer(const char *text, int *value) {
    char *end = NULL;
    errno = 0;
    const long parsed = strtol(text, &end, 10);

    if (errno == ERANGE || parsed < INT_MIN || parsed > INT_MAX ||
        end == text || *end != '\0') {
        return 0;
    }

    *value = (int)parsed;
    return 1;
}

static void print_number(double value) {
    printf("%.2f", value);
}

static void print_list(const int numbers[], size_t count) {
    printf("[");
    for (size_t index = 0; index < count; index++) {
        if (index > 0) {
            printf(", ");
        }
        printf("%d", numbers[index]);
    }
    printf("]");
}

int main(int argc, char *argv[]) {
    const int sample_numbers[] = {12, 4, 7, 4, 9, 12, 3, 12, 4, 8};
    const size_t sample_count = sizeof(sample_numbers) / sizeof(sample_numbers[0]);
    const size_t count = argc > 1 ? (size_t)(argc - 1) : sample_count;

    int *numbers = malloc(count * sizeof(*numbers));
    int *sorted_numbers = malloc(count * sizeof(*sorted_numbers));
    int *modes = malloc(count * sizeof(*modes));

    if (numbers == NULL || sorted_numbers == NULL || modes == NULL) {
        fprintf(stderr, "Error: unable to allocate memory.\n");
        free(numbers);
        free(sorted_numbers);
        free(modes);
        return EXIT_FAILURE;
    }

    if (argc > 1) {
        for (size_t index = 0; index < count; index++) {
            if (!parse_integer(argv[index + 1], &numbers[index])) {
                fprintf(stderr, "Error: '%s' is not a valid integer.\n",
                        argv[index + 1]);
                free(numbers);
                free(sorted_numbers);
                free(modes);
                return EXIT_FAILURE;
            }
        }
    } else {
        for (size_t index = 0; index < count; index++) {
            numbers[index] = sample_numbers[index];
        }
    }

    for (size_t index = 0; index < count; index++) {
        sorted_numbers[index] = numbers[index];
    }
    qsort(sorted_numbers, count, sizeof(*sorted_numbers), compare_integers);

    const double mean = calculate_mean(numbers, count);
    const double median = calculate_median(sorted_numbers, count);
    const size_t mode_count = calculate_modes(sorted_numbers, count, modes);

    printf("STATISTICS CALCULATOR - C (PROCEDURAL)\n");
    printf("======================================\n");
    printf("Input:  ");
    print_list(numbers, count);
    printf("\nSorted: ");
    print_list(sorted_numbers, count);
    printf("\nMean:   ");
    print_number(mean);
    printf("\nMedian: ");
    print_number(median);
    printf("\nMode(s): ");
    print_list(modes, mode_count);
    printf("\n");

    free(numbers);
    free(sorted_numbers);
    free(modes);
    return EXIT_SUCCESS;
}
