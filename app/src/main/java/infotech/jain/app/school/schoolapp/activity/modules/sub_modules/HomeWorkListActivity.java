package infotech.jain.app.school.schoolapp.activity.modules.sub_modules;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.activity.SignInActivity;
import infotech.jain.app.school.schoolapp.activity.modules.HomeworkModule;
import infotech.jain.app.school.schoolapp.adapter.HomeworkListAdapter;
import infotech.jain.app.school.schoolapp.adapter.SubjectAdapterForHomework;
import infotech.jain.app.school.schoolapp.bean.Homework;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;
import infotech.jain.app.school.schoolapp.sqlite_db.DBHelper;

public class HomeWorkListActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView homeworkRecycleView;
    Toolbar toolbar;
    ArrayList<Homework> homework_list;
    StaggeredGridLayoutManager mStaggeredLayoutManager;
    String subject_id;

    String TAG = "HomeWorkListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work_list);

        homeworkRecycleView = (RecyclerView) findViewById(R.id.homeworkRecycleView);
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
        getSupportActionBar().setTitle("Homework");


        Intent intent1 =  getIntent();
        subject_id     = intent1.getStringExtra("subject_id");
        homework_list   = new ArrayList<Homework>();

        getHomeworkList();
    }


    public void getHomeworkList()
    {

        DBHelper dbHelper = new DBHelper(getApplicationContext());

        //subject_id = "124";
        String url = Web_API_Config.homework_by_subject_API + "Subject_Id="+subject_id+"&Section_Id="+dbHelper.getSectionId();

        Log.e(TAG, "URL : "+url);
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {

                        try {
                            String status = response.getString("Homework_Status");
                            if(status.equals("1"))
                            {
                                progressBar.setVisibility(View.GONE);
                                JSONArray jsonArray = response.getJSONArray("Homwork_List");
                                for(int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    Homework homework = new Homework();
                                    homework.setId(Integer.parseInt(jsonObject.getString("Homework_Id")));
                                    homework.setCaption(String.valueOf(jsonObject.getString("Homework_Caption")));
                                    homework.setCreated_at(String.valueOf(jsonObject.getString("Created_At")));
                                    homework.setType(String.valueOf(jsonObject.get("Type")));
                                    homework_list.add(homework);

                                    setItemToRecycleView();
                                }
                            }else{
                                new SweetAlertDialog(HomeWorkListActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("No Homework")
                                        .setContentText("Please Try For Other Subjects")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                /*Intent intent2 = new Intent(getApplicationContext(), HomeworkModule.class);
                                                startActivity(intent2);*/
                                                finish();
                                            }
                                        })
                                        .show();
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

    public void setItemToRecycleView()
    {
        homeworkRecycleView.setVisibility(View.VISIBLE);
        homeworkRecycleView.setHasFixedSize(true);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        homeworkRecycleView.setLayoutManager(mStaggeredLayoutManager);
        HomeworkListAdapter homeworkListAdapter = new HomeworkListAdapter(homework_list, getApplicationContext());
        homeworkRecycleView.setAdapter(homeworkListAdapter);
    }

}
