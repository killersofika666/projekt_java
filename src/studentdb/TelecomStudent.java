package studentdb;

public class TelecomStudent extends Student{
	
	public TelecomStudent(int id, String firstName, String lastName, int yearOfBirth) {
		super(id, firstName, lastName, yearOfBirth);
	}
	
	@Override
	public void showSkill() {
		String fullName = getFirstName() + "" + getLastName();
		String morseCode = MorseUtil.toMorse(fullName.toUpperCase());
		System.out.println("Jmeno a prijmeni v Morseove abecede: " + morseCode);
	}

}
