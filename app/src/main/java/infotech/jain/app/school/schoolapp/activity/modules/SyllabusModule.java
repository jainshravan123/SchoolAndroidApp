package infotech.jain.app.school.schoolapp.activity.modules;

import android.app.Activity;
import android.graphics.Color;
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
import java.util.Random;

import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.adapter.PreviousLeavesAdapter;
import infotech.jain.app.school.schoolapp.adapter.SubjectAdapter;
import infotech.jain.app.school.schoolapp.bean.Module;
import infotech.jain.app.school.schoolapp.bean.Subject;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;
import infotech.jain.app.school.schoolapp.sqlite_db.DBHelper;
import infotech.jain.app.school.schoolapp.utility.ModuleSelector;

public class SyllabusModule extends AppCompatActivity {


    private ProgressBar  progressBar;
    private RecyclerView subjectRecycleView;
    Toolbar              toolbar;
    ArrayList<Subject>   subject_list;
    StaggeredGridLayoutManager mStaggeredLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus_module);

        subjectRecycleView = (RecyclerView) findViewById(R.id.subjectRecycleView);
        progressBar        = (ProgressBar) findViewById(R.id.prgBar);
        toolbar            = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        getSupportActionBar().setTitle("Subjects");


        subject_list = new ArrayList<Subject>();
        getAllSubjects();
    }


    public void getAllSubjects()
    {

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String url = Web_API_Config.subjects_API + dbHelper.getPersonalUserId();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        progressBar.setVisibility(View.GONE);
                        Log.d("Subjects Response : ", response.toString());

                        for(int i=0; i<response.length(); i++)
                        {
                            try {
                                JSONObject jsonObject = (JSONObject) response.get(i);

                                int sub_id      = Integer.parseInt(jsonObject.getString("Subject_Id"));
                                String sub_name = jsonObject.getString("Subject");
                                String sub_type = "Primary";

                                Subject subject = new Subject();
                                subject.setId(sub_id);
                                subject.setName(sub_name);
                                subject.setType(sub_type);

                                subject_list.add(subject);

                                setItemToRecycleView();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

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
        subjectRecycleView.setVisibility(View.VISIBLE);
        subjectRecycleView.setHasFixedSize(true);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        subjectRecycleView.setLayoutManager(mStaggeredLayoutManager);

        SubjectAdapter subjectAdapter = new SubjectAdapter(subject_list, getApplicationContext());

        subjectRecycleView.setAdapter(subjectAdapter);
    }


}
