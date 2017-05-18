package com.example.call_the_roll.database;

import org.litepal.crud.DataSupport;

/**
 * Created by 廖智涌 on 2017/5/18.
 */

public class MenuMember extends DataSupport {

    private String memberName;

    private String memberID;

    public String getMemberName(){
        return memberName;
    }

    public void setMemberName(String memberName){
        this.memberName = memberName;
    }

    public String getMemberID(){
        return memberID;
    }

    public void setMemberID(String memberID){
        this.memberID = memberID;
    }

}
