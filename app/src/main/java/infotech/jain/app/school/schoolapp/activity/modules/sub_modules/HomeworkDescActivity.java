package infotech.jain.app.school.schoolapp.activity.modules.sub_modules;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
import infotech.jain.app.school.schoolapp.activity.modules.LeaveModule;
import infotech.jain.app.school.schoolapp.bean.Homework;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;

public class HomeworkDescActivity extends AppCompatActivity {


    ProgressBar progressBar;
    LinearLayout linearLayout1, linearLayout2,linearLayout3, linearLayout4;
    TextView txt_homework_caption, txt_homework_details, txt_start_date, txt_end_date, txt_subject_type;
    Button btn_view_attachment, btn_download_attachment;
    ProgressDialog progressDialog;
    String TAG       = "HomeworkDescActivity";
    String absolute_path = "";

    Toolbar toolbar;
    String homework_id;
    Homework homework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_desc);


        linearLayout1           = (LinearLayout) findViewById(R.id.linearLayout1);
        linearLayout2           = (LinearLayout) findViewById(R.id.linearLayout2);
        linearLayout3           = (LinearLayout) findViewById(R.id.linearLayout3);
        linearLayout4           = (LinearLayout) findViewById(R.id.linearLayout4);
        progressBar             = (ProgressBar) findViewById(R.id.prgBar);
        txt_homework_caption    = (TextView) findViewById(R.id.txt_homework_caption);
        txt_homework_details    = (TextView) findViewById(R.id.txt_homework_details);
        txt_start_date          = (TextView) findViewById(R.id.txt_created_at);
        txt_end_date            = (TextView) findViewById(R.id.txt_end_date);
        txt_subject_type        = (TextView) findViewById(R.id.txt_subject_type);
        btn_view_attachment     = (Button) findViewById(R.id.btn_view_attachment);
        btn_download_attachment = (Button) findViewById(R.id.btn_download_attachment);

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
        getSupportActionBar().setTitle("Description");


        Intent intent1 = getIntent();
        homework_id = intent1.getStringExtra("homework_id");

        getHomeworkDescription();
    }


    public void getHomeworkDescription()
    {
        String url = Web_API_Config.homework_desc_API + homework_id;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        showViewsAndHideProgressBar();
                        homework = new Homework();

                        try {
                            JSONObject jsonObject = (JSONObject) response.get(0);
                            homework.setSubject_id(jsonObject.getString("Subject_Id"));
                            homework.setCaption(jsonObject.getString("Homework_Caption"));
                            homework.setDetails(jsonObject.getString("Detail"));
                            homework.setAttachment(jsonObject.getString("Attachment"));
                            homework.setType(jsonObject.getString("Type"));
                            homework.setStart_date(jsonObject.getString("Start_Date"));
                            homework.setEnd_date(jsonObject.getString("End_Date"));
                            homework.setStatus(jsonObject.getString("Status"));
                            homework.setMsg(jsonObject.getString("Msg"));


                            setData();
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


        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    public void showViewsAndHideProgressBar()
    {
        progressBar.setVisibility(View.GONE);
        linearLayout1.setVisibility(View.VISIBLE);
        linearLayout2.setVisibility(View.VISIBLE);
        linearLayout3.setVisibility(View.VISIBLE);
        linearLayout4.setVisibility(View.VISIBLE);

    }

    public void setData()
    {
        txt_homework_caption.setText(homework.getCaption());
        txt_homework_details.setText(homework.getDetails());
        if(homework.getStart_date().equals(""))
        {
            txt_start_date.setText("NA");
        } else {
            txt_start_date.setText(homework.getStart_date());
        }
        if(homework.getEnd_date().equals("")){
            txt_end_date.setText("NA");
        }else{
            txt_end_date.setText(homework.getEnd_date());
        }
        txt_subject_type.setText(homework.getType());
        setbButtonActionListener();
    }

    public void setbButtonActionListener()
    {
        btn_view_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAttachment();
            }
        });

        btn_download_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReadWritePermissison();
                //new DownloadFileFromURL().execute(Web_API_Config.root_image_url + leaveDescription.getLeave().getAttached_doc_path());
            }
        });
    }

    public void showAttachment()
    {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(HomeworkDescActivity.this)
                .title(homework.getCaption())
                .titleColor(Color.BLACK)
                .customView(R.layout.leave_attachment_dialog_layout, true)
                .negativeText("Cancel")
                .negativeColorRes(R.color.gray_btn_bg_color)
                .canceledOnTouchOutside(false)
                .autoDismiss(false);


        final MaterialDialog materialDialog = builder.build();
        materialDialog.show();

        View view1 = materialDialog.getCustomView();

        ImageView imageView      = (ImageView) view1.findViewById(R.id.attachmentImageView);



        Picasso.with(getApplicationContext()).load(Web_API_Config.root_image_url + homework.getAttachment()).into(imageView);

        View negative  = materialDialog.getActionButton(DialogAction.NEGATIVE);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });

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
                URL url   = new URL(Web_API_Config.root_image_url + homework.getAttachment());
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String url1 = Web_API_Config.root_image_url +  homework.getAttachment();
                String fileName = url1.substring(url1.lastIndexOf('/') + 1);

                // File file = new File(Environment.getExternalStorageDirectory(), "downloadedfile.jpg");
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

    public void getReadWritePermissison()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            }
        }else{
            new DownloadFileFromURL().execute(Web_API_Config.root_image_url +  homework.getAttachment());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Log.e("Permission", "Granted");
                    new DownloadFileFromURL().execute(Web_API_Config.root_image_url +  homework.getAttachment());

                } else {
                    Intent intent1 = new Intent(getApplicationContext(), LeaveModule.class);
                    startActivity(intent1);
                    finish();
                    Log.e("Permission", "Denied");
                }
                return;
            }
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
        apply_leave_success_dialog = new SweetAlertDialog(HomeworkDescActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        apply_leave_success_dialog.setTitleText("Document Downloaded Successfully.");
        apply_leave_success_dialog.setContentText("Doc Path : "+absolute_path);
        apply_leave_success_dialog.show();

    }
}
