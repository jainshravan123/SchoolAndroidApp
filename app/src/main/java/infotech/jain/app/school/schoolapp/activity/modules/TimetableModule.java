package infotech.jain.app.school.schoolapp.activity.modules;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import cn.pedant.SweetAlert.SweetAlertDialog;
import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.activity.modules.sub_modules.HomeWorkListActivity;
import infotech.jain.app.school.schoolapp.activity.modules.sub_modules.HomeworkDescActivity;
import infotech.jain.app.school.schoolapp.adapter.GalleryCategoryAdapter;
import infotech.jain.app.school.schoolapp.bean.GalleryCategoryImages;
import infotech.jain.app.school.schoolapp.bean.Timetable;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;
import infotech.jain.app.school.schoolapp.sqlite_db.DBHelper;

public class TimetableModule extends AppCompatActivity {


    private ProgressDialog  progressDialog;
    private ProgressBar     progressBar;
    private Toolbar         toolbar;
    private CardView        cardView, cardView2;
    private ImageView       image_view_timetable, image_view_timetable_under_card_view;
    private TextView        txt_timetable_heading;
    private AppCompatButton btn_timetable_download;
    private Timetable       timetable;
    private String          absolute_path = "";
    private String          TAG           = "TimetableModule";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_module);

        cardView                             = (CardView)  findViewById(R.id.cv);
        cardView2                            = (CardView)  findViewById(R.id.cv2);
        image_view_timetable                 = (ImageView) findViewById(R.id.image_view_timetable);
        image_view_timetable_under_card_view = (ImageView) findViewById(R.id.imgTimetable);
        txt_timetable_heading                = (TextView)  findViewById(R.id.txt_timetable_heading);
        btn_timetable_download               = (AppCompatButton) findViewById(R.id.btn_timetable_download);
        progressBar                          = (ProgressBar) findViewById(R.id.prgBar);



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
        getSupportActionBar().setTitle("Timetable");

        getTimetableDetail();
    }


    public void getTimetableDetail()
    {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String url = Web_API_Config.timetable_API + dbHelper.getSectionId();

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressBar.setVisibility(View.GONE);
                        cardView.setVisibility(View.VISIBLE);
                        cardView2.setVisibility(View.VISIBLE);


                        try {
                            String success = response.getString("Success");

                            if(success.equals("1"))
                            {
                                String timeTableImage = response.getString("Time_Table_Image");
                                timetable = new Timetable();
                                timetable.setTimetable(timeTableImage);

                                showViews();
                            }else{
                                new SweetAlertDialog(TimetableModule.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("No Timetable")
                                        .setContentText("Please Contact To Admin")
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

    public void showViews()
    {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .rect();
        TextDrawable txtDrawable = builder.build("T", color1);
        image_view_timetable.setImageDrawable(txtDrawable);

        Picasso.with(getApplicationContext()).load(Web_API_Config.root_image_url + timetable.getTimetable()).into(image_view_timetable_under_card_view);

        btn_timetable_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReadWritePermissison();
            }
        });

    }

    public void getReadWritePermissison()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            }
        }else{
            new DownloadFileFromURL().execute(Web_API_Config.root_image_url + timetable.getTimetable());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Log.e("Permission", "Granted");
                    new DownloadFileFromURL().execute(Web_API_Config.root_image_url + timetable.getTimetable());

                } else
                {
                    finish();
                    Log.e("Permission", "Denied");
                }
                return;
            }
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                //URL url = new URL(f_url[0]);
                URL url   = new URL(Web_API_Config.root_image_url + timetable.getTimetable());
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String url1 = Web_API_Config.root_image_url +  timetable.getTimetable();
                String fileName = url1.substring(url1.lastIndexOf('/') + 1);


                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                // Output stream to write file
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();
                absolute_path = file.getAbsolutePath();
                Log.e(TAG, "Absolute Path : " + file.getAbsolutePath());
                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String file_url)
        {

            dismissProgressDialog();
            documentDialogSuccess();

        }

    }


    public void showProgressDialog()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading file. Please wait...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public void dismissProgressDialog()
    {
        progressDialog.dismiss();
    }

    public void documentDialogSuccess()
    {
        SweetAlertDialog apply_leave_success_dialog;
        apply_leave_success_dialog = new SweetAlertDialog(TimetableModule.this, SweetAlertDialog.SUCCESS_TYPE);
        apply_leave_success_dialog.setTitleText("Timetable Downloaded Successfully.");
        apply_leave_success_dialog.setContentText("Timetable Path : "+absolute_path);
        apply_leave_success_dialog.show();

    }

}
