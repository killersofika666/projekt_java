package studentdb;

import java.util.List;
import java.util.Scanner;

public class Menu {
    private StudentManager studentManager;
    private StudentService studentService;
    private Database database;
    private Scanner scanner;
    private int nextId = 1;

    public Menu(Database db) {
        this.database = db;
        this.studentManager = new StudentManager();
        this.scanner = new Scanner(System.in);
        this.studentService = new StudentService(studentManager, nextId);

        List<Student> loadedStudents = database.loadStudents();
        for (Student s : loadedStudents) {
            studentManager.addStudent(s);
            if (s.getId() >= nextId) {
                nextId = s.getId() + 1;
            }
        }
        studentService.setNextId(nextId); 
    }

    public void start() {
        boolean running = true;

        while (running) {
            printMenu();
            
            int choice = -1;
            System.out.print("Zvolte možnost: ");
            if (scanner.hasNextInt()) {
            	choice = scanner.nextInt();
                scanner.nextLine(); 
                if (choice < 0 || choice > 11) {
                    System.out.println("Neplatná volba! Zadejte číslo mezi 0 a 11.");
                    continue;
                }
            } else {
                System.out.println("Neplatný vstup! Zadejte číslo.");
                scanner.nextLine();
                continue;
            }
            
            switch (choice) {
                case 0:
                    recreateDatabase();
                    break;
                case 1:
                    addStudent();
                    break;
                case 2:
                    addGradeToStudent();
                    break;
                case 3:
                    removeStudent();
                    break;
                case 4:
                    searchStudents();
                    break;
                case 5:
                    showSkill();
                    break;
                case 6:
                    printAllStudents();
                    break;
                case 7:
                    printAverage();
                    break;
                case 8:
                    printStudentCounts();
                    break;
                case 9:
                    saveStudentToFile();
                    break;
                case 10:
                    loadStudentFromFile();
                    break;
                case 11:
                    database.saveStudents(studentManager.getAllStudents());
                    running = false;
                    System.out.println("Data byla uložena. Program se ukončuje...");
                    break;
                default:
                    System.out.println("Neplatná volba. Zkuste znovu.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== STUDENTSKÁ DATABÁZE ===");
        System.out.println("0. Vytvořit databázi (smazat vše a vytvořit znovu)");
        System.out.println("1. Přidat studenta");
        System.out.println("2. Přidat známku studentovi");
        System.out.println("3. Odstranit studenta");
        System.out.println("4. Najít studenta (ID / jméno / příjmení)");
        System.out.println("5. Ukázat dovednost studenta");
        System.out.println("6. Seznam studentů");
        System.out.println("7. Průměr studentů");
        System.out.println("8. Počet studentů");
        System.out.println("9. Uložit studenta do souboru");
        System.out.println("10. Načíst studenta ze souboru");
        System.out.println("11. Ukončení programu");
    }
    
    private void recreateDatabase() {
        System.out.println("!!! Tato operace odstraní všechna data. Pokračovat? (ano/ne)");
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("ano")) {
            database.recreateDatabase();
            studentManager.clearAllStudents();
            nextId = 1;
            studentService.setNextId(nextId);
            System.out.println("Databáze byla znovu vytvořena.");
        } else {
            System.out.println("Operace byla zrušena.");
        }
    }

    private void addStudent() {
        String firstName;
        while (true) {
            System.out.print("Zadejte jméno: ");
            firstName = scanner.nextLine().trim();
            if (firstName.matches("[a-zA-Zá-žÁ-Ž]+")) {
                break;
            } else {
                System.out.println("Neplatné jméno! Zadejte pouze písmena bez číslic a speciálních znaků.");
            }
        }

        String lastName;
        while (true) {
            System.out.print("Zadejte příjmení: ");
            lastName = scanner.nextLine().trim();
            if (lastName.matches("[a-zA-Zá-žÁ-Ž]+")) {
                break;
            } else {
                System.out.println("Neplatné příjmení! Zadejte pouze písmena bez číslic a speciálních znaků.");
            }
        }

        int yearOfBirth;
        while (true) {
            System.out.print("Zadejte rok narození: ");
            if (scanner.hasNextInt()) {
                yearOfBirth = scanner.nextInt();
                scanner.nextLine();
                if (yearOfBirth >= 1900 && yearOfBirth <= 2025) {
                    break;
                } else {
                    System.out.println("Neplatný rok! Rok musí být mezi 1900 a 2025.");
                }
            } else {
                System.out.println("Neplatný vstup! Zadejte číslo.");
                scanner.nextLine();
            }
        }

        int specialization;
        while (true) {
            System.out.println("Zvolte obor:");
            System.out.println("1. Telekomunikace");
            System.out.println("2. Kyberbezpečnost");

            if (scanner.hasNextInt()) {
                specialization = scanner.nextInt();
                scanner.nextLine();
                if (specialization == 1 || specialization == 2) {
                    break;
                } else {
                    System.out.println("Neplatná volba! Zadejte 1 nebo 2.");
                }
            } else {
                System.out.println("Neplatný vstup! Zadejte číslo.");
                scanner.nextLine();
            }
        }

        Student student;
        if (specialization == 1) {
            student = new TelecomStudent(nextId++, firstName, lastName, yearOfBirth);
        } else {
            student = new CyberStudent(nextId++, firstName, lastName, yearOfBirth);
        }

        studentManager.addStudent(student);

        System.out.println("\n=== Nový student přidán ===");
        System.out.println("ID: " + student.getId());
        System.out.println("Jméno: " + student.getFirstName());
        System.out.println("Příjmení: " + student.getLastName());
        System.out.println("Rok narození: " + student.getYearOfBirth());
        System.out.println("Obor: " + (specialization == 1 ? "Telekomunikace" : "Kyberbezpečnost"));
        System.out.println("============================\n");
    }

    
    private void addGradeToStudent() {
        System.out.print("Zadej ID studenta: ");
        if (scanner.hasNextInt()) {
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Zadejte známky oddělené mezerou: ");
            String gradesLine = scanner.nextLine();
            
            boolean success = studentService.addGrades(id, gradesLine);
            if (success) {
                System.out.println("Známky byly úspěšně přidány.");
            } else {
                System.out.println("Známky nebyly přidány.");
            }
        } else {
            System.out.println("Neplatný vstup! Očekává se číslo.");
            scanner.nextLine();
        }
    }

    private void removeStudent() {
        System.out.print("Zadejte ID studenta k odstranění: ");
        
        if (scanner.hasNextInt()) {
        	int id = scanner.nextInt();
            scanner.nextLine();
            
            boolean removed = studentService.removeStudent(id);
            if (removed) {
                System.out.println("Student byl odstraněn.");
            } else {
                System.out.println("Student nenalezen.");
            }
        } else {
            System.out.println("Neplatný vstup! Očekává se číslo.");
            scanner.nextLine();
        }
    }
    
    private void searchStudents() {
        System.out.print("Zadejte ID, jméno nebo příjmení studenta: ");
        String input = scanner.nextLine();
        List<Student> found = studentService.findStudentFlexible(input);
        if (found.isEmpty()) {
            System.out.println("Student nebyl nalezen.");
        } else {
            for (Student s : found) {
                System.out.println(s);
            }
        }
    }
    
    private void showSkill() {
        System.out.print("Zadejte ID studenta: ");
        if (scanner.hasNextInt()) {
            int id = scanner.nextInt();
            scanner.nextLine();
            studentService.showSkill(id);
        } else {
            System.out.println("Neplatný vstup! Zadejte číslo.");
            scanner.nextLine();
        }
    }

    private void printAllStudents() {
        System.out.println("\n=== Výpis studentů (podle příjmení) ===");
        System.out.println("1. Všechny studenty");
        System.out.println("2. Studenty oboru Telekomunikace");
        System.out.println("3. Studenty oboru Kyberbezpečnost");
        System.out.print("Zvolte možnost: ");
        
        if (!scanner.hasNextInt()) {
            System.out.println("Neplatná volba! Musíte zadat číslo.");
            scanner.nextLine(); 
            return;
        }

        int choice = scanner.nextInt();
        scanner.nextLine(); 

        List<Student> students = studentManager.getStudentsSortedByLastName();

        switch (choice) {
            case 1:
                for (Student s : students) {
                    System.out.println(s);
                }
                break;
            case 2:
                for (Student s : students) {
                    if (s instanceof TelecomStudent) {
                        System.out.println(s);
                    }
                }
                break;
            case 3:
                for (Student s : students) {
                    if (s instanceof CyberStudent) {
                        System.out.println(s);
                    }
                }
                break;
            default:
                System.out.println("Neplatná volba.");
        }
    }

    private void printAverage() {
        System.out.println("\n=== Výpočet průměrů ===");
        System.out.println("1. Průměr všech studentů");
        System.out.println("2. Průměr studentů oboru Telekomunikace");
        System.out.println("3. Průměr studentů oboru Kyberbezpečnost");
        System.out.print("Zvolte možnost: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Neplatná volba! Musíte zadat číslo.");
            scanner.nextLine(); 
            return;
        }

        int choice = scanner.nextInt();
        scanner.nextLine(); 

        double average = 0.0;

        switch (choice) {
            case 1:
                average = studentManager.averageGrageForAll();
                System.out.printf("Průměr všech studentů: %.2f\n", average);
                break;
            case 2:
                average = studentManager.averageGradeForSpecialization("telecom");
                System.out.printf("Průměr studentů Telekomunikace: %.2f\n", average);
                break;
            case 3:
                average = studentManager.averageGradeForSpecialization("cyber");
                System.out.printf("Průměr studentů Kyberbezpečnost: %.2f\n", average);
                break;
            default:
                System.out.println("Neplatná volba.");
        }
    }
    
    private void printStudentCounts() {
        studentService.printStudentCounts();
    }

    private void saveStudentToFile() {
        System.out.print("Zadejte název souboru (např. 'soubor.txt'): ");
        String filename = scanner.nextLine();
        boolean append = false;

        while (true) {
            System.out.print("Zadejte ID studenta k uložení: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            Student student = studentManager.findStudentById(id);
            if (student != null) {
                FileManager.saveStudentToTextFile(student, filename, append);
                append = true;
            } else {
                System.out.println("Student nenalezen.");
            }

            System.out.print("Chcete uložit dalšího studenta? (ano/ne): ");
            String answer = scanner.nextLine();
            if (!answer.equalsIgnoreCase("ano")) {
                break;
            }
        }
    }

    private void loadStudentFromFile() {
        System.out.print("Zadejte název souboru: ");
        String filename = scanner.nextLine();

        List<Student> students = FileManager.loadAllStudentsFromTextFile(filename);
        if (students.isEmpty()) {
            System.out.println("Žádní studenti nebyli načteni.");
        } else {
            for (Student s : students) {
                s.setId(nextId++);
                studentManager.addStudent(s);
            }
            studentService.setNextId(nextId);
            System.out.println("Studenti byli úspěšně načteni ze souboru.");
        }
    }
}
