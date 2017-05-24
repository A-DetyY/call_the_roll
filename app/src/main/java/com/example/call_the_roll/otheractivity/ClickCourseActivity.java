package com.example.call_the_roll.otheractivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.call_the_roll.R;
import com.example.call_the_roll.database.Course;
import com.example.call_the_roll.database.Menu;
import com.example.call_the_roll.database.MenuMember;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

public class ClickCourseActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView title;
    private TextView content;
    private Button back;
    private Button edit;
    private Button signup;

    private String titleText;
    private String ip;
    private int port;
    private String id;
    private String name;
    private String returnback;

    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_course);

        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        back = (Button) findViewById(R.id.back);
        edit = (Button) findViewById(R.id.edit);
        signup = (Button) findViewById(R.id.signup);
        back.setOnClickListener(this);
        edit.setOnClickListener(this);
        signup.setOnClickListener(this);

        Intent intent = getIntent();
        titleText = intent.getStringExtra("courseName");
        title.setText(titleText);
        ip = intent.getStringExtra("ip");
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.edit:
                showPopupMenu(edit);
                break;
            case R.id.signup:
                final  View view = LayoutInflater.from(ClickCourseActivity.this)
                        .inflate(R.layout.change,null);
                final EditText editText = (EditText) view.findViewById(R.id.known);
                editText.setHint("请输入端口号");
                final EditText editText2 = (EditText) view.findViewById(R.id.change);
                editText2.setHint("请输入IP地址");
                AlertDialog.Builder builder = new AlertDialog.Builder(ClickCourseActivity.this)
                        .setTitle("输入端口号")
                        .setIcon(R.mipmap.ic_launcher)
                        .setView(editText)
                        .setPositiveButton("确定",new AlertDialog.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface,int i){
                                port = Integer.parseInt(editText.getText().toString());
                                ip = editText2.getText().toString();
                                try {
                                     Thread set = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try{
                                                //port = 1111;
                                                //指定服务端的ip和端口号
                                                //socket = new Socket("172.29.35.28", 9527);
                                                socket = new Socket(ip,port);
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        content.append("服务器连接成功\n");
                                                        //content.append("数据包的内容为：" + id + " " + name + "\n");
                                                    }
                                                });
                                                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter
                                                        (socket.getOutputStream()));
                                                writer.write(id + " " + name + "\n");
                                                //writer.write("Time\n");
                                                writer.flush();
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        content.append("数据包发送成功\n");
                                                        content.append("数据包的内容为：" + id + " " + name + "\n");
                                                    }
                                                });

                                                Thread receive = new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            BufferedReader reader = new BufferedReader
                                                                    (new InputStreamReader(socket.getInputStream()));
                                                            String s = null;
                                                            int count = 0;
                                                            while (true) {
                                                                if (TextUtils.isEmpty(returnback = reader.readLine())) {
                                                                    Thread.sleep(500);
                                                                    count++;
                                                                    runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            content.append("返回信息未接收\n");
                                                                        }
                                                                    });
                                                                    if (count == 480) {
                                                                        break;
                                                                    }
                                                                }
                                                                else {
                                                                    runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            content.append("返回信息接收，信息为：" + returnback + "\n");
                                                                            content.append("签到成功！\n");
                                                                        }
                                                                    });
                                                                    //if (s.equals("OK")) {
                                                                    //content.append("签到成功！\n");
                                                                    //break;
                                                                    //}
                                                                    break;
                                                                }
                                                            }
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                                receive.start();
                                            }
                                            catch (IOException e){
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    //set.setPriority(10);
                                    set.start();
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("取消",null);
                builder.show();
                break;
            default:
        }
    }

    private void showPopupMenu(View view){
        //View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.getMenuInflater().inflate(R.menu.coursemenu,popupMenu.getMenu());

        //menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item){
                String s = item.getTitle().toString();
                switch (s){
                    case "修改课程名":
                        final EditText editText = new EditText(ClickCourseActivity.this);
                        AlertDialog.Builder builder = new AlertDialog.Builder(ClickCourseActivity.this)
                                .setTitle("修改名单名")
                                .setIcon(R.mipmap.ic_launcher)
                                .setView(editText)
                                .setPositiveButton("确定", new AlertDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //MenuMember member = new MenuMember();
                                        String content = editText.getText().toString();
                                        judgeSameName(content);
                                    }
                                })
                                .setNegativeButton("取消",null);
                        builder.show();
                        break;
                    case "修改老师IP":
                        final EditText editText2 = new EditText(ClickCourseActivity.this);
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ClickCourseActivity.this)
                                .setTitle("修改老师IP")
                                .setIcon(R.mipmap.ic_launcher)
                                .setView(editText2)
                                .setPositiveButton("确定", new AlertDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //MenuMember member = new MenuMember();
                                        String content = editText2.getText().toString();
                                        Course course = new Course();
                                        course.setTeacherIP(content);
                                        course.updateAll("courseName = ?",titleText);
                                    }
                                })
                                .setNegativeButton("取消",null);
                        builder2.show();
                        break;
                    case "删除课程":
                        DataSupport.deleteAll(Course.class,"courseName = ?",titleText);
                        finish();
                        break;
                    default:
                }
                return false;
            }
        });
        //menu的关闭事件
        popupMenu.setOnDismissListener(new android.support.v7.widget.PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(android.support.v7.widget.PopupMenu menu) {
                Toast.makeText(getApplicationContext(),"关闭EditMenu",Toast.LENGTH_SHORT).show();
            }
        });

        popupMenu.show();
    }

    //判断修改的课程名是否重名
    private void judgeSameName(String content){
        List<Course> courseList = DataSupport.findAll(Course.class);
        int i;
        for(i = 0;i < courseList.size();i++){
            if(courseList.get(i).getCourseName().equals(content)){
                AlertDialog.Builder builder = new AlertDialog.Builder(ClickCourseActivity.this)
                        .setTitle("提示")
                        .setMessage("在名单中已存在该课程名")
                        .setPositiveButton("确定",null);
                builder.show();
                break;
            }
        }
        if(i == courseList.size()){
            Course course = new Course();
            course.setCourseName(content);
            course.updateAll("courseName = ?",titleText);
            titleText = content;
            title.setText(titleText);
            //memberList = DataSupport.select("memberName","memberID","lateNum","belong")
            //        .where("belong = ?",titletext)
            //       .order("lateNum")
            //       .find(MenuMember.class);
            //memberList = menus.get(0).getMemberList();
            //CountAdapter adapter = new CountAdapter(ClickMenuActivity.this,R.layout.item3,memberList);
            //listView.setAdapter(adapter);
        }
    }
}
