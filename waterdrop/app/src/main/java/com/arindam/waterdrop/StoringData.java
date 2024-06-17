package com.arindam.waterdrop;

public class StoringData {
    String fullName;
    String id;
    String password;
    String role;
    String address;
    String phone;

    public StoringData() {

    }

    public StoringData(String fullName, String id, String phone, String address, String password, String role) {
        this.fullName = fullName;
        this.id = id;
        this.password = password;
        this.address= address;
        this.phone = phone;
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public String getId() {
        return id;
    }

    public String getPhone(){ return phone;}

    public String getAddress() { return address; }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
