package com.example.call_the_roll.otheractivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.call_the_roll.R;
import com.example.call_the_roll.adapter.MemberAdapter;
import com.example.call_the_roll.database.Menu;
import com.example.call_the_roll.database.MenuMember;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CreateMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;

    private boolean isFinish = false;
    private boolean isSave = false;
    private String fileName;
    private String path;
    private String name;

    private Button back;
    private Button save;
    private TextView title;
    private ListView listView;

    private List<MenuMember> memberList = new ArrayList<MenuMember>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_menu);

        //showProgressDialog();
        back = (Button) findViewById(R.id.back);
        save = (Button) findViewById(R.id.save);
        back.setOnClickListener(this);
        save.setOnClickListener(this);

        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        if(ContextCompat.checkSelfPermission(CreateMenuActivity.this, Manifest.
                permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CreateMenuActivity.this,new String[]
                    { Manifest.permission.READ_EXTERNAL_STORAGE },1);
        }
        else {
            try {
                CreateList(path);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(CreateMenuActivity.this, "Exception", Toast.LENGTH_SHORT).show();
            }
        }
        closeProgressDialog();
        title = (TextView) findViewById(R.id.title);
        int position = fileName.indexOf(".");
        name = fileName.substring(0,position);
        title.setText(fileName);
        MemberAdapter adapter = new MemberAdapter(CreateMenuActivity.this,R.layout.item,memberList);
        listView = (ListView) findViewById(R.id.menuMemberList);
        listView.setAdapter(adapter);
    }

    private void CreateList(String path) throws IOException{
        File file = new File(path);
        fileName = file.getName();
        Log.d("CreateMenuActivity",fileName);
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String s = null;
        int i = 0;
        while((s = reader.readLine()) != null){
            i++;
            String str[] = s.split(" ");
            MenuMember member = new MenuMember();
            member.setMemberID(str[0]);
            member.setMemberName(str[1]);
            member.setToDefault("lateNum");
            member.updateAll();
            //member.setBelong();
            memberList.add(member);
        }
        fr.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    try {
                        CreateList(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(CreateMenuActivity.this, "Exception", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(CreateMenuActivity.this,"You denied the permission",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.back:
                if(isFinish){
                    finish();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateMenuActivity.this)
                            .setTitle("提示")
                            .setIcon(R.mipmap.ic_launcher)
                            .setMessage("名单还未保存确定退出吗？")
                            .setPositiveButton("确定",new AlertDialog.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface,int i){
                                    finish();
                                }
                            })
                            .setNegativeButton("取消",null);
                    builder.show();
                }
                break;
            case R.id.save:
                if(!isSave) {
                    List<Menu> menuList = DataSupport.findAll(Menu.class);
                    int i;
                    for (i = 0;i < menuList.size(); i++) {
                        if (menuList.get(i).getMenuName().equals(name)) {
                            final EditText editText = new EditText(CreateMenuActivity.this);
                            final AlertDialog.Builder builder = new AlertDialog.Builder(CreateMenuActivity.this)
                                    .setTitle("已有同名名单，请修改名单名：")
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setView(editText);
                            builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    name = editText.getText().toString();
                                    save.callOnClick();
                                }
                            })
                                    .setNegativeButton("取消", null);
                            builder.show();
                            break;
                        }
                    }
                    if(i == menuList.size()) {
                        Menu menu = new Menu();
                        menu.setMenuName(name);
                        menu.save();
                        for(int j = 0;j < memberList.size();j++){
                            memberList.get(j).setBelong(name);
                            memberList.get(j).save();
                        }
                        Toast.makeText(CreateMenuActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        isFinish = true;
                        isSave = true;
                    }
                }
                else{
                    Toast.makeText(CreateMenuActivity.this,"该名单已经保存",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    //显示进度对话框
    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(CreateMenuActivity.this);
            progressDialog.setMessage("正在生成名单...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    //关闭进度对话框
    private void closeProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

}
