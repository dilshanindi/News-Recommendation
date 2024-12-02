package models;

public class Manager {

    private String loggedInUserNIC;
    private String loggedInUserEmail;
    private String loggedInUserType; // Admin or User
    private String loggedInUserName;
    private int loggedInUserID;

    private static Manager instance;

    // Private constructor to prevent instantiation from other classes
    private Manager() { }

    // Singleton pattern to ensure a single instance of SessionManager
    public static Manager getInstance() {
        if (instance == null) {
            instance = new Manager();
        }
        return instance;
    }

    // Getters and Setters
    public int getLoggedInUserID() {
        return loggedInUserID;
    }

    public void setLoggedInUserID(int userID) {
        this.loggedInUserID = userID;
    }

    public String getLoggedInUserNIC() {
        return loggedInUserNIC;
    }

    public void setLoggedInUserNIC(String userNIC) {
        this.loggedInUserNIC = userNIC;
    }

    public String getLoggedInUserEmail() {
        return loggedInUserEmail;
    }

    public void setLoggedInUserEmail(String email) {
        this.loggedInUserEmail = email;
    }

    public String getLoggedInUserName() {
        return loggedInUserName;
    }

    public void setLoggedInUserName(String name) {
        this.loggedInUserName = name;
    }

    public String getLoggedInUserType() {
        return loggedInUserType;
    }

    public void setLoggedInUserType(String type) {
        this.loggedInUserType = type; // Store "admin" or "user"
    }

    // Utility method to fetch User ID directly from NIC (assuming NIC is numeric)
    public int getUserIdFromNIC() {
        try {
            return Integer.parseInt(loggedInUserNIC); // Ensure NIC is numeric
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("NIC should be numeric");
        }
    }

    // Clear the session details
    public void clearSession() {
        this.loggedInUserNIC = null;
        this.loggedInUserEmail = null;
        this.loggedInUserName = null;
        this.loggedInUserType = null;
        this.loggedInUserID = 0;
    }
}
