package studentdb;

import java.util.ArrayList;
import java.util.List;

public class StudentService {
    private StudentManager studentManager;
    private int nextId;

    public StudentService(StudentManager studentManager, int nextId) {
        this.studentManager = studentManager;
        this.nextId = nextId;
    }

    public int getNextId() {
        return nextId;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }

    public Student addStudent(String firstName, String lastName, int yearOfBirth, int specialization) {
        Student student = null;
        if (specialization == 1) {
            student = new TelecomStudent(nextId++, firstName, lastName, yearOfBirth);
        } else if (specialization == 2) {
            student = new CyberStudent(nextId++, firstName, lastName, yearOfBirth);
        }

        if (student != null) {
            studentManager.addStudent(student);
        }

        return student;
    }

    public boolean removeStudent(int id) {
        return studentManager.removeStudentById(id);
    }

    public void showSkill(int id) {
        Student student = studentManager.findStudentById(id);
        if (student != null) {
            student.showSkill();
        } else {
            System.out.println("Student nenalezen.");
        }
    }

    public List<Student> getAllStudentsSorted() {
        return studentManager.getStudentsSortedByLastName();
    }

    public double calculateAverage(int choice) {
        switch (choice) {
            case 1:
                return studentManager.averageGrageForAll();
            case 2:
                return studentManager.averageGradeForSpecialization("telecom");
            case 3:
                return studentManager.averageGradeForSpecialization("cyber");
            default:
                return 0.0;
        }
    }

    public boolean addGrades(int id, String gradesLine) {
        Student student = studentManager.findStudentById(id);
        if (student != null) {
            String[] grades = gradesLine.split(" ");
            boolean addedAny = false;

            for (String gradeStr : grades) {
                try {
                    int grade = Integer.parseInt(gradeStr);
                    if (grade >= 1 && grade <= 5) {
                        student.addGrade(grade);
                        addedAny = true;
                    } else {
                        System.out.println("Známka " + grade + " je mimo platný rozsah (1-5) a nebyla přidána.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Neplatný vstup: " + gradeStr + " není číslo.");
                }
            }

            return addedAny;
        } else {
            System.out.println("Student nenalezen.");
            return false;
        }
    }

    public void printStudentCounts() {
        int telecomCount = 0;
        int cyberCount = 0;

        for (Student student : studentManager.getAllStudents()) {
            if (student instanceof TelecomStudent) {
                telecomCount++;
            } else if (student instanceof CyberStudent) {
                cyberCount++;
            }
        }

        System.out.println("Počet studentů v oboru Telekomunikace: " + telecomCount);
        System.out.println("Počet studentů v oboru Kyberbezpečnost: " + cyberCount);
    }

    public List<Student> findStudentFlexible(String input) {
        List<Student> foundStudents = new ArrayList<>();

        try {
            int id = Integer.parseInt(input);
            Student student = studentManager.findStudentById(id);
            if (student != null) {
                foundStudents.add(student);
            }
        } catch (NumberFormatException e) {
            for (Student student : studentManager.getAllStudents()) {
                String firstName = student.getFirstName().toLowerCase();
                String lastName = student.getLastName().toLowerCase();
                String searchInput = input.toLowerCase();

                if (firstName.contains(searchInput) || lastName.contains(searchInput)) {
                    foundStudents.add(student);
                }
            }
        }
        return foundStudents;
    }
}
