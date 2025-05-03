package studentdb;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    // Сохранить одного студента в файл
    public static void saveStudentToTextFile(Student student, String filename, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, append))) {
            StringBuilder sb = new StringBuilder();
            sb.append(student.getFirstName()).append(",");
            sb.append(student.getLastName()).append(",");
            sb.append(student.getYearOfBirth()).append(",");
            if (student instanceof TelecomStudent) {
                sb.append("telecom").append(",");
            } else if (student instanceof CyberStudent) {
                sb.append("cyber").append(",");
            }
            for (int grade : student.getGrades()) {
                sb.append(grade).append(" ");
            }
            writer.write(sb.toString().trim()); // убираем последний пробел
            writer.newLine();
            System.out.println("Student byl úspěšně uložen do souboru.");
        } catch (IOException e) {
            System.out.println("Chyba při ukládání do souboru.");
            e.printStackTrace();
        }
    }

    // Загрузить всех студентов из файла
    public static List<Student> loadAllStudentsFromTextFile(String filename) {
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Student student = loadStudentFromTextLine(line);
                if (student != null) {
                    students.add(student);
                }
            }
            System.out.println("Všichni studenti byli úspěšně načteni ze souboru.");
        } catch (IOException e) {
            System.out.println("Chyba při načítání studentů ze souboru.");
            e.printStackTrace();
        }
        return students;
    }

    // Помощник: создать студента из одной строки
    private static Student loadStudentFromTextLine(String line) {
        try {
            String[] parts = line.split(",", 5); // максимум 5 частей
            if (parts.length < 4) {
                return null;
            }

            String firstName = parts[0];
            String lastName = parts[1];
            int yearOfBirth = Integer.parseInt(parts[2]);
            String specialization = parts[3];

            Student student = null;
            if ("telecom".equalsIgnoreCase(specialization)) {
                student = new TelecomStudent(-1, firstName, lastName, yearOfBirth);
            } else if ("cyber".equalsIgnoreCase(specialization)) {
                student = new CyberStudent(-1, firstName, lastName, yearOfBirth);
            }

            if (student != null && parts.length == 5) {
                String[] grades = parts[4].trim().split(" ");
                for (String g : grades) {
                    if (!g.isEmpty()) {
                        student.addGrade(Integer.parseInt(g));
                    }
                }
            }

            return student;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
