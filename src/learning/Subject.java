package learning;
import java.util.*;

import utilityAndServices.UtilityClass;
import utilityAndServices.crudInterface;

public class Subject extends crudInterface<Subject> implements Comparable<Subject> {
    protected String name;
    protected String description;
    private static int subjectsNo;

    private final UUID id;

    public Subject(String name, String description) {
        this(UUID.randomUUID(), name, description);
    }

    public Subject(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    @Override
    public String getObjectType() {
        return "Subject";
    }

    @Override
    public List<Object> getParametersValues() { // se respecta ordinea atributelor ordinea aributelor
        List<Object> lista = new ArrayList<>(List.of(id.toString(), name, description));
        return lista;
    }

    @Override
    public Subject read() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the subject:");
        this.name = scanner.nextLine();
        System.out.println("Enter the description of the subject:");
        this.description = scanner.nextLine();
        return this;
    }

    @Override
    public int compareTo(Subject subject) {
        return this.name.compareTo(subject.name);
    }

    @Override
    public String toString() {
        return "Subject " + name + " description: " + description + "\n";
    }
}
