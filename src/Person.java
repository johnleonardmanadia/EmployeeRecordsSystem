/**
 * Person - the parent (super) class.
 * It stores only the data shared by every kind of Person: a name.
 */
public class Person {

    // Encapsulation: the field is private, so it can only be changed through methods.
    private String name;

    // Constructor
    public Person(String name) {
        this.name = name;
    }

    // Getter
    public String getName() {
        return name;
    }

    // Setter
    public void setName(String name) {
        this.name = name;
    }

    // These two methods are overridden in Employee (polymorphism).
    public String getRole() {
        return "Person";
    }

    public String getInfo() {
        return "Name: " + name;
    }
}
