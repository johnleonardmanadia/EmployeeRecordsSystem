import java.util.ArrayList;

/**
 * AutoChecker - PROVIDED. DO NOT EDIT.
 * Powers the "Backend Auto-Check" panel on the page. It runs your
 * EmployeeManager and SortingAlgorithms code on test data and reports PASS / FAIL.
 * It checks results only - it does not contain any solution code.
 */
public class AutoChecker {

    private StringBuilder out;
    private String where = "";

    public String runAll() {
        out = new StringBuilder("OK\n\n");
        checkLinearSearch();
        checkAdd();
        checkDuplicate();
        checkUpdate();
        checkDelete();
        String[] names = {"Bubble Sort", "Insertion Sort", "Selection Sort", "Merge Sort", "Quick Sort"};
        for (int a = 1; a <= 5; a++) {
            checkSort(a, names[a - 1]);
        }
        return out.toString();
    }

    private void line(String name, String status, String detail) {
        out.append(name).append("|").append(status).append("|").append(detail).append("\n");
    }

    private String id(int n) {
        return "EMP-" + String.format("%03d", n);
    }

    private Employee peek(EmployeeManager m, String id) {
        ArrayList<Employee> list = m.getEmployees();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getEmployeeId().equals(id)) {
                return list.get(i);
            }
        }
        return null;
    }

    private Employee make(String id, String name, int level) {
        return new Employee(id, name, "Department" + (char) ('A' + level), "Position" + (char) ('A' + level), level * 10.0 + 5);
    }

    // ------------------------------------------------------------
    // BACKEND CHECKS
    // ------------------------------------------------------------
    private void checkLinearSearch() {
        String name = "Linear Search (findIndexById)";
        try {
            EmployeeManager m = new EmployeeManager();
            ArrayList<Employee> list = m.getEmployees();
            for (int i = 0; i < list.size(); i++) {
                String id = list.get(i).getEmployeeId();
                int found = m.findIndexById(id);
                if (found != i) {
                    line(name, "FAIL", "Searching for " + id + " returned " + found + " but it is at index " + i + ".");
                    return;
                }
            }
            if (m.findIndexById(id(999)) != -1) {
                line(name, "FAIL", "A missing ID must return -1.");
                return;
            }
            line(name, "PASS", "Found all " + list.size() + " records and returned -1 for a missing ID.");
        } catch (Throwable t) {
            line(name, "FAIL", "Your code crashed: " + t);
        }
    }

    private void checkAdd() {
        String name = "Add Employee";
        try {
            EmployeeManager m = new EmployeeManager();
            int before = m.getEmployees().size();
            boolean result = m.addEmployee(make(id(100), "Test Record", 1));
            if (!result || m.getEmployees().size() != before + 1) {
                line(name, "FAIL", "Adding a new ID must return true and grow the list by 1.");
                return;
            }
            line(name, "PASS", "A new record was added.");
        } catch (Throwable t) {
            line(name, "FAIL", "Your code crashed: " + t);
        }
    }

    private void checkDuplicate() {
        String name = "Duplicate ID rejected";
        try {
            EmployeeManager m = new EmployeeManager();
            int before = m.getEmployees().size();
            String existing = m.getEmployees().get(0).getEmployeeId();
            boolean result = m.addEmployee(make(existing, "Copycat", 1));
            if (result || m.getEmployees().size() != before) {
                line(name, "FAIL", "Adding an existing ID (" + existing + ") must return false and must NOT add anything.");
                return;
            }
            line(name, "PASS", "The duplicate ID was refused.");
        } catch (Throwable t) {
            line(name, "FAIL", "Your code crashed: " + t);
        }
    }

    private void checkUpdate() {
        String name = "Update Employee";
        try {
            EmployeeManager m = new EmployeeManager();
            String target = m.getEmployees().get(0).getEmployeeId();
            boolean result = m.updateEmployee(target, "New Name", "Updated", "Updated", 99.0);
            Employee s = peek(m, target);
            if (!result || s == null) {
                line(name, "FAIL", "Updating an existing ID must return true.");
                return;
            }
            if (!s.getName().equals("New Name") && s.getDepartment().equals("Updated") && s.getPosition().equals("Updated") && s.getSalary() == 99.0) {
                line(name, "FAIL", "Not every field changed (name and all other fields must be updated).");
                return;
            }
            if (m.updateEmployee(id(999), "X", "Updated", "Updated", 99.0)) {
                line(name, "FAIL", "Updating a missing ID must return false.");
                return;
            }
            line(name, "PASS", "All fields changed; a missing ID returned false.");
        } catch (Throwable t) {
            line(name, "FAIL", "Your code crashed: " + t);
        }
    }

    private void checkDelete() {
        String name = "Delete Employee";
        try {
            EmployeeManager m = new EmployeeManager();
            int before = m.getEmployees().size();
            String target = m.getEmployees().get(2).getEmployeeId();
            boolean result = m.deleteEmployee(target);
            if (!result || m.getEmployees().size() != before - 1 || peek(m, target) != null) {
                line(name, "FAIL", "Deleting an existing ID must return true and remove exactly that record.");
                return;
            }
            if (m.deleteEmployee(id(999))) {
                line(name, "FAIL", "Deleting a missing ID must return false.");
                return;
            }
            line(name, "PASS", "The record was removed; a missing ID returned false.");
        } catch (Throwable t) {
            line(name, "FAIL", "Your code crashed: " + t);
        }
    }

    // ------------------------------------------------------------
    // SORTING CHECKS
    // ------------------------------------------------------------
    private void checkSort(int algo, String name) {
        String[] cases = {"sample data", "empty list", "one record", "two records",
                          "already sorted", "reverse sorted", "duplicates"};
        int tests = 0;
        try {
            SortingAlgorithms sorter = new SortingAlgorithms();
            for (int c = 0; c < cases.length; c++) {
                for (int key = 1; key <= 5; key++) {
                    where = "case '" + cases[c] + "', sorted by " + keyName(key);
                    ArrayList<Employee> list = makeCase(c);
                    ArrayList<Employee> original = new ArrayList<Employee>(list);

                    if (algo == 1) sorter.bubbleSort(list, key);
                    else if (algo == 2) sorter.insertionSort(list, key);
                    else if (algo == 3) sorter.selectionSort(list, key);
                    else if (algo == 4) sorter.mergeSort(list, key);
                    else sorter.quickSort(list, key);

                    String problem = verify(original, list, key);
                    if (problem != null) {
                        line(name, "FAIL", "In " + where + ": " + problem);
                        return;
                    }
                    tests++;
                }
            }
            line(name, "PASS", "Passed all " + tests + " tests (5 sort fields x 7 situations).");
        } catch (Throwable t) {
            line(name, "FAIL", "Your code crashed in " + where + ": " + t);
        }
    }

    private String verify(ArrayList<Employee> before, ArrayList<Employee> after, int key) {
        if (after.size() != before.size()) {
            return "the list has " + after.size() + " records but should have " + before.size() + ".";
        }
        for (int i = 0; i < before.size(); i++) {
            int count = 0;
            for (int j = 0; j < after.size(); j++) {
                if (after.get(j) == before.get(i)) {
                    count++;
                }
            }
            if (count != 1) {
                return "a record was lost or duplicated.";
            }
        }
        for (int i = 0; i < after.size() - 1; i++) {
            if (cmp(after.get(i), after.get(i + 1), key) > 0) {
                return "not ascending at positions " + i + " and " + (i + 1) + " ("
                        + label(after.get(i), key) + " is before " + label(after.get(i + 1), key) + ").";
            }
        }
        return null;
    }

    private int cmp(Employee a, Employee b, int key) {
        if (key == 1) return a.getEmployeeId().compareTo(b.getEmployeeId());
        if (key == 2) return a.getName().compareToIgnoreCase(b.getName());
        if (key == 3) return a.getDepartment().compareToIgnoreCase(b.getDepartment());
        if (key == 4) return a.getPosition().compareToIgnoreCase(b.getPosition());
        if (key == 5) return Double.compare(a.getSalary(), b.getSalary());
        return 0;
    }

    private String label(Employee s, int key) {
        if (key == 1) return "" + s.getEmployeeId();
        if (key == 2) return "" + s.getName();
        if (key == 3) return "" + s.getDepartment();
        if (key == 4) return "" + s.getPosition();
        if (key == 5) return "" + s.getSalary();
        return "";
    }

    private String keyName(int key) {
        if (key == 1) return "Employee ID";
        if (key == 2) return "Name";
        if (key == 3) return "Department";
        if (key == 4) return "Position";
        if (key == 5) return "Salary";
        return "";
    }

    private ArrayList<Employee> makeCase(int c) {
        ArrayList<Employee> list = new ArrayList<Employee>();
        if (c == 0) {
            list.addAll(new EmployeeManager().getEmployees());
        } else if (c == 2) {
            list.add(make(id(1), "Solo", 3));
        } else if (c == 3) {
            list.add(make(id(2), "Zed", 2));
            list.add(make(id(1), "Amy", 1));
        } else if (c == 4 || c == 5) {
            list.add(make(id(1), "Amy", 1));
            list.add(make(id(2), "Ben", 2));
            list.add(make(id(3), "Cara", 3));
            list.add(make(id(4), "Dan", 4));
            list.add(make(id(5), "Eve", 5));
            if (c == 5) {
                ArrayList<Employee> reversed = new ArrayList<Employee>();
                for (int i = list.size() - 1; i >= 0; i--) {
                    reversed.add(list.get(i));
                }
                return reversed;
            }
        } else if (c == 6) {
            list.add(make(id(4), "Sam", 3));
            list.add(make(id(1), "Sam", 3));
            list.add(make(id(3), "Amy", 2));
            list.add(make(id(2), "Sam", 3));
            list.add(make(id(5), "Amy", 2));
        }
        return list;
    }
}
