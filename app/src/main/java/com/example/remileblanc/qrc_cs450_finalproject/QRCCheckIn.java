package com.example.remileblanc.qrc_cs450_finalproject;

public class QRCCheckIn {

    public String name;
    public String professor;
    public String course;
    public String objective;


    public QRCCheckIn(){

    }

    public QRCCheckIn(String aName, String aProfessor, String aCourse, String anObjective){

        this.name = aName;
        this.professor = aProfessor;
        this.course = aCourse;
        this.objective = anObjective;
    }


}
