/**
 * Employee - a child (sub) class of Person.
 * It inherits "name" from Person and adds its own data.
 */
public class Employee extends Person {

    private String employeeId;
    private String department;
    private String position;
    private double salary;

    // Constructor: calls the parent constructor with super(...)
    public Employee(String employeeId, String name, String department, String position, double salary) {
        super(name);
        this.employeeId = employeeId;
        this.department = department;
        this.position = position;
        this.salary = salary;
    }

    // Getters
    public String getEmployeeId() {
        return employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public String getPosition() {
        return position;
    }

    public double getSalary() {
        return salary;
    }

    // Setters (there is no setEmployeeId because an ID should never change)
    public void setDepartment(String department) {
        this.department = department;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    // Method overriding (polymorphism): replaces the versions in Person.
    @Override
    public String getRole() {
        return "Employee";
    }

    @Override
    public String getInfo() {
        return "Role      : " + getRole() + "\n"
             + "Employee ID: " + employeeId + "\n"
             + "Name: " + getName() + "\n"
             + "Department: " + department + "\n"
             + "Position: " + position + "\n"
             + "Salary: " + String.format("%.2f", salary);
    }
}
