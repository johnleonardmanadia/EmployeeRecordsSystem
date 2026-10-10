import java.util.ArrayList;

/**
 * SortingAlgorithms - YOU must write five sorting algorithms by hand.
 * Every algorithm must sort ArrayList<Employee> in ASCENDING order.
 *
 * sortBy: 1 = Employee ID, 2 = Name, 3 = Department, 4 = Position, 5 = Salary
 *
 * Use the two helper methods below (compare and swap) inside your algorithms.
 * You MAY add your own private helper methods (for example for Merge Sort
 * and Quick Sort).
 *
 * NOT allowed: Collections.sort(), list.sort(), Arrays.sort(),
 *              Comparator, lambdas, streams.
 */
public class SortingAlgorithms {

    // ------------------------------------------------------------
    // HELPERS (finished - use them!)
    // ------------------------------------------------------------

    // Compares two records using the chosen field.
    // Returns negative if a comes first, positive if b comes first, 0 if equal.
    public int compare(Employee a, Employee b, int sortBy) {
        if (sortBy == 1) {
            return a.getEmployeeId().compareTo(b.getEmployeeId());
        }
        if (sortBy == 2) {
            return a.getName().compareToIgnoreCase(b.getName());
        }
        if (sortBy == 3) {
            return a.getDepartment().compareToIgnoreCase(b.getDepartment());
        }
        if (sortBy == 4) {
            return a.getPosition().compareToIgnoreCase(b.getPosition());
        }
        if (sortBy == 5) {
            return Double.compare(a.getSalary(), b.getSalary());
        }
        return 0;
    }

    // Swaps the records at positions i and j.
    public void swap(ArrayList<Employee> records, int i, int j) {
        Employee temp = records.get(i);
        records.set(i, records.get(j));
        records.set(j, temp);
    }

    // ------------------------------------------------------------
    // 1. BUBBLE SORT
    // ------------------------------------------------------------
    public void bubbleSort(ArrayList<Employee> records, int sortBy) {
        // TODO: STUDENT TASK
    }

    // ------------------------------------------------------------
    // 2. INSERTION SORT
    // ------------------------------------------------------------
    public void insertionSort(ArrayList<Employee> records, int sortBy) {
        // TODO: STUDENT TASK
    }

    // ------------------------------------------------------------
    // 3. SELECTION SORT
    // ------------------------------------------------------------
    public void selectionSort(ArrayList<Employee> records, int sortBy) {
        // TODO: STUDENT TASK
    }

    // ------------------------------------------------------------
    // 4. MERGE SORT
    // (You will probably need extra helper methods. Add them below.)
    // ------------------------------------------------------------
    public void mergeSort(ArrayList<Employee> records, int sortBy) {
        // TODO: STUDENT TASK
    }

    // ------------------------------------------------------------
    // 5. QUICK SORT
    // (You will probably need extra helper methods. Add them below.)
    // ------------------------------------------------------------
    public void quickSort(ArrayList<Employee> records, int sortBy) {
        // TODO: STUDENT TASK
    }
}
