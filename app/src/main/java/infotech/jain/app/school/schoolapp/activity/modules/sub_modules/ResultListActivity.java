package infotech.jain.app.school.schoolapp.activity.modules.sub_modules;

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
import infotech.jain.app.school.schoolapp.activity.modules.ResultModule;
import infotech.jain.app.school.schoolapp.adapter.ExamsListAdapter;
import infotech.jain.app.school.schoolapp.adapter.ResultListAdapter;
import infotech.jain.app.school.schoolapp.bean.Exam;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;
import infotech.jain.app.school.schoolapp.sqlite_db.DBHelper;

public class ResultListActivity extends AppCompatActivity {


    private ProgressBar        progressBar;
    private RecyclerView       resultRecycleView;
    private Toolbar            toolbar;
    ArrayList<Exam>            result_list;
    StaggeredGridLayoutManager mStaggeredLayoutManager;
    private String TAG = "ResultListActivity";
    private Exam exam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        progressBar       = (ProgressBar) findViewById(R.id.prgBar);
        resultRecycleView = (RecyclerView) findViewById(R.id.resultRecycleView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("Result");

        result_list = new ArrayList<Exam>();

        exam = new Exam();

        Intent intent1 = getIntent();
        String exam_term_id   = intent1.getStringExtra("exam_term_id");
        String exam_term_name = intent1.getStringExtra("exam_term_name");

        exam.setExamTermId(Integer.parseInt(exam_term_id));
        exam.setExamTermName(exam_term_name);

        getResultList();

    }

    public void getResultList()
    {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String url = Web_API_Config.exam_result_API + "ClassId=" + dbHelper.getClassId() + "&SId=" + dbHelper.getPersonalUserId() + "&Exam_Term_Id="+exam.getExamTermId() + "&Term_Name=" + exam.getExamTermName();

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressBar.setVisibility(View.GONE);
                        resultRecycleView.setVisibility(View.VISIBLE);


                        Log.e(TAG, response.toString());
                        try {
                            String success  = response.getString("Success");
                            String termName = response.getString("Term_Name");

                            if(success.equals("1"))
                            {

                                JSONArray jsonArray = response.getJSONArray("Marks_List");

                                for(int i=-0; i<jsonArray.length(); i++)
                                {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String subjectName   = jsonObject.getString("Subject_Name");
                                    String totalMarks    = jsonObject.getString("Total_Marks");
                                    String outOfTotal    = jsonObject.getString("Out_Of_Total");
                                    String grade         = jsonObject.getString("Grade");
                                    String status        = jsonObject.getString("Status");


                                    Exam exam1 = new Exam();
                                    exam1.setSubjectName(subjectName);
                                    exam1.setTotalMarks(totalMarks);
                                    exam1.setOutOfTotal(outOfTotal);
                                    exam1.setGrade(grade);
                                    exam1.setStatus(status);

                                    result_list.add(exam1);


                                }
                                setItemToRecycleView();
                            }else{
                                new SweetAlertDialog(ResultListActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("No Result Available")
                                        .setContentText("Please Try After Exam")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
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
        resultRecycleView.setVisibility(View.VISIBLE);
        resultRecycleView.setHasFixedSize(true);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        resultRecycleView.setLayoutManager(mStaggeredLayoutManager);
        ResultListAdapter resultListAdapter = new ResultListAdapter(result_list, getApplicationContext(), ResultListActivity.this);
        resultRecycleView.setAdapter(resultListAdapter);
    }


}
