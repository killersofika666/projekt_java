package studentdb;

import java.util.ArrayList;
import java.util.List;


public abstract class Student {
	private int id;
	private String firstName;
	private String lastName;
	private int yearOfBirth;
	private List<Integer> grades;
	
	public Student(int id, String firstName, String lastName, int yearOfBirth) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.yearOfBirth = yearOfBirth;
		this.grades = new ArrayList<>();
	}
	
	public void addGrade(int grade) {
		if (grade >= 1 && grade <= 5) {
			grades.add(grade);
		} else {
			System.out.println("Neplatna znamka! Platne znamky jsou 1 az 5.");
		}
	}
	
	public double getAverageGrade() {
		if (grades.isEmpty()) {
			return 0.0;
		}
		int sum = 0;
		for (int grade : grades) {
			sum += grade;
		}
		return (double) sum / grades.size();
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
	    this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public int getYearOfBirth() {
		return yearOfBirth;
	}

	public List<Integer> getGrades() {
		return new ArrayList<>(grades);
	}
	
	public abstract void showSkill();
	
	@Override
	public String toString() {
		return String.format("ID: %d | %s %s | Rok narozeni: %d | Prumer: %.2f", 
				id, firstName, lastName, yearOfBirth, getAverageGrade());
	}

}
