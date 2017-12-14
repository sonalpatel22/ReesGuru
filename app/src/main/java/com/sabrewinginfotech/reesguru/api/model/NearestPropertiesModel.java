package com.sabrewinginfotech.reesguru.api.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Dashrath on 10/26/2017.
 */
/*{
"DISTANCE":1.1057327845731328,
"PropertyID":1,
"Title":"Nandini_DemoGroup",
"GroupID":1,
"PropertyImage":null,
"Longitude":72.820789600000010,
"Latitude":21.167661600000000,
"Location":"surat",
"UsdSalePrice":0.00,
"NoofBedrooms":1,
"NoofBathrooms":1,
"NoofKitchen":1,
"KeyLandmark":"dumas",
"LandArea":400.0,
"LandAreaUnit":"Sqfeet",
"BuiltArea":200.0,
"MonthlyRent":25000.0000,
"AgentId":8,
"UserId":10,
"Agent_FirstName":"NimishaVaghasiyaSucesss",
"Agent_LastName":"abc",
"Agent_EmailID":"nimishavaghasiya2@gmail.com",
"Agent_Cell":null,
"Agent_City":"surat",
"Agent_Address":"surat",
"Agent_CompnayAddress":null,
"Agent_CompnayName":null,
"Agent_PostalCode":"395004 ",
"Agent_Telephone":null,
"Agent_IsWhatsupEnable":true,
"Agent_Image":""
}*/


public class NearestPropertiesModel implements JsonModel {


    private Double DISTANCE;
    private int PropertyID;
    private String Title;
    private String PropertyImage;
    private String Longitude;
    private String Latitude;
    private String Location;
    private Double UsdSalePrice;
    private int NoofBedrooms;
    private int NoofBathrooms;
    private int NoofKitchen;

    private String KeyLandmark;
    private Double LandArea;
    private String LandAreaUnit;
    private Double BuiltArea;
    private Double MonthlyRent;
    private int UserId;

    private int AgentID;
    private String Agent_FirstName;
    private String Agent_LastName;
    private String Agent_EmailID;
    private String Agent_Cell;
    private String Agent_City;
    private String Agent_Address;
    private String Agent_CompnayAddress;
    private String Agent_CompnayName;
    private String Agent_PostalCode;
    private String Agent_Telephone;
    private boolean Agent_IsWhatsupEnable;
    private String Agent_Image;


    public Double getDISTANCE() {
        return DISTANCE;
    }

    public int getPropertyID() {
        return PropertyID;
    }

    public String getTitle() {
        return Title;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLocation() {
        return Location;
    }

    public Double getUsdSalePrice() {
        return UsdSalePrice;
    }

    public int getNoofBedrooms() {
        return NoofBedrooms;
    }

    public int getNoofBathrooms() {
        return NoofBathrooms;
    }

    public int getAgentID() {
        return AgentID;
    }

    public int getNoofKitchen() {
        return NoofKitchen;
    }

    public String getKeyLandmark() {
        return KeyLandmark;
    }

    public Double getLandArea() {
        return LandArea;
    }

    public String getLandAreaUnit() {
        return LandAreaUnit;
    }

    public Double getBuiltArea() {
        return BuiltArea;
    }

    public Double getMonthlyRent() {
        return MonthlyRent;
    }

    public int getUserId() {
        return UserId;
    }

    public String getAgent_FirstName() {
        return Agent_FirstName;
    }

    public String getAgent_LastName() {
        return Agent_LastName;
    }

    public String getAgent_EmailID() {
        return Agent_EmailID;
    }

    public String getAgent_Cell() {
        return Agent_Cell;
    }

    public String getAgent_City() {
        return Agent_City;
    }

    public String getAgent_Address() {
        return Agent_Address;
    }

    public String getAgent_CompnayAddress() {
        return Agent_CompnayAddress;
    }

    public String getAgent_CompnayName() {
        return Agent_CompnayName;
    }

    public String getAgent_PostalCode() {
        return Agent_PostalCode;
    }

    public String getAgent_Telephone() {
        return Agent_Telephone;
    }

    public boolean isAgent_IsWhatsupEnable() {
        return Agent_IsWhatsupEnable;
    }

    public String getAgent_Image() {
        return Agent_Image;
    }

    public String getPropertyImage() {
        return PropertyImage;
    }


    @Override
    public void fromJson(JSONObject object) {

        try {
            //this.DISTANCE = object.getDouble("DISTANCE");
            this.PropertyID = object.getInt("PropertyID");
            this.UserId = object.getInt("UserId");
            this.Title = object.getString("Title");
            this.PropertyImage = object.getString("PropertyImage");
            this.Longitude = object.getString("Longitude");
            this.Latitude = object.getString("Latitude");
            this.Location = object.getString("Location");
            this.UsdSalePrice = object.optDouble("UsdSalePrice", 0.00);
            this.NoofBedrooms = object.getInt("NoofBedrooms");
            this.NoofBathrooms = object.getInt("NoofBathrooms");
            this.NoofKitchen = object.getInt("NoofKitchen");

            this.KeyLandmark = object.getString("KeyLandmark");
            this.LandArea = object.optDouble("LandArea", 0.00);
            this.LandAreaUnit = object.getString("LandAreaUnit");
            this.BuiltArea = object.optDouble("BuiltArea", 0.00);
            this.MonthlyRent = object.optDouble("MonthlyRent", 0.00);

            this.AgentID = object.getInt("AgentId");


            if (object.has("Agent_FirstName")) {
                this.Agent_FirstName = object.getString("Agent_FirstName");
                this.Agent_LastName = object.getString("Agent_LastName");
                this.Agent_EmailID = object.getString("Agent_EmailID");
                this.Agent_Cell = object.getString("LandAreaUnit");
                this.Agent_City = object.getString("LandAreaUnit");
                this.Agent_Address = object.getString("LandAreaUnit");
                this.Agent_CompnayAddress = object.getString("LandAreaUnit");
                this.Agent_CompnayName = object.getString("LandAreaUnit");
                this.Agent_PostalCode = object.getString("LandAreaUnit");
                this.Agent_Telephone = object.getString("LandAreaUnit");
                this.Agent_IsWhatsupEnable = object.getBoolean("Agent_IsWhatsupEnable");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}


