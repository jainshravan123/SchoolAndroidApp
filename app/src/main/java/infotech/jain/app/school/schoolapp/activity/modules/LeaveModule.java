package infotech.jain.app.school.schoolapp.activity.modules;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.activity.SignInActivity;
import infotech.jain.app.school.schoolapp.adapter.ViewPagerAdapter;
import infotech.jain.app.school.schoolapp.bean.Leave;
import infotech.jain.app.school.schoolapp.bean.LeaveDescription;
import infotech.jain.app.school.schoolapp.bean.LeaveType;
import infotech.jain.app.school.schoolapp.bean.Module;
import infotech.jain.app.school.schoolapp.bean.Student;
import infotech.jain.app.school.schoolapp.bean.User;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;
import infotech.jain.app.school.schoolapp.fragment.ApplyLeave;
import infotech.jain.app.school.schoolapp.fragment.PreviousLeaves;
import infotech.jain.app.school.schoolapp.sqlite_db.DBHelper;
import infotech.jain.app.school.schoolapp.sqlite_db.FirebaseTokenStorage;
import infotech.jain.app.school.schoolapp.utility.ModuleSelector;

public class LeaveModule extends AppCompatActivity {


    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    ProgressBar progressBar;
    ArrayList<LeaveType> leave_type_list;
    ArrayList<LeaveDescription> previous_leaves_list;
    String TAG = "LeaveModule";

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_module);


        progressBar     = (ProgressBar) findViewById(R.id.prgBar);
        tabLayout       = (TabLayout)findViewById(R.id.tabLayout);
        viewPager       = (ViewPager)findViewById(R.id.viewPager);
        toolbar         = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("Leave");


        leave_type_list      = new ArrayList<LeaveType>();
        previous_leaves_list = new ArrayList<LeaveDescription>();
        getLeaveTypes();

    }


    public void getLeaveTypes()
    {
        String url = Web_API_Config.leave_type_API;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.e(TAG, "Leaves Types Data : " + response.toString());


                        for(int i=0; i<response.length(); i++)
                        {
                            try {
                                JSONObject jsonObject     = (JSONObject) response.get(i);
                                int leave_type_id         = Integer.parseInt(jsonObject.getString("Id"));
                                String leave_type_caption = jsonObject.getString("Caption");

                                LeaveType leaveType = new LeaveType();
                                leaveType.setId(leave_type_id);
                                leaveType.setCaption(leave_type_caption);
                                leave_type_list.add(leaveType);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        getPreviousLeaves();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });


        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    public void getPreviousLeaves()
    {

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String url = Web_API_Config.previous_leaves_API + dbHelper.getPersonalUserId();

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        Log.e(TAG, "Previous Leaves Data : " + response.toString());

                        try {
                            String status = response.getString("Status");

                            if(status.equals("1"))
                            {
                                JSONArray jsonArray = response.getJSONArray("List");
                                for(int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    String leave_application_number = jsonObject.getString("Leave_Application_No");
                                    String leave_type_caption       = jsonObject.getString("Leave_Type_Caption");
                                    String leave_status             = jsonObject.getString("Status");

                                    LeaveType leaveType = new LeaveType();
                                    leaveType.setCaption(leave_type_caption);

                                    Leave leave = new Leave();
                                    leave.setLeaveType(leaveType);

                                    LeaveDescription leaveDescription = new LeaveDescription();
                                    leaveDescription.setLeave_application_number(leave_application_number);

                                    leaveDescription.setLeave(leave);
                                    leaveDescription.setStatus(leave_status);

                                    previous_leaves_list.add(leaveDescription);
                                }
                                showFragments();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);
    }

    public void showFragments()
    {
        progressBar.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new PreviousLeaves(previous_leaves_list), "Previous");
        viewPagerAdapter.addFragments(new ApplyLeave(leave_type_list), "Apply");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}
