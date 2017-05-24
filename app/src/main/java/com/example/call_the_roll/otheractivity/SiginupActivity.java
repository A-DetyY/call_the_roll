package com.example.call_the_roll.otheractivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.call_the_roll.R;
import com.example.call_the_roll.adapter.CountAdapter;
import com.example.call_the_roll.database.MenuMember;
import com.example.call_the_roll.util.MyServer;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class SiginupActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private ListView listView2;
    private Button back;
    private TextView title;
    private TextView delay;
    private ProgressDialog progressDialog;

    private int port;
    private int time;
    private String titleText;

    private Timer timer = new Timer();

    private MyServer myServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siginup);

        showProgressDialog();

        listView = (ListView) findViewById(R.id.list);
        listView2 = (ListView) findViewById(R.id.list2);
        back = (Button) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        delay = (TextView) findViewById(R.id.delay);
        back.setOnClickListener(this);

        Intent intent = getIntent();
        port = Integer.parseInt(intent.getStringExtra("number"));
        titleText = intent.getStringExtra("menuName");
        title.setText(titleText);
        time = Integer.parseInt(intent.getStringExtra("time"));
        delay.setText(" " + time);

        //在android 4.0上运行时报android.os.NetworkOnMainThreadException异常，
        // 在4.0中，访问网络不能在主程序中进行
        try {
            myServer = new MyServer(port,titleText);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    myServer.service();
                }
            }).start();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        timer.schedule(task,1000,1000);             //timeTask
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    time--;
                    delay.setText(" " + time);
                    if(time < 0){
                        timer.cancel();
                        delay.setVisibility(View.GONE);
                        closeProgressDialog();
                        try {
                            myServer.close();
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                        addLate();
                        onResume();
                    }
                }
            });
        }
    };

    private void addLate(){
        int i;
        for(i = 0;i < MyServer.memberList.size();i++){
            MenuMember member = MyServer.memberList.get(i);
            MenuMember m = new MenuMember();
            int num = member.getLateNum();
            member.setLateNum(num + 1);
            m.setLateNum(num + 1);
            m.updateAll("memberID = ? and belong = ?",member.getMemberID(),member.getBelong());
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        CountAdapter adapter = new CountAdapter(SiginupActivity.this,R.layout.item3,
                MyServer.memberList);
        listView.setAdapter(adapter);
        CountAdapter adapter2 = new CountAdapter(SiginupActivity.this,R.layout.item3,
                MyServer.newcome);
        listView2.setAdapter(adapter2);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                final MenuMember member = MyServer.newcome.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(SiginupActivity.this)
                        .setTitle("提示")
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage("是否要将该学生添加进该名单中？")
                        .setPositiveButton("确认",new AlertDialog.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i){
                                MenuMember m = new MenuMember();
                                m.setMemberName(member.getMemberName());
                                m.setMemberID(member.getMemberID());
                                m.setLateNum(member.getLateNum());
                                m.setBelong(member.getBelong());
                                m.save();
                            }
                        })
                        .setNegativeButton("取消",null);
                builder.show();
            }
        });
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            default:
        }
    }

    //显示进度对话框
    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(SiginupActivity.this);
            progressDialog.setMessage("正在生成名单...");
            progressDialog.setCancelable(false);
        }
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    //关闭进度对话框
    private void closeProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }
}
