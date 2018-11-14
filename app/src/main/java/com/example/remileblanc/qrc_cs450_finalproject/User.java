package com.example.remileblanc.qrc_cs450_finalproject;

public class User {


    public String email;
    public String firstName;
    public String lastName;
    public String userType;
    public String userID;

    public User(){

    }

    public User(String aUserID, String aEmail, String aFirstName, String aLastName, String aUserType){

        this.userID = aUserID;
        this.email = aEmail;
        this.firstName = aFirstName;
        this.lastName = aLastName;
        this.userType = aUserType;
    }



}
