package com.example.remileblanc.qrc_cs450_finalproject;

public class Assignment {
    public String professor;
    public String assignmentType;
    public String title;
    public String date;
    public String additionalInformation;

    public Assignment(){

    }

    public Assignment(String aProfessor, String anAssignmentType, String aTitle,String aDate, String aAdditionalInformation){

        this.professor = aProfessor;
        this.assignmentType = anAssignmentType;
        this.title = aTitle;
        this.date=aDate;
        this.additionalInformation=aAdditionalInformation;

    }


}
