package learning;

import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class AccreditedCourse extends Course {
    protected int accreditationPeriod; // in years
    protected Level accreditationLevel;

    public AccreditedCourse(double price, String name, Difficulty difficulty, List<Quiz> quizList, double length, User teacher, int accreditationPeriod, Level accreditationLevel) {
        super(price, name, difficulty, quizList, length, teacher);
        this.accreditationLevel = accreditationLevel;
        this.accreditationPeriod = accreditationPeriod;
    }
    public AccreditedCourse() {
        super();
    }

    @Override
    public List<Object> getParametersValues() { // se respecta ordinea atributelor ordinea aributelor
        List<Object> lista = super.getParametersValues(); // Call superclass method
        lista.addAll(List.of(accreditationLevel, accreditationPeriod)); // Add accreditation level and period
        return lista;
    }

    @Override
    public AccreditedCourse read() {
        Scanner scanner = new Scanner(System.in);
        super.read();
        System.out.println("Enter accreditation period (in years):");
        this.accreditationPeriod = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter accreditation level:");
        this.accreditationLevel = Level.valueOf(scanner.nextLine());
        return this;
    }
    @Override
    public String toString() {
        return "Accredited " + super.toString();
    }
}
