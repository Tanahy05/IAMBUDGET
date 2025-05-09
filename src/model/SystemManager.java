package model;
import model.User;



public class SystemManager {
    private static User currentUser;

    public static void loginUser(User user) {
        currentUser = user;
        loadUserData(user.getUserID());
        System.out.println(currentUser.getUserID() + " " + currentUser.getUsername());
    }

    public static void logoutUser() {
        currentUser = null;
        System.out.println("Logged out successfully");

    }

    public static User getCurrentUser() {
        return currentUser;
    }

    private static void loadUserData(int uid) {
        // TODO: Load Budget, Expense, Income, Transaction, etc.
        // from files like "data/uid_budget.dat" etc.
        // TODO: Use your serialization logic here
    }

    public static void saveUserData() {
        if (currentUser == null) return;

        int uid = currentUser.getUserID();

        // TODO: Save Budget, Expense, Income, Transaction, etc.
        // to files like "data/uid_budget.dat" etc.
    }

    public static boolean isUserLoggedIn() {
        return currentUser != null;
    }
}
