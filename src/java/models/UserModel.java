package models;

/**
 *
 * @author ataulislam.raihan
 */
public class UserModel {
    private int pin;
    private String email;
    private String password;
    private String fullName;
    private UserType type;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }
}