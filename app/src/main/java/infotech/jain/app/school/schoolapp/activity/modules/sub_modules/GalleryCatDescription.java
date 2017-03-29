package infotech.jain.app.school.schoolapp.activity.modules.sub_modules;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.adapter.GalleryCatDescAdapter;
import infotech.jain.app.school.schoolapp.adapter.GalleryCategoryAdapter;
import infotech.jain.app.school.schoolapp.bean.GalleryCategoryDesc;
import infotech.jain.app.school.schoolapp.bean.GalleryCategoryImages;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;

public class GalleryCatDescription extends AppCompatActivity {

    private GalleryCategoryImages galleryCategoryImages;

    RecyclerView galCatRecyclerView;
    ProgressBar progressBar;

    ArrayList<GalleryCategoryDesc> galleryCategoryDescArrayList;
    StaggeredGridLayoutManager mStaggeredLayoutManager;
    String TAG = "GalleryCategoryDescription";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_cat_description);



        Intent intent1  = getIntent();
        String galCatId = intent1.getStringExtra("getCatId");
        String galCatName = intent1.getStringExtra("galCatName");
        galleryCategoryImages = new GalleryCategoryImages();
        galleryCategoryImages.setId(galCatId);
        galleryCategoryImages.setCat_name(galCatName);

        galCatRecyclerView = (RecyclerView) findViewById(R.id.galCatDescRecycleView);
        progressBar        = (ProgressBar) findViewById(R.id.prgBar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle(galleryCategoryImages.getCat_name());

        galleryCategoryDescArrayList = new ArrayList<GalleryCategoryDesc>();

        getGalleryCategoriesDescription();

    }

    public void getGalleryCategoriesDescription()
    {
        String url = Web_API_Config.galleryCategoryDescAPI+galleryCategoryImages.getId();

        Log.e(TAG, url);
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressBar.setVisibility(View.GONE);
                        galCatRecyclerView.setVisibility(View.VISIBLE);


                        Log.e(TAG, response.toString());
                        try {
                            String success = response.getString("Status");

                            if(success.equals("1"))
                            {

                                JSONArray jsonArray = response.getJSONArray("Gallery_Image_List");

                                for(int i=-0; i<jsonArray.length(); i++)
                                {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    GalleryCategoryDesc galleryCategoryDesc = new GalleryCategoryDesc();
                                    galleryCategoryDesc.setImage_path(jsonObject.getString("Image_Path"));
                                    galleryCategoryDesc.setThumb_image(jsonObject.getString("Thumb_Image"));
                                    galleryCategoryDesc.setImage_caption(jsonObject.getString("Image_Caption"));
                                    galleryCategoryDesc.setImage_Alt(jsonObject.getString("Image_Alt"));
                                    galleryCategoryDesc.setGallery_year_Id(jsonObject.getString("Gallery_Year_Id"));


                                    galleryCategoryDescArrayList.add(galleryCategoryDesc);

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        galCatRecyclerView.setHasFixedSize(true);
                        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                        mStaggeredLayoutManager.setSpanCount(2);
                        galCatRecyclerView.setLayoutManager(mStaggeredLayoutManager);

                        GalleryCatDescAdapter galleryCatDescAdapter = new GalleryCatDescAdapter(galleryCategoryDescArrayList, getApplicationContext(), GalleryCatDescription.this);
                        galCatRecyclerView.setAdapter(galleryCatDescAdapter);


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
