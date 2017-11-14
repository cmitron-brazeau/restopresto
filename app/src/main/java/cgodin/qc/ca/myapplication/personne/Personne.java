package cgodin.qc.ca.myapplication.personne;

public class Personne {
    public int id;
    public String firstName;
    public String password;
    public String phone;
    public String email;
    public String address;
    public String city;
    public String country;

    public Personne(int id, String firstName, String password, String email) {
        this.id = id;
        this.firstName = firstName;
        this.password = password;
        this.email = email;
    }

    // Getters
    public String getDisplayName() {
        return firstName;
    }
}
