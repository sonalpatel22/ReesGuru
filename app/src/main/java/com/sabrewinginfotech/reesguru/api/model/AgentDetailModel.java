package com.sabrewinginfotech.reesguru.api.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alpesh on 12/2/2017.
 */

public class AgentDetailModel implements JsonModel {

    String C_ID;

    public String getCompnayName() {
        return CompnayName;
    }

    public void setCompnayName(String compnayName) {
        CompnayName = compnayName;
    }

    public String getC_FName() {
        return C_FName;
    }

    public void setC_FName(String c_FName) {
        C_FName = c_FName;
    }

    public String getC_LName() {
        return C_LName;
    }

    public void setC_LName(String c_LName) {
        C_LName = c_LName;
    }

    public String getRegisterNo() {
        return RegisterNo;
    }

    public void setRegisterNo(String registerNo) {
        RegisterNo = registerNo;
    }

    public String getRegisterAddress() {
        return RegisterAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        RegisterAddress = registerAddress;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getLandLine() {
        return LandLine;
    }

    public void setLandLine(String landLine) {
        LandLine = landLine;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    public String getAgentDetailID() {
        return AgentDetailID;
    }

    public void setAgentDetailID(String agentDetailID) {
        AgentDetailID = agentDetailID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getAgentID() {
        return AgentID;
    }

    public void setAgentID(String agentID) {
        AgentID = agentID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity1() {
        return City1;
    }

    public void setCity1(String city1) {
        City1 = city1;
    }

    public String getPostalCode1() {
        return PostalCode1;
    }

    public void setPostalCode1(String postalCode1) {
        PostalCode1 = postalCode1;
    }

    public String getEmailID1() {
        return EmailID1;
    }

    public void setEmailID1(String emailID1) {
        EmailID1 = emailID1;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getCell() {
        return Cell;
    }

    public void setCell(String cell) {
        Cell = cell;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserImage1() {
        return UserImage1;
    }

    public void setUserImage1(String userImage1) {
        UserImage1 = userImage1;
    }

    public String getC_ID1() {
        return C_ID1;
    }

    public void setC_ID1(String c_ID1) {
        C_ID1 = c_ID1;
    }

    public String getIsWhatsupEnable() {
        return IsWhatsupEnable;
    }

    public void setIsWhatsupEnable(String isWhatsupEnable) {
        IsWhatsupEnable = isWhatsupEnable;
    }

    String CompnayName;
    String C_FName;
    String C_LName;
    String RegisterNo;
    String RegisterAddress;
    String EmailID;
    String City;
    String State;
    String Mobile;
    String LandLine;
    String PostalCode;
    String UserImage;
    String AgentDetailID;
    String UserID;
    String AgentID;
    String FirstName;
    String LastName;
    String Address;
    String City1;
    String PostalCode1;
    String EmailID1;
    String Telephone;
    String Cell;
    String Password;
    String UserImage1;
    String C_ID1;
    String IsWhatsupEnable;

    @Override
    public void fromJson(JSONObject object) {
        try {
            this.setCompnayName(object.getString("CompnayName"));
            this.setC_FName(object.getString("C_FName"));
            this.setC_LName(object.getString("C_LName"));
            this.setRegisterNo(object.getString("RegisterNo"));
            this.setRegisterAddress(object.getString("RegisterAddress"));
            this.setEmailID(object.getString("EmailID"));
            this.setCity(object.getString("City"));
            this.setState(object.getString("State"));
            this.setMobile(object.getString("Mobile"));
            this.setLandLine(object.getString("LandLine"));
            this.setPostalCode(object.getString("PostalCode"));
            this.setUserImage(object.getString("UserImage"));
            this.setAgentDetailID(object.getString("AgentDetailID"));
            this.setUserID(object.getString("UserID"));
            this.setAgentID(object.getString("AgentID"));
            this.setFirstName(object.getString("FirstName"));
            this.setLastName(object.getString("LastName"));
            this.setAddress(object.getString("Address"));
            this.setCity1(object.getString("City1"));
            this.setPostalCode1(object.getString("PostalCode1"));
            this.setEmailID1(object.getString("EmailID1"));
            this.setTelephone(object.getString("Telephone"));
            this.setCell(object.getString("Cell"));
            this.setPassword(object.getString("Password"));
            this.setUserImage1(object.getString("UserImage1"));
            this.setC_ID1(object.getString("C_ID1"));
            this.setIsWhatsupEnable(object.getString("IsWhatsupEnable"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
