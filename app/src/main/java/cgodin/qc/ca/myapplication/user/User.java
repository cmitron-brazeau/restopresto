package cgodin.qc.ca.myapplication.user;


public class User {
    private int id;
    private String userName;
    private String password;
    private String email;
    private String facebookID;
    private String googleID;

    public User(int id, String userName, String password, String email, String facebookID, String googleID){
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.facebookID = facebookID;
        this.googleID = googleID;
    }

    public User(String userName, String password, String email, String facebookID, String googleID){
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.facebookID = facebookID;
        this.googleID = googleID;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public String getGoogleID() {
        return googleID;
    }
}
