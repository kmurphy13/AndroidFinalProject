package com.example.remileblanc.qrc_cs450_finalproject;

public class ScheduleSession {

    public String name;
    public String professor;
    public String mentor;
    public String course;
    public String date;
    public String time;


    public ScheduleSession(){

    }

    public ScheduleSession(String aMentor, String aFullName, String aCourse,String aProfessor,String aDate, String aTime){

        this.name = aFullName;
        this.mentor = aMentor;
        this.professor = aProfessor;
        this.course = aCourse;
        this.date = aDate;
        this.time = aTime;
    }


}
