package model;

public class AddressUser {
    private int userId;
    private int addressId;
    private String address;
    private String name;
    private String email;
    private String phone;

    public AddressUser(int userId, int addressId, String address, String name, String email, String phone) {
        this.userId = userId;
        this.addressId = addressId;
        this.address = address;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public AddressUser() {
    }

    public AddressUser(int userId, int addressId, String address) {
        this.userId = userId;
        this.addressId = addressId;
        this.address = address;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
}
