
import java.util.ArrayList;

/**
 * EmployeeManager - owns the ArrayList<Employee> and performs
 * add / search / update / delete / sort operations for the web page.
 */
public class EmployeeManager {

    private ArrayList<Employee> records;
    private SortingAlgorithms sorter;

    public EmployeeManager() {
        records = new ArrayList<Employee>();
        sorter = new SortingAlgorithms();
        loadSampleData();
    }

    // Sample data. Intentionally NOT in sorted order, with duplicate values
    // in several fields so sorting can be tested properly.
    private void loadSampleData() {
        records.add(new Employee("EMP-005", "Carlo Mendoza", "IT", "Developer", 45000.00));
        records.add(new Employee("EMP-002", "Maria Santos", "HR", "Manager", 60000.00));
        records.add(new Employee("EMP-008", "Liza Ramos", "Finance", "Accountant", 45000.00));
        records.add(new Employee("EMP-001", "Juan Dela Cruz", "IT", "Manager", 70000.00));
        records.add(new Employee("EMP-004", "Ana Garcia", "Finance", "Analyst", 52000.00));
        records.add(new Employee("EMP-007", "Maria Santos", "IT", "Developer", 38000.50));
        records.add(new Employee("EMP-003", "Pedro Reyes", "HR", "Clerk", 28000.00));
        records.add(new Employee("EMP-006", "Ben Torres", "IT", "Developer", 45000.00));
    }

    // ------------------------------------------------------------
    // LIST + TEST TOOLS (finished - used by the web page)
    // ------------------------------------------------------------
    public ArrayList<Employee> getEmployees() {
        return records;
    }

    // Puts the sample records back (the page's "Load sample data" button).
    public void resetSampleData() {
        records.clear();
        loadSampleData();
    }

    // Removes every record (the page's "Clear all" button) - handy for testing an empty list.
    public void clearAll() {
        records.clear();
    }

    // ------------------------------------------------------------
    // ADD
    // ------------------------------------------------------------
    public boolean addEmployee(Employee record) {
        // If the ID is already in the list, findIndexById gives a number that is NOT -1.
        if (findIndexById(record.getEmployeeId()) != -1) {
            return false; // duplicate ID -> do not add
        }

        records.add(record);
        return true;
    }

    // ------------------------------------------------------------
    // LINEAR SEARCH (by ID)
    // ------------------------------------------------------------
    // Returns the index of the record with this ID, or -1 if not found.
    public int findIndexById(String id) {
        // Check each record one by one, from index 0 to the end.
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getEmployeeId().equals(id)) {
                return i; // found it, stop here
            }
        }
        return -1; // went through everything and never found it
    }

    // Returns the Employee object, or null if not found. (finished)
    public Employee searchById(String id) {
        int index = findIndexById(id);
        if (index == -1) {
            return null;
        }
        return records.get(index);
    }

    // ------------------------------------------------------------
    // UPDATE
    // ------------------------------------------------------------
    public boolean updateEmployee(String id, String name, String department, String position, double salary) {
        Employee r = searchById(id);
        if (r == null) {
            return false;
        }

        // Change every field using the setters (the ID is NOT changed).
        r.setName(name);             // setName is inherited from Person
        r.setDepartment(department);
        r.setPosition(position);
        r.setSalary(salary);

        return true;
    }

    // ------------------------------------------------------------
    // DELETE
    // ------------------------------------------------------------
    public boolean deleteEmployee(String id) {
        int index = findIndexById(id);   // 1. find where it is
        if (index == -1) {               // 2. not found -> nothing to delete
            return false;
        }
        records.remove(index);           // 3. remove that position
        return true;
    }

    // ------------------------------------------------------------
    // SORT (finished - it only CALLS your algorithms)
    // algorithm: 1=Bubble 2=Insertion 3=Selection 4=Merge 5=Quick
    // sortBy   : see getSortFieldName below
    // ------------------------------------------------------------
    public void sortEmployees(int algorithm, int sortBy) {
        if (algorithm == 1) {
            sorter.bubbleSort(records, sortBy);
        } else if (algorithm == 2) {
            sorter.insertionSort(records, sortBy);
        } else if (algorithm == 3) {
            sorter.selectionSort(records, sortBy);
        } else if (algorithm == 4) {
            sorter.mergeSort(records, sortBy);
        } else if (algorithm == 5) {
            sorter.quickSort(records, sortBy);
        }
    }

    public String getAlgorithmName(int algorithm) {
        if (algorithm == 1) return "Bubble Sort";
        if (algorithm == 2) return "Insertion Sort";
        if (algorithm == 3) return "Selection Sort";
        if (algorithm == 4) return "Merge Sort";
        return "Quick Sort";
    }

    public String getSortFieldName(int sortBy) {
        if (sortBy == 1) return "Employee ID";
        if (sortBy == 2) return "Name";
        if (sortBy == 3) return "Department";
        if (sortBy == 4) return "Position";
        return "Salary";
    }
}