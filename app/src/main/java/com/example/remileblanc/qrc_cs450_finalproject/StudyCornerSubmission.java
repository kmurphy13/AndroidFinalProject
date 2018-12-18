package com.example.remileblanc.qrc_cs450_finalproject;

public class StudyCornerSubmission {


    public String name;
    public String professor;
    public String course;
    public String assignment;
    public String time;
    public String date;
    public boolean shareWithOthers;

    public StudyCornerSubmission(){

    }

    public StudyCornerSubmission(String aName, String aProfessor, String aCourse, String anAssignment, String aTime, String aDate,boolean aShareWithOthers){

        this.name = aName;
        this.professor = aProfessor;
        this.course = aCourse;
        this.assignment = anAssignment;
        this.time = aTime;
        this.date = aDate;
        this.shareWithOthers = aShareWithOthers;
    }


}
