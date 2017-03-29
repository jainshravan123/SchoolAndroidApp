package infotech.jain.app.school.schoolapp.activity.modules;

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

import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.adapter.CircularAdapter;
import infotech.jain.app.school.schoolapp.adapter.ModuleAdapter;
import infotech.jain.app.school.schoolapp.bean.Circular;
import infotech.jain.app.school.schoolapp.bean.UserProfile;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;

public class CircularsModule extends AppCompatActivity {


    ArrayList<Circular> list_circular;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    Toolbar toolbar;
    StaggeredGridLayoutManager mStaggeredLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circulars_module);

        progressBar   = (ProgressBar) findViewById(R.id.prgBar1);
        recyclerView  = (RecyclerView) findViewById(R.id.circularRecyleView1);
        toolbar       = (Toolbar)     findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("Circulars");


        list_circular = new ArrayList<Circular>();

        getCircularsFromWeb();

    }

    public void getCircularsFromWeb()
    {

        String url = Web_API_Config.circularAPI;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("Circular API : ", response.toString());
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);


                        try {
                            String success = response.getString("Status");

                            if(success.equals("1"))
                            {

                                JSONArray jsonArray = response.getJSONArray("CircularList");

                                for(int i=-0; i<jsonArray.length(); i++)
                                {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    Circular circular = new Circular();
                                    circular.setId(Integer.parseInt(jsonObject.getString("Circular_Id")));
                                    circular.setHeading(jsonObject.getString("Heading"));
                                    circular.setCreatedAt(jsonObject.getString("Created_At"));
                                    circular.setUpdatedAt(jsonObject.getString("Updated_At"));

                                    list_circular.add(circular);

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        for(Circular circular : list_circular){
                            Log.e("Circular Heading : ", circular.getId() + "  -  "+ circular.getHeading());
                        }

                            recyclerView.setHasFixedSize(true);
                            mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                            mStaggeredLayoutManager.setSpanCount(1);
                            recyclerView.setLayoutManager(mStaggeredLayoutManager);

                            CircularAdapter circularAdapter = new CircularAdapter(list_circular, getApplicationContext());
                            recyclerView.setAdapter(circularAdapter);


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);

    }
}
