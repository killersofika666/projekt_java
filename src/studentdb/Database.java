package studentdb;

import java.sql.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:studentydb.db");
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void initializaceDatabase() {
        try (
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            BufferedReader reader = new BufferedReader(new FileReader("sql/init.sql"))
        ) {
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line);
            }
            String[] queries = sql.toString().split(";");
            for (String query : queries) {
                if (!query.trim().isEmpty()) {
                    stmt.executeUpdate(query);
                }
            }
            System.out.println("Database initialized successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveStudents(List<Student> students) {
        try (Connection conn = connect()) {
            conn.setAutoCommit(false); // чтобы сделать всё одной транзакцией

            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = OFF;");
                stmt.executeUpdate("DELETE FROM Grade;");
                stmt.executeUpdate("DELETE FROM Student;");
                stmt.execute("PRAGMA foreign_keys = ON;");
            }

            String insertStudentSql = "INSERT INTO Student (id, name, surname, birthYear, groupType) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement studentStmt = conn.prepareStatement(insertStudentSql)) {

                String insertGradeSql = "INSERT INTO Grade (student_id, grade) VALUES (?, ?)";
                try (PreparedStatement gradeStmt = conn.prepareStatement(insertGradeSql)) {

                    for (Student s : students) {
                        studentStmt.setInt(1, s.getId());
                        studentStmt.setString(2, s.getFirstName());
                        studentStmt.setString(3, s.getLastName());
                        studentStmt.setInt(4, s.getYearOfBirth());
                        studentStmt.setString(5, (s instanceof TelecomStudent) ? "telecom" : "cyber");
                        studentStmt.addBatch();

                        for (Integer grade : s.getGrades()) {
                            gradeStmt.setInt(1, s.getId());
                            gradeStmt.setInt(2, grade);
                            gradeStmt.addBatch();
                        }
                    }

                    studentStmt.executeBatch();
                    gradeStmt.executeBatch();
                }
            }

            conn.commit();
            System.out.println("Data saved to database successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            try (Connection conn = connect()) {
                if (conn != null) conn.rollback();
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }

    public List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        try (Connection conn = connect()) {
            if (conn != null) {
                String sql = "SELECT * FROM Student";
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {

                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        String surname = rs.getString("surname");
                        int birthYear = rs.getInt("birthYear");
                        String groupType = rs.getString("groupType");

                        Student student = null;
                        if ("telecom".equalsIgnoreCase(groupType)) {
                            student = new TelecomStudent(id, name, surname, birthYear);
                        } else if ("cyber".equalsIgnoreCase(groupType)) {
                            student = new CyberStudent(id, name, surname, birthYear);
                        }

                        if (student != null) {
                            String gradeQuery = "SELECT grade FROM Grade WHERE student_id = ?";
                            try (PreparedStatement gradeStmt = conn.prepareStatement(gradeQuery)) {
                                gradeStmt.setInt(1, id);
                                try (ResultSet gradeRs = gradeStmt.executeQuery()) {
                                    while (gradeRs.next()) {
                                        student.addGrade(gradeRs.getInt("grade"));
                                    }
                                }
                            }
                            students.add(student);
                        }
                    }
                }
            }
            System.out.println("Data loaded from database successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public void recreateDatabase() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             BufferedReader reader = new BufferedReader(new FileReader("sql/init.sql"))) {

            stmt.execute("PRAGMA foreign_keys = OFF;");
            stmt.executeUpdate("DROP TABLE IF EXISTS Grade;");
            stmt.executeUpdate("DROP TABLE IF EXISTS Student;");
            stmt.execute("PRAGMA foreign_keys = ON;");

            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line);
            }
            String[] queries = sql.toString().split(";");
            for (String query : queries) {
                if (!query.trim().isEmpty()) {
                    stmt.executeUpdate(query);
                }
            }

            System.out.println("Databáze byla úspěšně vytvořena znovu!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
