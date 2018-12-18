package com.example.remileblanc.qrc_cs450_finalproject;

public class CheckOutItem {

    public String item;
    public String additionalInfo;
    public String ID;

    public CheckOutItem(){

    }

    public CheckOutItem(String anItem, String anAdditionalInfo, String anID){
        this.item =anItem;
        this.additionalInfo = anAdditionalInfo;
        this.ID = anID;
    }


}
