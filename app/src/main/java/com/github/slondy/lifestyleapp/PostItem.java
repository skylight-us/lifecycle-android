package com.github.slondy.lifestyleapp;

public class PostItem {
    static final int MON = 0;
    static final int TUE = 1;
    static final int WED = 2;
    static final int THU = 3;
    static final int FRI = 4;
    static final int SAT = 5;
    static final int SUN = 6;

//    @SerializedName("idx")
//    private int idx;
//    @SerializedName("classTitle")
//    private String classTitle;
//    @SerializedName("classPlace")
//    private String classPlace;
//    @SerializedName("ProfessorName")
//    private String professorName;
//    @SerializedName("day")
//    private int day;
//    @SerializedName("startTime")
//    private String startTime;
//    @SerializedName("endTime")
//    private String endTime;


    String classTitle="";
    String classPlace="";
    String professorName="";
    private int day = 0;
    private String startTime;
    private String endTime;
    private  int idx = 0;


    public int getIdx(){
        return idx;
    }

    public int setIdx(int i){
        return idx;
    }


    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
    }

    public String getClassPlace() {
        return classPlace;
    }

    public void setClassPlace(String classPlace) {
        this.classPlace = classPlace;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


}
