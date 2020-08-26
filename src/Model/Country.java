package Model;

public class Country extends LoginManager {

    //Properties
    private int countryId;
    private String countryName;
    private String customerName;


    //Constructor for standard SQL statements
    public Country(int countryId, String countryName) {
        setCountryId(countryId);
        setCountryName(countryName);

    }

    //Setters
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    //Getters
    public int getCountryId() {
        return countryId;
    }
    public String getCountryName() {
        return countryName;
    }
    public String getCustomerName() {
        return customerName;
    }

}