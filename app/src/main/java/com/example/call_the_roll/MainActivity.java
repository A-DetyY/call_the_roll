package com.example.call_the_roll;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.call_the_roll.adapter.CourseAdapter;
import com.example.call_the_roll.adapter.MenuAdapter;
import com.example.call_the_roll.database.Course;
import com.example.call_the_roll.database.Menu;
import com.example.call_the_roll.otheractivity.ClickCourseActivity;
import com.example.call_the_roll.otheractivity.ClickMenuActivity;
import com.example.call_the_roll.otheractivity.CreateMenuActivity;
import com.example.call_the_roll.otheractivity.SiginupActivity;
import com.example.call_the_roll.util.FileUtils;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public final static int FILE_SELECT_CODE = 1;
    public final static int CREATE_MENU_BACK = 2;
    public final static int CLICK_MENU_BACK = 3;
    public final static int CLICK_COURCE_BACK = 4;

    private ViewPager mViewPager;
    private PagerAdapter mAdapter;
    private List<View> mViews = new ArrayList<View>();

    private LinearLayout mTabCallup;
    private LinearLayout mTabSignup;
    private LinearLayout mTabMine;

    private ImageButton mCallupImg;
    private ImageButton mSignupImg;
    private ImageButton mMineImg;
    private ImageButton mAdd_Img;

    private TextView id;
    private TextView name;

    private List<Menu> menus;
    private List<Course> courses;

    //private String[] data1 = {"Apple","Banana","Orange"};
   // private String[] data2 = {"Pear","Mango","Pear"};
    private String idtext;
    private String nametext;

    private ListView menuList;
    private ListView courceList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();

        initEvents();

        //String str = "界面侧边ABc字母检索";
        //for(char c:str.toCharArray()){
            //Log.d("MainActivity", Pinyin.toPinyin(c));
        //}

    }

    private void initView()
    {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        // tabs
        mTabCallup = (LinearLayout) findViewById(R.id.id_tab_callup);
        mTabSignup = (LinearLayout) findViewById(R.id.id_tab_signup);
        mTabMine = (LinearLayout) findViewById(R.id.id_tab_mine);

        // ImageButton
        mCallupImg = (ImageButton) findViewById(R.id.id_tab_callup_img);
        mSignupImg = (ImageButton) findViewById(R.id.id_tab_signup_img);
        mMineImg = (ImageButton) findViewById(R.id.id_tab_mine_img);
        mAdd_Img = (ImageButton) findViewById(R.id.Tab_add);

        //实例化一个LayoutInflater对象
        LayoutInflater mInflater = LayoutInflater.from(this);
        View tab01 = mInflater.inflate(R.layout.tab01, null);
        View tab02 = mInflater.inflate(R.layout.tab02, null);
        View tab03 = mInflater.inflate(R.layout.tab03, null);

        mViews.add(tab01);
        mViews.add(tab02);
        mViews.add(tab03);

        //id = (TextView) findViewById(R.id.ID);
        //name = (TextView) findViewById(R.id.Name);
        Intent intent = getIntent();
        idtext = intent.getStringExtra("ID");
        nametext = intent.getStringExtra("Name");
        //id.setText(idtext);
        //name.setText(nametext);

        /*调试的时候发现在tab01，02，03布局里面的东西都为null，即在main布局里读取不到
        id = (TextView) tab03.findViewById(R.id.idtext);
        name = (TextView) tab03.findViewById(R.id.nametext);
        Intent intent = getIntent();
        String idtext = intent.getStringExtra("ID");
        String nametext = intent.getStringExtra("Name");
        id.setText(idtext);
        name.setText(nametext);
        */

        //mTab01_add = (ImageButton) findViewById(R.id.Tab01_add);
        //mTab02_add = (ImageButton) findViewById(R.id.Tab02_add);

        mAdapter = new PagerAdapter()
        {
            //这个方法从viewPager中移动当前的view
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object)
            {
                container.removeView(mViews.get(position));
            }

            //这个方法返回一个对象，该对象表明PagerAapter选择哪个对象放在当前的ViewPager中
            @Override
            public Object instantiateItem(ViewGroup container, int position)
            {
                View view = mViews.get(position);
                container.addView(view);
                if(position == 0) {
                    getMenus(view);
                    menuList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                            String menuName = menus.get(position).getMenuName();
                            Toast.makeText(MainActivity.this,menuName,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,ClickMenuActivity.class);
                            intent.putExtra("menuName",menuName);
                            startActivityForResult(intent,CLICK_MENU_BACK);
                        }
                    });
                }
                else if(position == 1){
                    getCourses(view);
                    courceList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                            String courceName = courses.get(position).getCourseName();
                            String ip = courses.get(position).getTeacherIP();
                            Toast.makeText(MainActivity.this,courceName,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, ClickCourseActivity.class);
                            intent.putExtra("courseName",courceName);
                            intent.putExtra("ip",ip);
                            intent.putExtra("name",nametext);
                            intent.putExtra("id",idtext);
                            startActivityForResult(intent,CLICK_COURCE_BACK);
                        }
                    });
                }
                else if(position == 2){
                    id = (TextView) view.findViewById(R.id.idtext);
                    name = (TextView) view.findViewById(R.id.nametext);
                    id.setText(idtext);
                    name.setText(nametext);
                }
                return view;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1)
            {
                return arg0 == arg1;
            }

            @Override
            public int getCount()
            {
                return mViews.size();
            }
        };

        mViewPager.setAdapter(mAdapter);

    }

    private void initEvents()
    {
        mTabCallup.setOnClickListener(this);
        mTabSignup.setOnClickListener(this);
        mTabMine.setOnClickListener(this);
        mAdd_Img.setOnClickListener(this);

        mViewPager.setCurrentItem(0);
        mCallupImg.setImageResource(R.drawable.menu_click);

        LitePal.getDatabase();
        Toast.makeText(MainActivity.this,"数据库创建成功",Toast.LENGTH_SHORT).show();

        mViewPager.addOnPageChangeListener(new OnPageChangeListener()
        {

            @Override
            public void onPageSelected(int arg0)
            {
                int currentItem = mViewPager.getCurrentItem();
                resetImg();
                switch (currentItem)
                {
                    case 0:
                        mCallupImg.setImageResource(R.drawable.menu_click);
                        break;
                    case 1:
                        mSignupImg.setImageResource(R.drawable.hand_click);
                        break;
                    case 2:
                        mMineImg.setImageResource(R.drawable.mine_click);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {

            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {

            }
        });
    }

    @Override
    public void onClick(View v)
    {
        resetImg();
        switch (v.getId())
        {
            case R.id.id_tab_callup:
                mViewPager.setCurrentItem(0);
                mCallupImg.setImageResource(R.drawable.menu_click);
                break;
            case R.id.id_tab_signup:
                mViewPager.setCurrentItem(1);
                mSignupImg.setImageResource(R.drawable.hand_click);
                break;
            case R.id.id_tab_mine:
                mViewPager.setCurrentItem(2);
                mMineImg.setImageResource(R.drawable.mine_click);
                break;
            case R.id.Tab_add:
                int currentItem = mViewPager.getCurrentItem();
                if(currentItem == 0) {
                    showPopupMenu(mAdd_Img);
                    Toast.makeText(MainActivity.this, "showPopupMenu", Toast.LENGTH_SHORT).show();
                }
                else if(currentItem == 1){
                    Toast.makeText(MainActivity.this,"新建课程",Toast.LENGTH_SHORT).show();
                    final View view = LayoutInflater.from(MainActivity.this)
                            .inflate(R.layout.change,null);
                    final EditText editText1 = (EditText) view.findViewById(R.id.known);
                    editText1.setHint("请输入课程名");
                    final EditText editText2 = (EditText) view.findViewById(R.id.change);
                    editText2.setHint("请输入老师IP");
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("新建课程")
                            .setIcon(R.mipmap.ic_launcher)
                            .setView(view)
                            .setPositiveButton("确定",new AlertDialog.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i){
                                    String name = editText1.getText().toString();
                                    String ip = editText2.getText().toString();
                                    Course course = new Course();
                                    course.setCourseName(name);
                                    course.setTeacherIP(ip);
                                    course.save();
                                    View view = mViews.get(1);
                                    getCourses(view);
                                }
                            })
                            .setNegativeButton("取消",null);
                    builder.show();
                    break;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 将所有的图片切换为暗色的
     */
    private void resetImg()
    {
        mCallupImg.setImageResource(R.drawable.menu);
        mSignupImg.setImageResource(R.drawable.hand);
        mMineImg.setImageResource(R.drawable.mine);
    }

    private void showPopupMenu(View view){
        //View当前PopupMenu显示的相对View的位置
        android.support.v7.widget.PopupMenu popupMenu = new android.support.v7.widget.PopupMenu(this,view);
        popupMenu.getMenuInflater().inflate(R.menu.popupmenu,popupMenu.getMenu());

        //menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new android.support.v7.widget.PopupMenu.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item){
                String title = item.getTitle().toString();
                switch (title) {
                    case "文件输入":
                        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                                permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]
                                    { Manifest.permission.READ_EXTERNAL_STORAGE },1);
                        }
                        else {
                            showFileChooser();
                        }
                        Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
                        break;
                    case "手动输入":
                        Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_SHORT).show();
                        break;
                    case "点名建立":
                        Toast.makeText(getApplicationContext(),"3",Toast.LENGTH_SHORT).show();
                        break;
                    case "获取本机IP":
                        try {
                            String ip = "";
                            /*
                            //获取wifi服务
                            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                            //判断wifi是否开启
                            if (wifiManager.isWifiEnabled()) {
                                //wifiManager.setWifiEnabled(true);
                                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                                int ipAddress = wifiInfo.getIpAddress();
                                ip = intToIp(ipAddress);
                            }
                            else {
                            */
                                for (Enumeration<NetworkInterface> en = NetworkInterface
                                        .getNetworkInterfaces(); en.hasMoreElements(); ) {
                                    NetworkInterface intf = en.nextElement();
                                    for (Enumeration<InetAddress> enumIpAddr = intf
                                            .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                                        InetAddress inetAddress = enumIpAddr.nextElement();
                                        if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                                            ip = inetAddress.getHostAddress().toString();
                                            break;
                                        }
                                    }
                                }
                            //}
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("本机IP")
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setMessage(ip)
                                    .setPositiveButton("确定",null)
                                    .setNegativeButton("取消",null);
                            builder.show();
                        } catch (SocketException ex) {
                            Log.e("MainActivity", ex.toString());
                        }

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
                Toast.makeText(getApplicationContext(),"关闭PopupMenu",Toast.LENGTH_SHORT).show();
            }
        });

        popupMenu.show();
    }

    private String intToIp(int i) {

        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    showFileChooser();
                }
                else{
                    Toast.makeText(MainActivity.this,"You denied the permission",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult( Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",  Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String path = FileUtils.getPath(this, uri);
                    Log.d("MainActivity",path);
                    Intent intent = new Intent(MainActivity.this, CreateMenuActivity.class);
                    intent.putExtra("path",path);
                    startActivityForResult(intent,CREATE_MENU_BACK);
                }
                break;
            case CREATE_MENU_BACK:
                View view = mViews.get(0);
                getMenus(view);
                break;
            case CLICK_MENU_BACK:
                View view2 = mViews.get(0);
                getMenus(view2);
                break;
            case CLICK_COURCE_BACK:
                View view3 = mViews.get(1);
                getCourses(view3);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getMenus(View view){
        menus = DataSupport.findAll(Menu.class);
        MenuAdapter adapter = new MenuAdapter(MainActivity.this,R.layout.item2,menus);
        menuList = (ListView) view.findViewById(R.id.menulist);
        menuList.setAdapter(adapter);
    }

    private void getCourses(View view){
        courses = DataSupport.findAll(Course.class);
        CourseAdapter adapter = new CourseAdapter(MainActivity.this,R.layout.item2,courses);
        courceList = (ListView) view.findViewById(R.id.courselist);
        courceList.setAdapter(adapter);
    }
}
