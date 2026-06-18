import random

DAYS = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
SHIFTS = ["morning", "afternoon", "evening"]
MAX_DAYS = 5
EMPLOYEES_PER_SHIFT = 2
RANDOM_SEED = 7


class Employee:
    def __init__(self, name, preferences):
        self.name = name
        self.preferences = preferences  # {day: preferred_shift}


def build_sample_employees():
    names = ["Alex", "Brianna", "Carlos", "Dana", "Ethan", "Fatima", "Grace", "Hassan", "Isabella"]
    employees = []

    for i, name in enumerate(names):
        preferences = {}
        for day_index, day in enumerate(DAYS):
            preferences[day] = SHIFTS[(i + day_index) % 3]
        employees.append(Employee(name, preferences))

    return employees


def get_user_employees():
    employees = []
    count = int(input("How many employees do you want to enter? "))

    for i in range(count):
        name = input(f"Employee {i + 1} name: ").strip()
        preferences = {}
        for day in DAYS:
            shift = input(f"Preferred shift for {name} on {day} (morning/afternoon/evening): ").strip().lower()
            if shift not in SHIFTS:
                shift = "morning"
            preferences[day] = shift
        employees.append(Employee(name, preferences))

    return employees


def create_empty_schedule():
    schedule = {}
    for day in DAYS:
        schedule[day] = {}
        for shift in SHIFTS:
            schedule[day][shift] = []
    return schedule


def already_working_that_day(employee_name, day, worked_days):
    return day in worked_days[employee_name]


def can_assign(employee, day, shift, schedule, worked_days):
    if already_working_that_day(employee.name, day, worked_days):
        return False
    if len(worked_days[employee.name]) >= MAX_DAYS:
        return False
    if len(schedule[day][shift]) >= EMPLOYEES_PER_SHIFT:
        return False
    return True


def assign_employee(employee, day, shift, schedule, worked_days):
    schedule[day][shift].append(employee.name)
    worked_days[employee.name].append(day)


def resolve_conflict(employee, day, day_index, schedule, worked_days):
    preferred = employee.preferences[day]

    for shift in SHIFTS:
        if shift != preferred and can_assign(employee, day, shift, schedule, worked_days):
            assign_employee(employee, day, shift, schedule, worked_days)
            return True

    if day_index < len(DAYS) - 1:
        next_day = DAYS[day_index + 1]
        next_preferred = employee.preferences[next_day]
        if can_assign(employee, next_day, next_preferred, schedule, worked_days):
            assign_employee(employee, next_day, next_preferred, schedule, worked_days)
            return True

        for shift in SHIFTS:
            if can_assign(employee, next_day, shift, schedule, worked_days):
                assign_employee(employee, next_day, shift, schedule, worked_days)
                return True

    return False


def assign_preferred_shifts(employees, schedule, worked_days):
    for day_index, day in enumerate(DAYS):
        employees_for_day = employees[:]
        employees_for_day.sort(key=lambda emp: len(worked_days[emp.name]))

        for employee in employees_for_day:
            preferred = employee.preferences[day]
            if can_assign(employee, day, preferred, schedule, worked_days):
                assign_employee(employee, day, preferred, schedule, worked_days)
            else:
                resolve_conflict(employee, day, day_index, schedule, worked_days)


def fill_missing_shifts(employees, schedule, worked_days):
    random.seed(RANDOM_SEED)

    for day in DAYS:
        for shift in SHIFTS:
            while len(schedule[day][shift]) < EMPLOYEES_PER_SHIFT:
                available = []
                for employee in employees:
                    if can_assign(employee, day, shift, schedule, worked_days):
                        available.append(employee)

                if not available:
                    break

                chosen = random.choice(available)
                assign_employee(chosen, day, shift, schedule, worked_days)


def print_schedule(schedule, worked_days, employees):
    print("FINAL EMPLOYEE SCHEDULE")
    print("=" * 50)
    print("Rules:")
    print("- One shift per employee per day")
    print(f"- Maximum {MAX_DAYS} days per employee per week")
    print(f"- Minimum {EMPLOYEES_PER_SHIFT} employees per shift")
    print()

    for day in DAYS:
        print(day)
        print("-" * len(day))
        for shift in SHIFTS:
            names = ", ".join(schedule[day][shift])
            print(f"{shift.capitalize()}: {names}")
        print()

    print("EMPLOYEE TOTAL DAYS")
    print("-" * 50)
    for employee in employees:
        print(f"{employee.name}: {len(worked_days[employee.name])}")


def main():
    print("Employee Schedule Manager - Python")
    choice = input("Use sample data? (y/n): ").strip().lower()

    if choice == "n":
        employees = get_user_employees()
    else:
        employees = build_sample_employees()

    schedule = create_empty_schedule()
    worked_days = {}
    for employee in employees:
        worked_days[employee.name] = []

    assign_preferred_shifts(employees, schedule, worked_days)
    fill_missing_shifts(employees, schedule, worked_days)

    print()
    print_schedule(schedule, worked_days, employees)


if __name__ == "__main__":
    main()
