package infotech.jain.app.school.schoolapp.activity.modules;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.adapter.AttendanceAdapter;
import infotech.jain.app.school.schoolapp.adapter.HolidayListAdapter;
import infotech.jain.app.school.schoolapp.bean.Attendance;
import infotech.jain.app.school.schoolapp.bean.Holiday;
import infotech.jain.app.school.schoolapp.bean.HolidayDesc;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;
import infotech.jain.app.school.schoolapp.sqlite_db.DBHelper;

public class HolidaysModule extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView holidaysRecycleView;
    private ProgressBar progressBar;
    private ArrayList<Holiday> holidayArrayList;
    String TAG = "HolidaysModule";
    StaggeredGridLayoutManager mStaggeredLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holidays_module);

        holidaysRecycleView = (RecyclerView) findViewById(R.id.holidaysRecycleView);
        progressBar         = (ProgressBar) findViewById(R.id.prgBar);
        toolbar             = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("Holidays");

        holidayArrayList = new ArrayList<Holiday>();
        getHolidaysList();
    }


    public void getHolidaysList()
    {

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String url = Web_API_Config.holiday_API + "SId=" + dbHelper.getPersonalUserId() + "&ClassId=" + dbHelper.getClassId();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.e(TAG, "Holiday Response : "+response);
                        progressBar.setVisibility(View.GONE);
                        holidaysRecycleView.setVisibility(View.VISIBLE);
                        for(int i=0; i<response.length(); i++)
                        {
                            try {
                                JSONObject jsonObject = (JSONObject) response.get(i);

                                Holiday holiday = new Holiday();
                                holiday.setMonth(String.valueOf(jsonObject.getInt("Month")));
                                holiday.setMonth_name(String.valueOf(jsonObject.getString("Month_Name")));
                                holiday.setYear(String.valueOf(jsonObject.getInt("Year")));
                                holiday.setNoOfWorkingDays(String.valueOf(jsonObject.getInt("No_Of_Working_Days")));
                                holiday.setNoOfHolidays(String.valueOf(jsonObject.getInt("No_Of_Holidays")));

                                if(Integer.parseInt(holiday.getNoOfHolidays()) > 0)
                                {
                                    ArrayList<HolidayDesc> holidayDescArrayList = new ArrayList<HolidayDesc>();

                                    JSONArray jsonArray = jsonObject.getJSONArray("Holiday_List");

                                    for(int j=0; j< jsonArray.length(); j++)
                                    {
                                        JSONObject jsonObject1 = (JSONObject) jsonArray.get(j);
                                        HolidayDesc holidayDesc = new HolidayDesc();
                                        holidayDesc.setNoOfDays(String.valueOf(jsonObject1.getString("Day_No_In_Month")));
                                        holidayDesc.setDaysOfWeek(String.valueOf(jsonObject1.getString("Day_Of_Week")));
                                        holidayDesc.setDescription(String.valueOf(jsonObject1.getString("Description")));
                                        holidayDescArrayList.add(holidayDesc);

                                    }

                                    holiday.setHolidaysDescList(holidayDescArrayList);

                                }

                                holidayArrayList.add(holiday);

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
        holidaysRecycleView.setVisibility(View.VISIBLE);
        holidaysRecycleView.setHasFixedSize(true);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        holidaysRecycleView.setLayoutManager(mStaggeredLayoutManager);

        HolidayListAdapter holidayListAdapter = new HolidayListAdapter(holidayArrayList, getApplicationContext(), this);

        holidaysRecycleView.setAdapter(holidayListAdapter);
    }

}
