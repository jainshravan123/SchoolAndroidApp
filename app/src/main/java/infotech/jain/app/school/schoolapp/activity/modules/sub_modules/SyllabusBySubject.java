package infotech.jain.app.school.schoolapp.activity.modules.sub_modules;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import infotech.jain.app.school.schoolapp.activity.SignInActivity;
import infotech.jain.app.school.schoolapp.activity.modules.SyllabusModule;
import infotech.jain.app.school.schoolapp.adapter.SubjectAdapter;
import infotech.jain.app.school.schoolapp.adapter.SyllabusBySubjectAdapter;
import infotech.jain.app.school.schoolapp.bean.Module;
import infotech.jain.app.school.schoolapp.bean.Student;
import infotech.jain.app.school.schoolapp.bean.Syllabus;
import infotech.jain.app.school.schoolapp.bean.User;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;
import infotech.jain.app.school.schoolapp.sqlite_db.DBHelper;
import infotech.jain.app.school.schoolapp.sqlite_db.FirebaseTokenStorage;
import infotech.jain.app.school.schoolapp.utility.ModuleSelector;

public class SyllabusBySubject extends AppCompatActivity {

    private Toolbar      toolbar;
    private String       sub_id;
    private ProgressBar  progressBar;
    private RecyclerView syllabusRecycleView;
    private ArrayList<Syllabus> syllabus_list;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus_by_subject);

        progressBar         = (ProgressBar) findViewById(R.id.prgBar);
        syllabusRecycleView = (RecyclerView) findViewById(R.id.syllabusRecycleView);
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
        getSupportActionBar().setTitle("Syllabus");

        Intent intent1 = getIntent();
        sub_id  = intent1.getStringExtra("subject_id");

        syllabus_list = new ArrayList<Syllabus>();
        getSyllabusBySubject();
    }

    public void getSyllabusBySubject()
    {
        String url = Web_API_Config.syllabus_API + "?Class_Id=" + "8" + "&Subject_Id="+ "124" ;

        Log.e("Syllabus URL : ", url);
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressBar.setVisibility(View.GONE);
                     Log.e("Syllabus Response : ", response.toString());


                        try {
                            String status = response.getString("Syllabus_Status");

                            if(status.equals("1"))
                            {

                                JSONArray jsonArray = response.getJSONArray("Syllabus_List");
                                for(int i=0; i<jsonArray.length(); i++){

                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    int id                = Integer.parseInt(jsonObject.getString("Id"));
                                    String exam_name      = jsonObject.getString("Exam_Name");
                                    String attachment_url = jsonObject.getString("Attachment");

                                    Syllabus syllabus     = new Syllabus();
                                    syllabus.setId(id);
                                    syllabus.setExam_name(exam_name);
                                    syllabus.setAttachment_URL(attachment_url);
                                    syllabus_list.add(syllabus);

                                }
                                //setItemToRecycleView();
                                getReadWritePermissison();
                            }else{
                                new SweetAlertDialog(SyllabusBySubject.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Syllabus Not Available")
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
        syllabusRecycleView.setVisibility(View.VISIBLE);
        syllabusRecycleView.setHasFixedSize(true);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        syllabusRecycleView.setLayoutManager(mStaggeredLayoutManager);

        SyllabusBySubjectAdapter syllabusBySubjectAdapter = new SyllabusBySubjectAdapter(syllabus_list, getApplicationContext(), SyllabusBySubject.this);

        syllabusRecycleView.setAdapter(syllabusBySubjectAdapter);
        //getReadWritePermissison();


    }

    public void getReadWritePermissison()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
                //setItemToRecycleView();
            }
        }else{
            setItemToRecycleView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    setItemToRecycleView();
                    Log.e("Permission", "Granted");

                } else
                {
                    Intent intent1 = new Intent(getApplicationContext(), SyllabusModule.class);
                    startActivity(intent1);
                    finish();
                    Log.e("Permission", "Denied");
                }
                return;
            }
        }
    }

}
