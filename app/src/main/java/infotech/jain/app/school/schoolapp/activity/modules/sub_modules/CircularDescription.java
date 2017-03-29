package infotech.jain.app.school.schoolapp.activity.modules.sub_modules;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.bean.CircularDesc;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;

public class CircularDescription extends AppCompatActivity {

    String id;
    ProgressBar progressBar;
    Toolbar      toolbar1;
    TextView txtId, txtHeading, txtDetails, txtAttachment, txtCreatedAt, txtUpdatedAt;
    RelativeLayout relativeLayout;
    CircularDesc circularDesc;
    String TAG = "CircularDescription";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular_description);

        Intent intent1 = this.getIntent();
        id = String.valueOf(intent1.getStringExtra("circular_id"));


        relativeLayout = (RelativeLayout) findViewById(R.id.layout1);
        txtId          = (TextView) findViewById(R.id.txtId);
        txtHeading     = (TextView) findViewById(R.id.txtHeading);
        txtDetails     = (TextView) findViewById(R.id.txtDetails);
        txtAttachment  = (TextView) findViewById(R.id.txtAttachment);
        txtCreatedAt   = (TextView) findViewById(R.id.txtCreatedAt);
        txtUpdatedAt   = (TextView) findViewById(R.id.txtUpdatedAt);
        progressBar    = (ProgressBar) findViewById(R.id.prgBar1);
        toolbar1       = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar1);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setTitle("Circular Description");

        getCircularDescriptionData();




    }


    public void getCircularDescriptionData()
    {
        String url = Web_API_Config.circularDescAPI + id;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("Circular Desc API : ", response.toString());
                        progressBar.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        txtAttachment.setClickable(true);


                        try {
                            String success = response.getString("Status");

                            if(success.equals("1"))
                            {
                                    String circularId   = response.getString("Circular_Id");
                                    String heading      = response.getString("Heading");
                                    String details      = response.getString("Detail");
                                    String attachment   = response.getString("Attachment");
                                    String created_at   = response.getString("Created_At");
                                    String updated_at   = response.getString("Updated_At");

                                circularDesc =new CircularDesc();
                                circularDesc.setId(Integer.parseInt(circularId));
                                circularDesc.setHeading(heading);
                                circularDesc.setDetails(details);
                                circularDesc.setAttachment(attachment);
                                circularDesc.setCreated_at(created_at);
                                circularDesc.setUpdated_at(updated_at);

                                txtId.setText(String.valueOf(circularDesc.getId()));
                                txtHeading.setText(String.valueOf(circularDesc.getHeading()));
                                txtDetails.setText(String.valueOf(circularDesc.getDetails()));
                                //txtAttachment.setText(String.valueOf(Web_API_Config.root_image_url + circularDesc.getAttachment()));
                                txtCreatedAt.setText(String.valueOf(circularDesc.getCreated_at()));
                                txtUpdatedAt.setText(String.valueOf(circularDesc.getUpdated_at()));

                                if(circularDesc.getAttachment().equals("false"))
                                {
                                    txtAttachment.setVisibility(View.INVISIBLE);
                                }else
                                {
                                    txtAttachment.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                           // Picasso.with(getApplicationContext()).load(String.valueOf(Web_API_Config.root_image_url + circularDesc.getAttachment()));
                                            //String.valueOf(Web_API_Config.root_image_url + circularDesc.getAttachment());
                                            Log.e(TAG, "File Server Path : "+String.valueOf(Web_API_Config.root_image_url + circularDesc.getAttachment()));
                                            downloadFile(String.valueOf(Web_API_Config.root_image_url + circularDesc.getAttachment()));
                                        }
                                    });
                                }
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



    public void downloadFile(String file_server_path)
    {

            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(file_server_path);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            Long reference = downloadManager.enqueue(request);



    }

}
