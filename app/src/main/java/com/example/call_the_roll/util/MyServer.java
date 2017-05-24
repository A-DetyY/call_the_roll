package com.example.call_the_roll.util;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.call_the_roll.database.MenuMember;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 廖智涌 on 2017/5/22.
 */

public class MyServer {

    private int port;

    private ServerSocket serverSocket;

    private String menuName;

    public static List<MenuMember> memberList = new ArrayList<MenuMember>();

    public static List<MenuMember> newcome = new ArrayList<MenuMember>();

    public MyServer(int number,String name) throws IOException {
        port = number;
        menuName = name;
        serverSocket = new ServerSocket(port);
        memberList = new ArrayList<MenuMember>();
        newcome = new ArrayList<MenuMember>();
        /*
        MenuMember member = new MenuMember();
        member.setMemberID("10000005");
        member.setMemberName("Harry");
        member.setLateNum(0);
        member.setBelong(menuName);
        newcome.add(member);
        */
        memberList = DataSupport.select("memberName","memberID","lateNum","belong")
                .where("belong = ?",menuName)
                .order("lateNum desc")
                .find(MenuMember.class);
        Toast.makeText(MyApplication.getContext(),"服务器启动",Toast.LENGTH_SHORT).show();
    }

    public void service(){
        while(true){
            Socket socket = null;
            try{
                socket = serverSocket.accept();
                Thread workThread = new Thread(new Handle(socket));
                workThread.start();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void close() throws IOException {
        serverSocket.close();
    }

}


class Handle implements Runnable {

    private Socket socket;

    public Handle(Socket socket){
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String line = reader.readLine();
            if (TextUtils.isEmpty(line)) {
                return;
            }
            String[] str = line.split(" ");
            int i;
            for (i = 0; i < MyServer.memberList.size(); i++) {
                String s = MyServer.memberList.get(i).getMemberID();
                if(s.equals(str[0])){
                    MyServer.memberList.remove(i);
                    writer.write("OK\n");
                    writer.flush();
                    break;
                }
            }
            if(i == MyServer.memberList.size()){
                MenuMember member = new MenuMember();
                member.setMemberID(str[0]);
                member.setMemberName(str[1]);
                member.setLateNum(0);
                member.setBelong(MyServer.memberList.get(0).getBelong());
                MyServer.newcome.add(member);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}

