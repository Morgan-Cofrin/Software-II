package Model;

public class Customer extends LoginManager {

    //Properties
    private int customerId;
    private int addressId;
    private int cityId;
    private int countryId;
    private String customerName;
    private short active;
    private String customerAddressLn1;
    private String customerAddressLn2;
    private String customerPostalCode;
    private String customerPhoneNum;
    private String customerCity;
    private String customerCountry;


    //Constructor
    public Customer(int customerId, int addressId, int cityId, int countryId, String customerName, int active, String customerAddressLn1, String customerAddressLn2,
                    String customerPostalCode, String customerPhoneNum, String customerCity, String customerCountry) {
        setCustomerId(customerId);
        setAddressId(addressId);
        setCityId(cityId);
        setCountryId(countryId);
        setCustomerName(customerName);
        setActive((short) active);
        setCustomerAddressLn1(customerAddressLn1);
        setCustomerAddressLn2(customerAddressLn2);
        setCustomerPostalCode(customerPostalCode);
        setCustomerPhoneNum(customerPhoneNum);
        setCustomerCity(customerCity);
        setCustomerCountry(customerCountry);
    }


    //Setters
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public void setActive(short active) {
        this.active = active;
    }
    public void setCustomerAddressLn1(String customerAddressLn1) {
        this.customerAddressLn1 = customerAddressLn1;
    }
    public void setCustomerAddressLn2(String customerAddressLn2) {
        this.customerAddressLn2 = customerAddressLn2;
    }
    public void setCustomerPostalCode(String customerPostalCode) {
        this.customerPostalCode = customerPostalCode;
    }
    public void setCustomerPhoneNum(String customerPhoneNum) {
        this.customerPhoneNum = customerPhoneNum;
    }
    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }
    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }

    //Getters
    public int getCustomerId() {
        return customerId;
    }
    public int getAddressId() {
        return addressId;
    }
    public int getCityId() {
        return cityId;
    }
    public int getCountryId() {
        return countryId;
    }
    public String getCustomerName() {
        return customerName;
    }
    public short getActive() {
        return active;
    }
    public String getCustomerAddressLn1() {
        return customerAddressLn1;
    }
    public String getCustomerAddressLn2() {
        return customerAddressLn2;
    }
    public String getCustomerPostalCode() {
        return customerPostalCode;
    }
    public String getCustomerPhoneNum() {
        return customerPhoneNum;
    }
    public String getCustomerCity() {
        return customerCity;
    }
    public String getCustomerCountry() {
        return customerCountry;
    }

}