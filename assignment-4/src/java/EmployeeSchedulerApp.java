import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

class Employee {
    String name;
    Map<String, String> preferences;

    Employee(String name, Map<String, String> preferences) {
        this.name = name;
        this.preferences = preferences;
    }
}

public class EmployeeSchedulerApp {
    static final List<String> DAYS = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
    static final List<String> SHIFTS = Arrays.asList("morning", "afternoon", "evening");
    static final int MAX_DAYS = 5;
    static final int EMPLOYEES_PER_SHIFT = 2;
    static final int RANDOM_SEED = 7;

    static List<Employee> buildSampleEmployees() {
        List<String> names = Arrays.asList("Alex", "Brianna", "Carlos", "Dana", "Ethan", "Fatima", "Grace", "Hassan", "Isabella");
        List<Employee> employees = new ArrayList<>();

        for (int i = 0; i < names.size(); i++) {
            Map<String, String> preferences = new LinkedHashMap<>();
            for (int dayIndex = 0; dayIndex < DAYS.size(); dayIndex++) {
                preferences.put(DAYS.get(dayIndex), SHIFTS.get((i + dayIndex) % 3));
            }
            employees.add(new Employee(names.get(i), preferences));
        }

        return employees;
    }

    static List<Employee> getUserEmployees(Scanner scanner) {
        List<Employee> employees = new ArrayList<>();
        System.out.print("How many employees do you want to enter? ");
        int count = Integer.parseInt(scanner.nextLine().trim());

        for (int i = 0; i < count; i++) {
            System.out.print("Employee " + (i + 1) + " name: ");
            String name = scanner.nextLine().trim();
            Map<String, String> preferences = new LinkedHashMap<>();

            for (String day : DAYS) {
                System.out.print("Preferred shift for " + name + " on " + day + " (morning/afternoon/evening): ");
                String shift = scanner.nextLine().trim().toLowerCase();
                if (!SHIFTS.contains(shift)) {
                    shift = "morning";
                }
                preferences.put(day, shift);
            }
            employees.add(new Employee(name, preferences));
        }

        return employees;
    }

    static Map<String, Map<String, List<String>>> createEmptySchedule() {
        Map<String, Map<String, List<String>>> schedule = new LinkedHashMap<>();
        for (String day : DAYS) {
            Map<String, List<String>> shiftMap = new LinkedHashMap<>();
            for (String shift : SHIFTS) {
                shiftMap.put(shift, new ArrayList<>());
            }
            schedule.put(day, shiftMap);
        }
        return schedule;
    }

    static boolean alreadyWorkingThatDay(String employeeName, String day, Map<String, List<String>> workedDays) {
        return workedDays.get(employeeName).contains(day);
    }

    static boolean canAssign(Employee employee, String day, String shift,
                             Map<String, Map<String, List<String>>> schedule,
                             Map<String, List<String>> workedDays) {
        if (alreadyWorkingThatDay(employee.name, day, workedDays)) {
            return false;
        }
        if (workedDays.get(employee.name).size() >= MAX_DAYS) {
            return false;
        }
        if (schedule.get(day).get(shift).size() >= EMPLOYEES_PER_SHIFT) {
            return false;
        }
        return true;
    }

    static void assignEmployee(Employee employee, String day, String shift,
                               Map<String, Map<String, List<String>>> schedule,
                               Map<String, List<String>> workedDays) {
        schedule.get(day).get(shift).add(employee.name);
        workedDays.get(employee.name).add(day);
    }

    static boolean resolveConflict(Employee employee, String day, int dayIndex,
                                   Map<String, Map<String, List<String>>> schedule,
                                   Map<String, List<String>> workedDays) {
        String preferred = employee.preferences.get(day);

        for (String shift : SHIFTS) {
            if (!shift.equals(preferred) && canAssign(employee, day, shift, schedule, workedDays)) {
                assignEmployee(employee, day, shift, schedule, workedDays);
                return true;
            }
        }

        if (dayIndex < DAYS.size() - 1) {
            String nextDay = DAYS.get(dayIndex + 1);
            String nextPreferred = employee.preferences.get(nextDay);

            if (canAssign(employee, nextDay, nextPreferred, schedule, workedDays)) {
                assignEmployee(employee, nextDay, nextPreferred, schedule, workedDays);
                return true;
            }

            for (String shift : SHIFTS) {
                if (canAssign(employee, nextDay, shift, schedule, workedDays)) {
                    assignEmployee(employee, nextDay, shift, schedule, workedDays);
                    return true;
                }
            }
        }

        return false;
    }

    static void assignPreferredShifts(List<Employee> employees,
                                      Map<String, Map<String, List<String>>> schedule,
                                      Map<String, List<String>> workedDays) {
        for (int dayIndex = 0; dayIndex < DAYS.size(); dayIndex++) {
            String day = DAYS.get(dayIndex);
            List<Employee> employeesForDay = new ArrayList<>(employees);
            Collections.sort(employeesForDay, (a, b) -> workedDays.get(a.name).size() - workedDays.get(b.name).size());

            for (Employee employee : employeesForDay) {
                String preferred = employee.preferences.get(day);
                if (canAssign(employee, day, preferred, schedule, workedDays)) {
                    assignEmployee(employee, day, preferred, schedule, workedDays);
                } else {
                    resolveConflict(employee, day, dayIndex, schedule, workedDays);
                }
            }
        }
    }

    static void fillMissingShifts(List<Employee> employees,
                                  Map<String, Map<String, List<String>>> schedule,
                                  Map<String, List<String>> workedDays) {
        Random random = new Random(RANDOM_SEED);

        for (String day : DAYS) {
            for (String shift : SHIFTS) {
                while (schedule.get(day).get(shift).size() < EMPLOYEES_PER_SHIFT) {
                    List<Employee> available = new ArrayList<>();
                    for (Employee employee : employees) {
                        if (canAssign(employee, day, shift, schedule, workedDays)) {
                            available.add(employee);
                        }
                    }

                    if (available.isEmpty()) {
                        break;
                    }

                    Employee chosen = available.get(random.nextInt(available.size()));
                    assignEmployee(chosen, day, shift, schedule, workedDays);
                }
            }
        }
    }

    static void printSchedule(Map<String, Map<String, List<String>>> schedule,
                              Map<String, List<String>> workedDays,
                              List<Employee> employees) {
        System.out.println("FINAL EMPLOYEE SCHEDULE");
        System.out.println("==================================================");
        System.out.println("Rules:");
        System.out.println("- One shift per employee per day");
        System.out.println("- Maximum " + MAX_DAYS + " days per employee per week");
        System.out.println("- Minimum " + EMPLOYEES_PER_SHIFT + " employees per shift");
        System.out.println();

        for (String day : DAYS) {
            System.out.println(day);
            System.out.println("-".repeat(day.length()));
            for (String shift : SHIFTS) {
                String names = String.join(", ", schedule.get(day).get(shift));
                System.out.println(capitalize(shift) + ": " + names);
            }
            System.out.println();
        }

        System.out.println("EMPLOYEE TOTAL DAYS");
        System.out.println("--------------------------------------------------");
        for (Employee employee : employees) {
            System.out.println(employee.name + ": " + workedDays.get(employee.name).size());
        }
    }

    static String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Employee Schedule Manager - Java");
        System.out.print("Use sample data? (y/n): ");
        String choice = scanner.nextLine().trim().toLowerCase();

        List<Employee> employees;
        if (choice.equals("n")) {
            employees = getUserEmployees(scanner);
        } else {
            employees = buildSampleEmployees();
        }

        Map<String, Map<String, List<String>>> schedule = createEmptySchedule();
        Map<String, List<String>> workedDays = new LinkedHashMap<>();
        for (Employee employee : employees) {
            workedDays.put(employee.name, new ArrayList<>());
        }

        assignPreferredShifts(employees, schedule, workedDays);
        fillMissingShifts(employees, schedule, workedDays);

        System.out.println();
        printSchedule(schedule, workedDays, employees);
    }
}
