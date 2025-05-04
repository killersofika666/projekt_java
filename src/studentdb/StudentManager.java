package studentdb;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentManager {
	private List<Student> students;
	private Map<Integer, Student> studentMap;
	
	public StudentManager() {
		this.students = new ArrayList<>();
		this.studentMap = new HashMap<>();
	}
	
	public void addStudent(Student student) {
		students.add(student);
		studentMap.put(student.getId(), student);
	}
	
	public boolean removeStudentById(int id) {
		Student toRemove = studentMap.remove(id);
		if (toRemove != null) {
			students.remove(toRemove);
			return true;
		}
		return false;
	}
	
	public Student findStudentById(int id) {
		return studentMap.get(id);
	}
	
	public List<Student> getStudentsSortedByLastName() {
		List<Student> sortedList = new ArrayList<>(students);
		sortedList.sort(Comparator.comparing(Student::getLastName));
		return sortedList;
	}
	
	public double averageGrageForAll() {
		if(students.isEmpty()) {
			return 0.0;
		}
		double totalSum = 0;
		int count = 0;
		for (Student student : students) {
			double avg = student.getAverageGrade();
			if (avg >= 0.0) {
				totalSum += avg;
				count++;
			}
		}
		return (count > 0) ? totalSum / count : 0.0;
	}
	
	public double averageGradeForSpecialization(String specialization) {
	    double sum = 0.0;
	    int count = 0;

	    for (Student student : students) {
	        if ((specialization.equalsIgnoreCase("telecom") && student instanceof TelecomStudent)
	            || (specialization.equalsIgnoreCase("cyber") && student instanceof CyberStudent)) {
	            for (int grade : student.getGrades()) {
	                sum += grade;
	                count++;
	            }
	        }
	    }

	    if (count == 0) {
	        return 0.0;
	    } else {
	        return sum / count;
	    }
	}

	
	public List<Student> getAllStudents() {
		return new ArrayList<>(students);
	}
	
	public void clearAllStudents() {
	    students.clear();
	}


}
