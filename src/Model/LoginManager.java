package Model;

import utils.DBConnection;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class LoginManager {

    //Properties
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;

    public LoginManager() {
        setCreateDate();
        setCreatedBy();
        setLastUpdate();
        setLastUpdateBy();

    }

    //Setters
    public void setCreateDate() {
        this.createDate = Timestamp.valueOf(LocalDateTime.now());
    }
    public void setCreatedBy() {
        this.createdBy = DBConnection.getUsername();
    }
    public void setLastUpdate() {
        this.lastUpdate = Timestamp.valueOf(LocalDateTime.now());
    }
    public void setLastUpdateBy() {
        this.lastUpdateBy = DBConnection.getUsername();
    }

    //Getters
    public Timestamp getCreateDate() {
        return createDate;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

}