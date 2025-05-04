package studentdb;

public class CyberStudent extends Student {

    public CyberStudent(int id, String firstName, String lastName, int yearOfBirth) {
        super(id, firstName, lastName, yearOfBirth);
    }

    @Override
    public void showSkill() {
        String fullName = getFirstName() + getLastName();
        String hash = HashUtil.toSHA256(fullName);
        if (hash != null) {
            System.out.println("Hash jména a příjmení: " + hash);
        } else {
            System.out.println("Chyba při generování hashe.");
        }
    }
}
