package learning;

public class Subject {
    protected String name;
    protected String description;

    public Subject(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    @Override
    public String toString() {
        return "Subject " + name + " description: " + description + "\n";
    }
}
