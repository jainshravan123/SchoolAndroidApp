package infotech.jain.app.school.schoolapp.activity.nav_drawer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.activity.DashboardActivity;
import infotech.jain.app.school.schoolapp.activity.SignInActivity;
import infotech.jain.app.school.schoolapp.bean.UserProfile;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;
import infotech.jain.app.school.schoolapp.sqlite_db.DBHelper;

public class Profile extends AppCompatActivity {

    private AppBarLayout app_bar;
    private NestedScrollView nested_scroll;
    private ProgressBar prgBar1;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    ImageView updatedImageView, profile_back;
    int PICK_IMAGE_CODE = 1001;
    Bitmap bitmap;
    Button changeImageBtn;

    UserProfile userProfile;
    CircleImageView userProfilePhoto;


    private TextView txt_father_name, txt_mother_name, txt_father_mobile, txt_mother_mobile, txt_work_email, txt_home_address, txt_school_address, txt_addmission_no;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        app_bar = (AppBarLayout) findViewById(R.id.app_bar);
        profile_back = (ImageView) findViewById(R.id.profile_back);
        nested_scroll = (NestedScrollView) findViewById(R.id.nested_scroll);
        userProfilePhoto = (CircleImageView) findViewById(R.id.user_profile_photo);
        prgBar1 = (ProgressBar) findViewById(R.id.prgBar1);

        txt_father_name    = (TextView) findViewById(R.id.txt_father_name);
        txt_mother_name    = (TextView) findViewById(R.id.txt_mother_name);
        txt_father_mobile  = (TextView) findViewById(R.id.txt_father_mobile);
        txt_mother_mobile  = (TextView) findViewById(R.id.txt_mother_mobile);
        txt_work_email     = (TextView) findViewById(R.id.txt_work_email);
        txt_home_address   = (TextView) findViewById(R.id.txt_home_address);
        txt_school_address = (TextView) findViewById(R.id.txt_school_address);
        txt_addmission_no  = (TextView) findViewById(R.id.txt_addmission_no);



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





        userProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfilePicDialog();
            }
        });

        getProfileData();
    }


    //Getting Profile Data
    public void getProfileData()
    {
        DBHelper dbHelper = new DBHelper(getApplicationContext());

        final String addmission_no = dbHelper.getAdmissionNo();
        String url    = Web_API_Config.userProfile + addmission_no;

        Log.e("Profile URI : ", url);
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!

                        prgBar1.setVisibility(View.GONE);
                        app_bar.setVisibility(View.VISIBLE);
                        nested_scroll.setVisibility(View.VISIBLE);
                        userProfilePhoto.setVisibility(View.VISIBLE);


                        try {

                            String name          = response.getString("Name");
                            String father_name   = response.getString("Father_Name");
                            String mother_name   = response.getString("Mother_Name");
                            String father_mobile = response.getString("Father_Mobile");
                            String mother_mobile = response.getString("Mother_Mobile");
                            String email         = response.getString("Email");
                            String home_address  = response.getString("Address");
                            String addmission_no = response.getString("Addmission_No");
                            String photo         = response.getString("Photo");

                            userProfile = new UserProfile();

                            userProfile.setName(name);
                            userProfile.setFathers_name(father_name);
                            userProfile.setMothers_name(mother_name);
                            userProfile.setFathers_mobile(father_mobile);
                            userProfile.setMothers_mobile(mother_mobile);
                            userProfile.setEmail(email);
                            userProfile.setHome_address(home_address);
                            userProfile.setAdmission_id(addmission_no);
                            userProfile.setPhoto(photo);
                            userProfile.setSchool_address("St. John School, Sec 7");

                            showProfileInformation();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                        Log.e("Profile Data : ", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);

    }

    public void showProfileInformation()
    {

        collapsingToolbarLayout.setTitle(userProfile.getName());
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);
        txt_father_name.setText(userProfile.getFathers_name());
        txt_mother_name.setText(userProfile.getMothers_name());
        txt_father_mobile.setText(userProfile.getFathers_mobile());
        txt_mother_mobile.setText(userProfile.getMothers_mobile());
        txt_work_email.setText(userProfile.getEmail());
        txt_home_address.setText(userProfile.getHome_address());
        txt_school_address.setText(userProfile.getSchool_address());
        txt_addmission_no.setText(userProfile.getAdmission_id());
        Log.e("Profile Image URL : ", Web_API_Config.root_image_url + userProfile.getPhoto());
        Picasso.with(getApplicationContext()).load(Web_API_Config.root_image_url + userProfile.getPhoto()).into(userProfilePhoto);


    }

    //Opening Profile Picture Dialog
    public void openProfilePicDialog()
    {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(Profile.this)
                .title("Profile Picture")
                .titleColor(Color.BLACK)
                .customView(R.layout.profile_pic_dialog, true)
                .negativeText("Cancel")
                .positiveColorRes(R.color.black_color)
                .negativeColorRes(R.color.gray_btn_bg_color)
                .canceledOnTouchOutside(false)
                .autoDismiss(false);

        final MaterialDialog materialDialog = builder.build();
        materialDialog.show();

        View view1 = materialDialog.getCustomView();

        CircleImageView imageView      = (CircleImageView) view1.findViewById(R.id.profilePicImageView);
        Button changeImageBtn = (Button) view1.findViewById(R.id.profilePicChangeBtn);

        if(userProfile.getPhoto().equals("NA") || userProfile.getPhoto().equals(""))
        {
            imageView.setImageResource(R.drawable.student);
        }
        else
        {
            Picasso.with(getApplicationContext()).load(Web_API_Config.root_image_url + userProfile.getPhoto()).into(imageView);

        }




        View negative  = materialDialog.getActionButton(DialogAction.NEGATIVE);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });


        changeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
                showFileChooser();
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                openUpdatedProfilePivDialog();
                updatedImageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openUpdatedProfilePivDialog(){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(Profile.this)
                .title("New Picture")
                .titleColor(Color.BLACK)
                .customView(R.layout.profile_pic_update_dialog, true)
                .negativeText("Cancel")
                .positiveColorRes(R.color.black_color)
                .negativeColorRes(R.color.gray_btn_bg_color)
                .canceledOnTouchOutside(false)
                .autoDismiss(false);

        final MaterialDialog materialDialog = builder.build();
        materialDialog.show();

        View view1 = materialDialog.getCustomView();

        updatedImageView         = (ImageView) view1.findViewById(R.id.updatedProfilePicImageView);
        changeImageBtn = (Button) view1.findViewById(R.id.profilePicUpdateBtn);

        changeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        View negative  = materialDialog.getActionButton(DialogAction.NEGATIVE);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
    }

}
