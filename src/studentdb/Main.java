package studentdb;

public class Main {
    public static void main(String[] args) {
        Database db = new Database();
        db.initializaceDatabase();

        Menu menu = new Menu(db);
        menu.start();
        
        System.out.println("Program ukončen.");
    }
}
