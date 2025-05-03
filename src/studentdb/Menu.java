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
        studentService.setNextId(nextId); // обязательно обновляем nextId в сервисе!
    }

    public void start() {
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Zvolte možnost: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // очистка ввода

            switch (choice) {
                case 0:
                    recreateDatabase();
                    break;
                case 1:
                    addStudent();
                    break;
                case 2:
                    searchStudents();
                    break;
                case 3:
                    removeStudent();
                    break;
                case 4:
                    showSkill();
                    break;
                case 5:
                    printAllStudents();
                    break;
                case 6:
                    printAverage();
                    break;
                case 7:
                    saveStudentToFile();
                    break;
                case 8:
                    loadStudentFromFile();
                    break;
                case 9:
                    database.saveStudents(studentManager.getAllStudents());
                    running = false;
                    System.out.println("Data byla uložena. Program se ukončuje...");
                    break;
                case 10:
                    addGradeToStudent();
                    break;
                case 11:
                    printStudentCounts();
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
        System.out.println("2. Najít studenta (ID / jméno / příjmení)");
        System.out.println("3. Odstranit studenta podle ID");
        System.out.println("4. Ukázat dovednost studenta");
        System.out.println("5. Vypsat všechny studenty podle příjmení");
        System.out.println("6. Vypočítat průměr všech studentů");
        System.out.println("7. Uložit studenta do souboru");
        System.out.println("8. Načíst studenta ze souboru");
        System.out.println("9. Konec");
        System.out.println("10. Přidat známku studentovi");
        System.out.println("11. Vypsat počet studentů podle skupin");
    }

    private void addStudent() {
        System.out.print("Zadejte jméno: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Zadejte příjmení: ");
        String lastName = scanner.nextLine().trim();

        int yearOfBirth;
        while (true) {
            System.out.print("Zadejte rok narození: ");
            if (scanner.hasNextInt()) {
                yearOfBirth = scanner.nextInt();
                scanner.nextLine();
                if (yearOfBirth >= 1900 && yearOfBirth <= 2015) {
                    break;
                } else {
                    System.out.println("Neplatný rok! Rok musí být mezi 1900 a 2015.");
                }
            } else {
                System.out.println("Neplatný vstup! Zadej číslo.");
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
                    System.out.println("Neplatná volba! Zadej 1 nebo 2.");
                }
            } else {
                System.out.println("Neplatný vstup! Zadej číslo.");
                scanner.nextLine();
            }
        }

        Student student = studentService.addStudent(firstName, lastName, yearOfBirth, specialization);
        if (student != null) {
            System.out.println("\n=== Nový student přidán ===");
            System.out.println("ID: " + student.getId());
            System.out.println("Jméno: " + student.getFirstName());
            System.out.println("Příjmení: " + student.getLastName());
            System.out.println("Rok narození: " + student.getYearOfBirth());
            System.out.println("Obor: " + (student instanceof TelecomStudent ? "Telekomunikace" : "Kyberbezpečnost"));
            System.out.println("============================\n");
            nextId = studentService.getNextId(); // Обновляем nextId после добавления
        } else {
            System.out.println("Chyba při přidávání studenta.");
        }
    }

    private void removeStudent() {
        System.out.print("Zadej ID studenta k odstranění: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        boolean removed = studentService.removeStudent(id);
        if (removed) {
            System.out.println("Student byl odstraněn.");
        } else {
            System.out.println("Student nenalezen.");
        }
    }

    private void showSkill() {
        System.out.print("Zadej ID studenta: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        studentService.showSkill(id);
    }

    private void printAllStudents() {
        List<Student> sortedStudents = studentService.getAllStudentsSorted();
        for (Student s : sortedStudents) {
            System.out.println(s);
        }
    }

    private void printAverage() {
        System.out.println("\n=== Výpočet průměrů ===");
        System.out.println("1. Průměr všech studentů");
        System.out.println("2. Průměr studentů oboru Telekomunikace");
        System.out.println("3. Průměr studentů oboru Kyberbezpečnost");
        System.out.print("Zvolte možnost: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        double average = studentService.calculateAverage(choice);
        if (average == 0.0) {
            System.out.println("Žádná data pro výpočet průměru.");
        } else {
            System.out.printf("Výpočet průměru: %.2f\n", average);
        }
    }

    private void saveStudentToFile() {
        System.out.print("Zadej název souboru: ");
        String filename = scanner.nextLine();
        boolean append = false;

        while (true) {
            System.out.print("Zadej ID studenta k uložení: ");
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
        System.out.print("Zadej název souboru: ");
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

    private void addGradeToStudent() {
        System.out.print("Zadej ID studenta: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Zadej známky oddělené mezerou: ");
        String gradesLine = scanner.nextLine();

        boolean success = studentService.addGrades(id, gradesLine);
        if (success) {
            System.out.println("Známky byly úspěšně přidány.");
        } else {
            System.out.println("Známky nebyly přidány.");
        }
    }

    private void printStudentCounts() {
        studentService.printStudentCounts();
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

    private void searchStudents() {
        System.out.print("Zadej ID, jméno nebo příjmení studenta: ");
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
}
