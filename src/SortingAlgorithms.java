
import java.util.ArrayList;

/**
 * SortingAlgorithms - five sorting algorithms written by hand.
 * Every algorithm sorts ArrayList<Employee> in ASCENDING order.
 *
 * sortBy: 1 = Employee ID, 2 = Name, 3 = Department, 4 = Position, 5 = Salary
 */
public class SortingAlgorithms {

    // ------------------------------------------------------------
    // HELPERS (finished)
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
    // Idea: compare neighbors and swap if they are in the wrong order.
    // After each pass, the biggest remaining item "bubbles" to the end.
    // ------------------------------------------------------------
    public void bubbleSort(ArrayList<Employee> records, int sortBy) {
        int n = records.size();
        for (int pass = 0; pass < n - 1; pass++) {
            boolean swapped = false;
            // the last 'pass' items are already in place, so skip them
            for (int j = 0; j < n - 1 - pass; j++) {
                if (compare(records.get(j), records.get(j + 1), sortBy) > 0) {
                    swap(records, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) {
                return; // no swaps in a full pass -> already sorted
            }
        }
    }

    // ------------------------------------------------------------
    // 2. INSERTION SORT
    // Idea: like sorting playing cards in your hand. Take the next item
    // and slide it left until it sits in the right spot.
    // ------------------------------------------------------------
    public void insertionSort(ArrayList<Employee> records, int sortBy) {
        for (int i = 1; i < records.size(); i++) {
            Employee key = records.get(i);   // the item we are inserting
            int j = i - 1;
            // shift bigger items one step to the right
            while (j >= 0 && compare(records.get(j), key, sortBy) > 0) {
                records.set(j + 1, records.get(j));
                j--;
            }
            records.set(j + 1, key);         // drop the key into the gap
        }
    }

    // ------------------------------------------------------------
    // 3. SELECTION SORT
    // Idea: find the smallest item in the unsorted part and put it
    // at the front. Repeat for the next position.
    // ------------------------------------------------------------
    public void selectionSort(ArrayList<Employee> records, int sortBy) {
        int n = records.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (compare(records.get(j), records.get(minIndex), sortBy) < 0) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                swap(records, i, minIndex);
            }
        }
    }

    // ------------------------------------------------------------
    // 4. MERGE SORT
    // Idea: split the list in half, sort each half (recursion),
    // then MERGE the two sorted halves into one sorted list.
    // ------------------------------------------------------------
    public void mergeSort(ArrayList<Employee> records, int sortBy) {
        if (records.size() < 2) {
            return; // empty or one record is already sorted
        }
        mergeSortHelper(records, 0, records.size() - 1, sortBy);
    }

    private void mergeSortHelper(ArrayList<Employee> records, int left, int right, int sortBy) {
        if (left >= right) {
            return; // one item -> nothing to do
        }
        int mid = (left + right) / 2;
        mergeSortHelper(records, left, mid, sortBy);       // sort left half
        mergeSortHelper(records, mid + 1, right, sortBy);  // sort right half
        merge(records, left, mid, right, sortBy);          // combine them
    }

    // Merges records[left..mid] and records[mid+1..right], both already sorted.
    private void merge(ArrayList<Employee> records, int left, int mid, int right, int sortBy) {
        ArrayList<Employee> temp = new ArrayList<Employee>();
        int i = left;      // pointer in the left half
        int j = mid + 1;   // pointer in the right half

        // take the smaller front item each time (<= keeps equal items in original order)
        while (i <= mid && j <= right) {
            if (compare(records.get(i), records.get(j), sortBy) <= 0) {
                temp.add(records.get(i));
                i++;
            } else {
                temp.add(records.get(j));
                j++;
            }
        }
        // copy whatever is left over
        while (i <= mid) {
            temp.add(records.get(i));
            i++;
        }
        while (j <= right) {
            temp.add(records.get(j));
            j++;
        }
        // copy the merged result back into the original list
        for (int k = 0; k < temp.size(); k++) {
            records.set(left + k, temp.get(k));
        }
    }

    // ------------------------------------------------------------
    // 5. QUICK SORT
    // Idea: pick a pivot, put smaller items on its left and bigger
    // items on its right, then sort each side (recursion).
    // ------------------------------------------------------------
    public void quickSort(ArrayList<Employee> records, int sortBy) {
        if (records.size() < 2) {
            return;
        }
        quickSortHelper(records, 0, records.size() - 1, sortBy);
    }

    private void quickSortHelper(ArrayList<Employee> records, int low, int high, int sortBy) {
        if (low < high) {
            int pivotIndex = partition(records, low, high, sortBy);
            quickSortHelper(records, low, pivotIndex - 1, sortBy);   // left of pivot
            quickSortHelper(records, pivotIndex + 1, high, sortBy);  // right of pivot
        }
    }

    // Uses the LAST item as the pivot. Returns the pivot's final position.
    private int partition(ArrayList<Employee> records, int low, int high, int sortBy) {
        Employee pivot = records.get(high);
        int i = low - 1;   // end of the "smaller or equal" zone
        for (int j = low; j < high; j++) {
            if (compare(records.get(j), pivot, sortBy) <= 0) {
                i++;
                swap(records, i, j);
            }
        }
        swap(records, i + 1, high); // put the pivot between the two zones
        return i + 1;
    }
}