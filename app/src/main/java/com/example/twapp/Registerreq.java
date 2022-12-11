package com.example.twapp;

public class Registerreq {

    private String contactPerson;//聯絡人
    private String email;
    private String password;
    private String contactNo;//電話
    private String relationship;//稱謂
    private Long deviceCode;

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
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

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }


    public void setDeviceCode(Long deviceCode) {

        this.deviceCode = deviceCode;
    }
}
