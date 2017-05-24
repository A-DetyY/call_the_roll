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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.call_the_roll.R;
import com.example.call_the_roll.adapter.CountAdapter;
import com.example.call_the_roll.adapter.MemberAdapter;
import com.example.call_the_roll.adapter.MenuAdapter;
import com.example.call_the_roll.database.Menu;
import com.example.call_the_roll.database.MenuMember;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ClickMenuActivity extends AppCompatActivity implements View.OnClickListener {

    public final static int NAME = 1;
    public final static int ID = 2;
    public final static int LATE = 3;
    public final static int CALL_UP_BACK = 4;

    private Menu menu;
    private List<MenuMember> memberList = new ArrayList<MenuMember>();
    private String titletext;

    private boolean isFinish = false;

    private Button back;
    private Button edit;
    private Button signup;
    //private Button statistics;
    private TextView title;
    private ListView listView;

    //private String[] data = {"Apple","Banana","Orange"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_menu);

        initView();

        Intent intent = getIntent();
        titletext = intent.getStringExtra("menuName");
        title.setText(titletext);
        memberList = DataSupport.select("memberName","memberID","lateNum","belong")
                .where("belong = ?",titletext)
                .order("lateNum desc")
                .find(MenuMember.class);
        //memberList = menus.get(0).getMemberList();
        CountAdapter adapter = new CountAdapter(ClickMenuActivity.this,R.layout.item3,memberList);
        //哇，很难受，在这里耗费了很多时间，才发现在TextView的setText中不能直接放整数得转化成字符串
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(ClickMenuActivity.this,
                //android.R.layout.simple_list_item_1,data);
        //MemberAdapter adapter = new MemberAdapter(ClickMenuActivity.this,R.layout.item,memberList);
        listView.setAdapter(adapter);
    }

    private void initView(){
        back = (Button) findViewById(R.id.back);
        edit = (Button) findViewById(R.id.edit);
        signup = (Button) findViewById(R.id.signup);
        //statistics = (Button) findViewById(R.id.statistics);
        title = (TextView) findViewById(R.id.title);
        listView = (ListView) findViewById(R.id.listview);

        back.setOnClickListener(this);
        edit.setOnClickListener(this);
        signup.setOnClickListener(this);
        //statistics.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.edit:
                showPopupMenu(edit);
                break;
            case R.id.signup:
                final View view = LayoutInflater.from(ClickMenuActivity.this)
                    .inflate(R.layout.change,null);
                final EditText editText1 = (EditText) view.findViewById(R.id.known);
                editText1.setHint("请输入端口号");
                final EditText editText2 = (EditText) view.findViewById(R.id.change);
                editText2.setHint("请输入点名时间");
                AlertDialog.Builder builder = new AlertDialog.Builder(ClickMenuActivity.this)
                        .setTitle("创建端口号")
                        .setIcon(R.mipmap.ic_launcher)
                        .setView(view)
                        .setPositiveButton("确定",new AlertDialog.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface,int i){
                                String number = editText1.getText().toString();
                                String time = editText2.getText().toString();
                                Intent intent = new Intent(ClickMenuActivity.this,SiginupActivity.class);
                                intent.putExtra("menuName",titletext);
                                intent.putExtra("number",number);
                                intent.putExtra("time",time);
                                startActivityForResult(intent,CALL_UP_BACK);
                            }
                        })
                        .setNegativeButton("取消",null);
                builder.show();
                break;
            default:
                break;
        }
    }

    private void showPopupMenu(View view){
        //View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.getMenuInflater().inflate(R.menu.editmenu,popupMenu.getMenu());

        //menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item){
                String s = item.getTitle().toString();
                switch (s){
                    case "修改名单名":
                        final EditText editText = new EditText(ClickMenuActivity.this);
                        AlertDialog.Builder builder = new AlertDialog.Builder(ClickMenuActivity.this)
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
                    case "修改姓名":
                        final View view = LayoutInflater.from(ClickMenuActivity.this)
                                .inflate(R.layout.change,null);
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ClickMenuActivity.this)
                                .setTitle("修改姓名")
                                .setIcon(R.mipmap.ic_launcher)
                                .setView(view)
                                .setPositiveButton("确定", new AlertDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        EditText known = (EditText) view.findViewById(R.id.known);
                                        EditText change = (EditText) view.findViewById(R.id.change);
                                        String s1 = known.getText().toString();
                                        String s2 = change.getText().toString();
                                        if(!TextUtils.isEmpty(s1) && !TextUtils.isEmpty(s2)) {
                                            change(s1, s2, NAME);
                                        }
                                        else{
                                            Toast.makeText(ClickMenuActivity.this,"输入了空内容",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton("取消",null);
                        builder2.show();
                        break;
                    case "修改ID":
                        final View view3 = LayoutInflater.from(ClickMenuActivity.this)
                                .inflate(R.layout.change,null);
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(ClickMenuActivity.this)
                                .setTitle("修改ID")
                                .setIcon(R.mipmap.ic_launcher)
                                .setView(view3)
                                .setPositiveButton("确定", new AlertDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        EditText known = (EditText) view3.findViewById(R.id.known);
                                        EditText change = (EditText) view3.findViewById(R.id.change);
                                        String s1 = known.getText().toString();
                                        String s2 = change.getText().toString();
                                        if(!TextUtils.isEmpty(s1) && !TextUtils.isEmpty(s2)) {
                                            change(s1, s2, ID);
                                        }
                                        else{
                                            Toast.makeText(ClickMenuActivity.this,"输入了空内容",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton("取消",null);
                        builder3.show();
                        break;
                    case "修改未签情况":
                        final View view4 = LayoutInflater.from(ClickMenuActivity.this)
                                .inflate(R.layout.change,null);
                        AlertDialog.Builder builder4 = new AlertDialog.Builder(ClickMenuActivity.this)
                                .setTitle("修改未签情况")
                                .setIcon(R.mipmap.ic_launcher)
                                .setView(view4)
                                .setPositiveButton("确定", new AlertDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        EditText known = (EditText) view4.findViewById(R.id.known);
                                        EditText change = (EditText) view4.findViewById(R.id.change);
                                        String s1 = known.getText().toString();
                                        String s2 = change.getText().toString();
                                        if(!TextUtils.isEmpty(s1) && !TextUtils.isEmpty(s2)) {
                                            change(s1, s2, LATE);
                                        }
                                        else{
                                            Toast.makeText(ClickMenuActivity.this,"输入了空内容",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton("取消",null);
                        builder4.show();
                        break;
                    case "添加名单数据":
                        final View view5 = LayoutInflater.from(ClickMenuActivity.this)
                                .inflate(R.layout.add,null);
                        AlertDialog.Builder builder5 = new AlertDialog.Builder(ClickMenuActivity.this)
                                .setTitle("修改ID")
                                .setIcon(R.mipmap.ic_launcher)
                                .setView(view5)
                                .setPositiveButton("确定", new AlertDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        EditText name = (EditText) view5.findViewById(R.id.name_add);
                                        EditText id = (EditText) view5.findViewById(R.id.id_add);
                                        EditText late = (EditText) view5.findViewById(R.id.late_add);
                                        String s1 = name.getText().toString();
                                        String s2 = id.getText().toString();
                                        String s3 = late.getText().toString();
                                        if(!TextUtils.isEmpty(s1) && !TextUtils.isEmpty(s2)
                                                && !TextUtils.isEmpty(s3)) {
                                            MenuMember member = new MenuMember();
                                            member.setMemberName(s1);
                                            member.setMemberID(s2);
                                            member.setLateNum(Integer.parseInt(s3));
                                            member.setBelong(titletext);
                                            member.save();
                                            onResume();
                                        }
                                        else{
                                            Toast.makeText(ClickMenuActivity.this,"输入了空内容",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton("取消",null);
                        builder5.show();
                        break;
                    case "删除名单数据":
                        final View view6 = LayoutInflater.from(ClickMenuActivity.this)
                                .inflate(R.layout.change,null);
                        AlertDialog.Builder builder6 = new AlertDialog.Builder(ClickMenuActivity.this)
                                .setTitle("修改未签情况")
                                .setIcon(R.mipmap.ic_launcher)
                                .setView(view6)
                                .setPositiveButton("确定", new AlertDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        EditText known = (EditText) view6.findViewById(R.id.known);
                                        EditText change = (EditText) view6.findViewById(R.id.change);
                                        String s1 = known.getText().toString();
                                        String s2 = change.getText().toString();
                                        if(!TextUtils.isEmpty(s1) && !TextUtils.isEmpty(s2)) {
                                            DataSupport.deleteAll(MenuMember.class,"memberID = ?",s1);
                                            onResume();
                                        }
                                        else{
                                            Toast.makeText(ClickMenuActivity.this,"输入了空内容",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton("取消",null);
                        builder6.show();
                        break;
                    case "删除名单":
                        DataSupport.deleteAll(MenuMember.class,"belong = ?",titletext);
                        DataSupport.deleteAll(Menu.class,"menuName = ?",titletext);
                        finish();
                        break;
                    default:
                        break;
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

    //判断修改的名单名是否重名
    private void judgeSameName(String content){
        List<Menu> menuList = DataSupport.findAll(Menu.class);
        int i;
        for(i = 0;i < menuList.size();i++){
            if(menuList.get(i).getMenuName().equals(content)){
                AlertDialog.Builder builder = new AlertDialog.Builder(ClickMenuActivity.this)
                        .setTitle("提示")
                        .setMessage("在名单中已存在该名单名")
                        .setPositiveButton("确定",null);
                builder.show();
                break;
            }
        }
        if(i == menuList.size()){
            Menu menu = new Menu();
            menu.setMenuName(content);
            menu.updateAll("menuName = ?",titletext);
            MenuMember member = new MenuMember();
            member.setBelong(content);
            member.updateAll("belong = ?",titletext);
            titletext = content;
            title.setText(titletext);
            //memberList = DataSupport.select("memberName","memberID","lateNum","belong")
            //        .where("belong = ?",titletext)
             //       .order("lateNum")
             //       .find(MenuMember.class);
            //memberList = menus.get(0).getMemberList();
            //CountAdapter adapter = new CountAdapter(ClickMenuActivity.this,R.layout.item3,memberList);
            //listView.setAdapter(adapter);
        }
    }

    private boolean judgeSameID(String content){
        List<MenuMember> members = DataSupport.select("memberID","belong")
                                            .where("belong = ?",titletext)
                                            .find(MenuMember.class);
        int i;
        for(i = 0;i < members.size();i++){
            if(members.get(i).getMemberID().equals(content)){
                AlertDialog.Builder builder = new AlertDialog.Builder(ClickMenuActivity.this)
                        .setTitle("提示")
                        .setMessage("在该名单中已存在该ID")
                        .setPositiveButton("确定",null);
                builder.show();
                break;
            }
        }
        if(i == members.size()){
            return false;
        }
        else
            return true;
    }

    //修改......
    private void change(String known,String change,int sign){
        switch (sign){
            case NAME:
                MenuMember member = new MenuMember();
                member.setMemberName(change);
                member.updateAll("memberID = ?",known);
                onResume();
                break;
            case ID:
                //这里好像还要考虑到修改的ID是否在名单中已存在，虽然说是修改者自己做的死
                //          可是我们还是要把它考虑进去啊
                boolean b = judgeSameID(change);
                if(!b) {
                    MenuMember member2 = new MenuMember();
                    member2.setMemberID(change);
                    member2.updateAll("memberName = ?", known);
                    onResume();
                }
                break;
            case LATE:
                MenuMember member3 = new MenuMember();
                member3.setLateNum(Integer.parseInt(change));
                member3.updateAll("memberID = ?",known);
                onResume();
                break;
            default:
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        memberList = DataSupport.select("memberName","memberID","lateNum","belong")
                .where("belong = ?",titletext)
                .order("lateNum desc")
                .find(MenuMember.class);
        //memberList = menus.get(0).getMemberList();
        CountAdapter adapter = new CountAdapter(ClickMenuActivity.this,R.layout.item3,memberList);
        listView.setAdapter(adapter);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CALL_UP_BACK:
                memberList = DataSupport.select("memberName","memberID","lateNum","belong")
                        .where("belong = ?",titletext)
                        .order("lateNum desc")
                        .find(MenuMember.class);
                //memberList = menus.get(0).getMemberList();
                CountAdapter adapter = new CountAdapter(ClickMenuActivity.this,R.layout.item3,memberList);
                listView.setAdapter(adapter);
                break;
            default:
        }
    }
}
