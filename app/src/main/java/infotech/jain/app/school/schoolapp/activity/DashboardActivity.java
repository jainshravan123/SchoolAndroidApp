package infotech.jain.app.school.schoolapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.activity.nav_drawer.AboutSchool;
import infotech.jain.app.school.schoolapp.activity.nav_drawer.Events;
import infotech.jain.app.school.schoolapp.activity.nav_drawer.News;
import infotech.jain.app.school.schoolapp.activity.nav_drawer.Notification;
import infotech.jain.app.school.schoolapp.activity.nav_drawer.Profile;
import infotech.jain.app.school.schoolapp.activity.nav_drawer.Settings;
import infotech.jain.app.school.schoolapp.adapter.ModuleAdapter;
import infotech.jain.app.school.schoolapp.bean.Module;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;
import infotech.jain.app.school.schoolapp.network.CheckInternetConnection;
import infotech.jain.app.school.schoolapp.session.SessionManager;
import infotech.jain.app.school.schoolapp.sqlite_db.DBHelper;
import infotech.jain.app.school.schoolapp.sqlite_db.FirebaseTokenStorage;
import infotech.jain.app.school.schoolapp.utility.ModuleSelector;

public class DashboardActivity extends AppCompatActivity {


    ProgressBar  prgBar1;
    RecyclerView recyclerView1;
    Toolbar      toolbar1;
    DrawerLayout mDrawerLayout;
    ArrayList<Module> modules;
    StaggeredGridLayoutManager mStaggeredLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        prgBar1           = (ProgressBar) findViewById(R.id.prgBar1);
        recyclerView1     = (RecyclerView) findViewById(R.id.recycleView1);
        toolbar1          = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout     = (DrawerLayout) findViewById(R.id.drawer_layout);
        //List of Modules
        modules = new ArrayList<Module>();



        setSupportActionBar(toolbar1);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);


        //Checking Internet Connection
        CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
        checkInternetConnection.showNetworkIdentifier(getApplicationContext(), DashboardActivity.this);

        if(checkInternetConnection.checkingInternetConnection(getApplicationContext())){
            //Getting Modules by hitting REST Web Service
            getModules();
        }
        else{
            prgBar1.setVisibility(View.GONE);
        }



        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();

                switch(id){

                    case R.id.navigation_item_about_school:
                                                              Intent abt_sch_intent = new Intent(getApplicationContext(), AboutSchool.class);
                                                              startActivity(abt_sch_intent);
                                                              return true;
                    case R.id.navigation_item_news:
                                                              Intent new_intent  = new Intent(getApplicationContext(), News.class);
                                                              startActivity(new_intent);
                                                              return true;

                    case R.id.navigation_item_location_events:
                                                               Intent event_intent = new Intent(getApplicationContext(), Events.class);
                                                               startActivity(event_intent);
                                                               return true;

                    case R.id.navigation_item_notification:
                                                               Intent noti_intent = new Intent(getApplicationContext(), Notification.class);
                                                               startActivity(noti_intent);
                                                               return true;

                    case R.id.navigation_item_share:
                                                                shareAppProcess();
                                                                return true;

                    case R.id.navigation_item_settings:
                                                                Intent setting_intent = new Intent(getApplicationContext(), Settings.class);
                                                                startActivity(setting_intent);
                                                                return true;
                    case R.id.navigation_item_logout:
                                                                logoutProcess();
                                                                return true;


                    default:
                                                               return true;

                }


            }
        });

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        View headerLayout = navigationView.getHeaderView(0);
        TextView txtUsername = (TextView) headerLayout.findViewById(R.id.username);
        TextView txtViewEmail = (TextView) headerLayout.findViewById(R.id.drawerTxtView);
        txtUsername.setText(dbHelper.getName());
        txtViewEmail.setText(dbHelper.getUsername());




        //OnClick Listener on Profile View
        RelativeLayout drawer_layout_relative = (RelativeLayout) headerLayout.findViewById(R.id.drawer_layout_relative);
        drawer_layout_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                Intent intent1 = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.miCompose:
                   Toast.makeText(getApplicationContext(), "Compose Clicked", Toast.LENGTH_LONG).show();
                   return true;
            case R.id.miProfile:
                if(mStaggeredLayoutManager.getSpanCount() == 1){
                    item.setIcon(R.mipmap.ic_view_list_white_48dp);
                    mStaggeredLayoutManager.setSpanCount(2);
                }else{
                    item.setIcon(R.mipmap.ic_grid_on_white_48dp);
                    mStaggeredLayoutManager.setSpanCount(1);
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void getModules(){

        prgBar1.setVisibility(View.VISIBLE);

        String url = Web_API_Config.modulesAPI;
        Log.e("URL", url);


            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            prgBar1.setVisibility(View.GONE);
                            recyclerView1.setVisibility(View.VISIBLE);
                            Log.d("Module Response 1", response.toString());


                            try {


                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject s_module  = (JSONObject) response.get(i);
                                    String module_id     = s_module.getString("Module_Id");
                                    String module_name   = s_module.getString("Module_Name");
                                    String img_path      = s_module.getString("Module_Thunbnail");
                                    String module_status = s_module.getString("Module_Status");

                                    ModuleSelector moduleSelector = new ModuleSelector();
                                    String imageName = moduleSelector.getModuleNameByModuleId(Integer.parseInt(module_id));
                                    //for module image
                                    String uri = "@drawable/"+imageName.toLowerCase();
                                    //String uri = "@drawable/transport";
                                    int imageResource = getApplicationContext().getResources().getIdentifier(uri, null, getApplicationContext().getPackageName());



                                    Module module = new Module();
                                    module.setId(Integer.parseInt(module_id));
                                    module.setName(module_name);
                                    module.setStatus(module_status);
                                    module.setImage(img_path);
                                    module.setImageId(imageResource);


                                    if(module.getStatus().equals("Yes")){
                                        modules.add(module);
                                    }

                                }
                            }catch (JSONException e){
                             e.printStackTrace();
                            }

                                                  }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();

                }
            });


        Volley.newRequestQueue(this).add(jsonArrayRequest);



        recyclerView1.setHasFixedSize(true);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(2);
        recyclerView1.setLayoutManager(mStaggeredLayoutManager);

        ModuleAdapter moduleAdapter = new ModuleAdapter(modules, getApplicationContext());

        recyclerView1.setAdapter(moduleAdapter);

    }

    public void logoutProcess(){

        final SweetAlertDialog sAlertDialog;
        sAlertDialog = new SweetAlertDialog(DashboardActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        sAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sAlertDialog.setTitleText("Logging Out...");
        sAlertDialog.setCancelable(false);
        sAlertDialog.show();
        //Counter for holding splash screens
        new CountDownTimer(2000,1000) {

            @Override
            public void onFinish() {


                sAlertDialog.dismiss();
                //Deleting User Data
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                dbHelper.deleteUserTable();


                //Deleting Session
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                sessionManager.setLogin(false);

                //Redirecting to SignIn Activity
                Intent intent1 = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent1);
                finish();

            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();



    }

    private void shareAppProcess() {
        //sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AndroidSolved");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Now Learn Android with AndroidSolved clicke here to visit https://androidsolved.wordpress.com/ ");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
