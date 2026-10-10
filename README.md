# Employee Records System — Final Project

## 1. Project Description

A **Employee Records System** with a ready-made **web page**. The page talks to a small Java server running on your own computer.
Employee records (ID, name, department, position, salary) are stored in memory in an `ArrayList<Employee>`. When you stop the program, the data disappears.

The page, the server, and the `Person` / `Employee` classes are already written. **You only work on the Java backend:**
the missing parts of `EmployeeManager.java` and `SortingAlgorithms.java` — most importantly **Linear Search** and
**five sorting algorithms**.

## 2. Learning Objectives

- Write and use classes, objects, constructors, and encapsulation
- Use inheritance (`Employee extends Person`) and basic polymorphism
- Store and manage objects in an `ArrayList`
- Implement **Linear Search** manually
- Implement **Bubble, Insertion, Selection, Merge, and Quick Sort** manually
- Read, understand, and explain code written by your instructor, then extend it

## 3. Features

| #   | Feature                 | Status in your starter code                                                                               |
| --- | ----------------------- | --------------------------------------------------------------------------------------------------------- |
| 1   | Add Employee            | Works, but duplicate-ID check is missing                                                                  |
| 2   | Display Employees       | Works (the table on the page)                                                                             |
| 3   | Search Employee (by ID) | **You implement Linear Search**                                                                           |
| 4   | Update Employee         | Partly done — you finish it                                                                               |
| 5   | Delete Employee         | **You implement it**                                                                                      |
| 6   | Sort Employees          | Page works — **you implement the 5 algorithms** (sort by Employee ID, Name, Department, Position, Salary) |

## 4. Project Structure

```
Employee_Records_System/
├── src/
│   ├── Main.java              (starts the server - DO NOT EDIT)
│   ├── WebServer.java         (connects the page to your code - DO NOT EDIT)
│   ├── AutoChecker.java       (powers the Auto-Check panel - DO NOT EDIT)
│   ├── Person.java          (parent class - finished)
│   ├── Employee.java               (child class - finished)
│   ├── EmployeeManager.java             (ArrayList + operations - YOU finish the TODOs)
│   └── SortingAlgorithms.java (5 sorting algorithms - YOU write them)
├── web/
│   └── index.html             (the page - DO NOT EDIT)
└── README.md
```

## 5. How to Run

Requires a **full JDK 11 or newer** (not just a JRE).

**IDE (IntelliJ IDEA / Eclipse / NetBeans / VS Code):** create a new Java project, copy the `.java` files from `src/`
into the source folder (default package), and keep the `web/` folder next to `src/` in the project root. Run `Main`.

**Command line** (from inside the project folder):

```
mkdir out
javac -d out src/*.java
java -cp out Main
```

Then open the address printed in the console (normally **http://localhost:8080/**). A browser tab may open by itself.
After you change your Java code: stop the program, recompile, run again, and refresh the page.
If the page stops responding (for example an infinite loop in your code), stop the program and run it again.

## 6. Required Tasks

Search the code for `// TODO: STUDENT TASK`. You must complete:

**In `EmployeeManager.java`**

1. `addEmployee` — reject a record whose ID already exists
2. `findIndexById` — **Linear Search** by ID (return the index, or -1)
3. `updateEmployee` — change every field of the record (not the ID)
4. `deleteEmployee` — remove the record from the `ArrayList`

**In `SortingAlgorithms.java`** 5. `bubbleSort` 6. `insertionSort` 7. `selectionSort` 8. `mergeSort` 9. `quickSort`

Tip: do them in the order above. Search is needed first, because Add, Update, and Delete all use it.
Use the **Backend Auto-Check** panel on the page to test each task.

## 7. Required Sorting Algorithms

- All five must sort an `ArrayList<Employee>` in **ascending order**.
- Each must work for every sort field: **Employee ID, Name, Department, Position, Salary**.
  Use the provided `compare(a, b, sortBy)` helper. Use `swap(...)` where it helps.
- Each must work on an empty list, a one-record list, and a list with duplicates.
- You may add your own private helper methods (Merge Sort and Quick Sort normally need them).
- Write each algorithm yourself. You must be able to explain every line.

## 8. OOP Requirements

- **Classes and objects** — `Employee` objects stored in the list
- **Constructors** — including `super(...)` in `Employee`
- **Encapsulation** — private fields with getters/setters
- **Inheritance** — `Employee extends Person`
- **Basic polymorphism** — `getRole()` and `getInfo()` are overridden in `Employee`
  Do not add extra classes or extra inheritance levels.

## 9. Restrictions

You may NOT use:

- `Collections.sort()`, `list.sort()`, `Arrays.sort()`, `Comparator`
- Lambdas, streams, or any other advanced Java feature
- Built-in search to replace Linear Search (`indexOf`, `contains`, etc.)
- Files, databases, GUI libraries, `try/catch`, or external libraries
- AI-generated or copied code that you cannot explain

You may NOT edit `Main.java`, `WebServer.java`, `AutoChecker.java`, or `web/index.html`, and you may NOT change the method signatures in the starter code.

## 10. Submission Instructions

1. Test your program with the Auto-Check panel and the scenarios in Section 12.
2. Make sure it compiles with no errors.
3. Zip the whole project folder and name it `Employee_Records_System.zip`.
4. Submit the ZIP by the deadline set by your instructor.

## 11. Defense Instructions

You will explain your own code to the panels.

- Be ready to run the program live and demonstrate every feature.
- You may be asked to explain any method line by line.
- You may be asked to make a small change on the spot (for example, sort in descending order).
- Practice by tracing each algorithm on a 4-5 record list on paper.
- If you cannot explain your code, your score for that part may be reduced to zero.

## 12. Suggested Self-Tests

Press **Run Auto-Check** often; it shows PASS or FAIL for each task with a hint. Also test by hand: add, search
(existing and missing ID), edit, delete, duplicate ID, **Clear all** (empty list), one record, and each algorithm by
every sort field. Try an already sorted list, a reverse-sorted list, duplicate values, and a list with only two records.
**A green Auto-Check does not guarantee a good grade — you must still be able to explain your code.**

Good luck!
