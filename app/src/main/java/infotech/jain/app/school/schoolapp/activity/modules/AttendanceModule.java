package infotech.jain.app.school.schoolapp.activity.modules;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.activity.DashboardActivity;
import infotech.jain.app.school.schoolapp.activity.SignInActivity;
import infotech.jain.app.school.schoolapp.adapter.AttendanceAdapter;
import infotech.jain.app.school.schoolapp.adapter.SubjectAdapter;
import infotech.jain.app.school.schoolapp.bean.Attendance;
import infotech.jain.app.school.schoolapp.bean.Subject;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;
import infotech.jain.app.school.schoolapp.sqlite_db.DBHelper;

public class AttendanceModule extends AppCompatActivity {


    private ProgressBar  progressBar;
    private RecyclerView attendanceRecycleView;
    Toolbar              toolbar;
    ArrayList<Attendance> attendance_list;
    StaggeredGridLayoutManager mStaggeredLayoutManager;
    String TAG = "AttendanceModule";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_module);

        attendanceRecycleView = (RecyclerView) findViewById(R.id.attendanceRecycleView);
        progressBar           = (ProgressBar) findViewById(R.id.prgBar);
        toolbar               = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setTitle("Attendance");

        attendance_list = new ArrayList<Attendance>();
        getAttendance();
    }


    public void getAttendance()
    {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String url = Web_API_Config.attendance_API + dbHelper.getPersonalUserId();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {

                        progressBar.setVisibility(View.GONE);
                        Log.e("Attendance Response : ", response.toString());

                        for(int i=0; i<response.length(); i++)
                        {
                            try {
                                JSONObject jsonObject = (JSONObject) response.get(i);

                                Attendance attendance = new Attendance();
                                attendance.setMonth(String.valueOf(jsonObject.getInt("Month")));
                                attendance.setMonth_name(jsonObject.getString("Month_Name"));
                                attendance.setYear(String.valueOf(jsonObject.getInt("Year")));
                                attendance.setAbsent(String.valueOf(jsonObject.getInt("NoOfAbsent")));
                                attendance.setWorking_days(String.valueOf(jsonObject.getInt("NoOfWorkingDays")));


                                if(!(attendance.getAbsent().equals(String.valueOf(0))))
                                {
                                    JSONArray jsonArray = jsonObject.getJSONArray("Dates");
                                    Log.e(TAG, "JSON Array Size : : "+jsonArray.length());

                                    ArrayList<String> absented_days_list = new ArrayList<String>();

                                    for(int j=0; j< jsonArray.length(); j++)
                                    {
                                         JSONObject jsonObject1 = (JSONObject) jsonArray.get(j);
                                         absented_days_list.add(jsonObject1.getString("Day"));

                                    }
                                    attendance.setAbsented_days_list(absented_days_list);
                                }

                              /*  Object obj = jsonObject.get("Dates");

                                if(obj instanceof JSONArray)
                                {
                                    ArrayList<String> absented_days_list = new ArrayList<String>();

                                    JSONArray jsonArray1 = (JSONArray) obj;

                                    if(jsonArray1.length() > 0)
                                    {
                                        for(int j=0; j<jsonArray1.length(); j++)
                                        {
                                            JSONObject jsonObject1 = (JSONObject) jsonArray1.get(i);
                                            absented_days_list.add(String.valueOf(jsonObject1.getString("Day")));
                                        }

                                        attendance.setAbsented_days_list(absented_days_list);
                                    }

                                }
                                else if(obj instanceof String)
                                {

                                }*/

                                attendance_list.add(attendance);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        setItemToRecycleView();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });


        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }



    public void setItemToRecycleView()
    {
        attendanceRecycleView.setVisibility(View.VISIBLE);
        attendanceRecycleView.setHasFixedSize(true);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        attendanceRecycleView.setLayoutManager(mStaggeredLayoutManager);

        AttendanceAdapter attendanceAdapter = new AttendanceAdapter(attendance_list, getApplicationContext(), this);

        attendanceRecycleView.setAdapter(attendanceAdapter);
    }

}
