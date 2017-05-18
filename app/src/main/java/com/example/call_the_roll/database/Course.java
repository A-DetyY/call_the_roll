package com.example.call_the_roll.database;

import org.litepal.crud.DataSupport;

/**
 * Created by 廖智涌 on 2017/5/18.
 */

public class Course extends DataSupport {

    private String courseName;

    private String teacherIP;

    public String getCourseName(){
        return courseName;
    }

    public void setCourseName(String courseName){
        this.courseName = courseName;
    }

    public String getTeacherIP(){
        return teacherIP;
    }

    public void setTeacherIP(String teacherIP){
        this.teacherIP = teacherIP;
    }

}
