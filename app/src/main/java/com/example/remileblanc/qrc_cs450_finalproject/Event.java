package com.example.remileblanc.qrc_cs450_finalproject;

public class Event {

    public String contactPerson;
    public String contactInfo;
    public String title;
    public String location;
    public String date;
    public String time;
    public String additionalInfo;

    public Event(){

    }

    public Event(String cp, String ci, String aTitle, String l, String d, String aTime, String ai){

        this.contactPerson = cp;
        this.contactInfo = ci;
        this.title = aTitle;
        this.location = l;
        this.date = d;
        this.time = aTime;
        this.additionalInfo = ai;
    }
}
