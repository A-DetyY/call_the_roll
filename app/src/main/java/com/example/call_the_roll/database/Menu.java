package com.example.call_the_roll.database;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 廖智涌 on 2017/5/18.
 */

public class Menu extends DataSupport {

    private String menuName;

    private List<MenuMember> memberList = new ArrayList<MenuMember>();

    public String getMenuName(){
        return menuName;
    }

    public void setMenuName(String menuName){
        this.menuName = menuName;
    }

    public List<MenuMember> getMemberList(){
        return memberList;
    }

    public void setMemberList(List<MenuMember> memberList){
        this.memberList = memberList;
    }

}
