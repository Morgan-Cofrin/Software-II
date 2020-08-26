package Model;

public class User extends LoginManager {

    //Properties
    private int userId;
    private String userName;
    private String password;
    private short active;


    //Constructor
    public User(int userId, String userName, String password, int active) {
        setUserId(userId);
        setUserName(userName);
        setPassword(password);
        setActive((short) active);
    }


    //Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setActive(short active) {
        this.active = active;
    }


    //Getters
    public int getUserId() {
        return userId;
    }
    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }
    public short getActive() {
        return active;
    }

}