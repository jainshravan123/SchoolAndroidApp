package infotech.jain.app.school.schoolapp.activity;



import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import cn.pedant.SweetAlert.SweetAlertDialog;
import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.bean.Student;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;
import infotech.jain.app.school.schoolapp.bean.User;
import infotech.jain.app.school.schoolapp.network.CheckInternetConnection;
import infotech.jain.app.school.schoolapp.session.SessionManager;
import infotech.jain.app.school.schoolapp.sqlite_db.DBHelper;
import infotech.jain.app.school.schoolapp.sqlite_db.FirebaseTokenStorage;

public class SignInActivity extends AppCompatActivity {

    TextView link_signup;
    Button   btn_login, sign_up_sub_btn;
    EditText editTxtUsername, editTxtPassword;
    SweetAlertDialog sAlertDialog, signUpAlertDialog, sign_up_sweet_dialog_success;
    EditText sign_up_name, sign_up_email, sign_up_password, sign_up_confirm_password, sign_up_contact_num, sign_up_adm_num;
    User sign_in_success_user;

    String TAG = "SignInActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hiding the action bar
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else{
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

        //Setting the content view
        setContentView(R.layout.activity_sign_in);

        //Getting Scroll View for Background Image
        ScrollView scrollView = (ScrollView) findViewById(R.id.sign_in_back_scroll_view);
        scrollView.setBackgroundResource(R.mipmap.back);

        //Getting Screen Size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;

        //Setting Linear Layout position
        LinearLayout sign_in_back_linear_layout = (LinearLayout) findViewById(R.id.sign_in_back_linear_layout);
        sign_in_back_linear_layout.setY((deviceHeight * 50)/100);


        link_signup     = (TextView) findViewById(R.id.link_signup);
        btn_login       = (Button)   findViewById(R.id.btn_login);
        editTxtUsername = (EditText) findViewById(R.id.input_email);
        editTxtPassword = (EditText) findViewById(R.id.input_password);

        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent sign_up_intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(sign_up_intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
                openSignUpDialog();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String username = editTxtUsername.getText().toString();
                String password = editTxtPassword.getText().toString();

                User sign_in_user = new User();
                sign_in_user.setUsername(username);
                sign_in_user.setPassword(password);

                //Checking Internet Connection for SignIn Activity
                CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
                checkInternetConnection.showNetworkIdentifier(getApplicationContext(), SignInActivity.this);


                if(checkInternetConnection.checkingInternetConnection(getApplicationContext()))
                {

                    if(checkSignInFields(sign_in_user)) {

                        sAlertDialog = new SweetAlertDialog(SignInActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        sAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        sAlertDialog.setTitleText("Authenticating...");
                        sAlertDialog.setCancelable(false);
                        sAlertDialog.show();

                        //calling function for signing process
                        signInProcess(sign_in_user);
                    }else{
                        String msg = checkSignInFieldsErrorMsg(sign_in_user);
                        new SweetAlertDialog(SignInActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(msg)
                                .show();
                    }
                }

            }
        });


    }


    //Sign In Process using Volley
    public void signInProcess(final User sign_in_user){

        String url = Web_API_Config.userLoginValidateAPI +"?Email="+sign_in_user.getUsername()+"&Password="+sign_in_user.getPassword();

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {

                            Log.e("SignIn Response : ", response.toString());
                            sAlertDialog.dismiss();
                            int status = Integer.parseInt(response.getString("Success").toString());

                            if(status==1){

                                String user_id       = response.getString("UserId");
                                String addmission_no = response.getString("Addmission_No");
                                String username      = response.getString("UserName");
                                String user_email    = response.getString("UserEmail");
                                String user_type     = response.getString("Type");
                                String s_id          = response.getString("SId");
                                String class_id      = response.getString("ClassId");
                                String sec_id        = response.getString("SectionId");
                                String t_id          = response.getString("TId");
                                String login_stats   = response.getString("LoginStatus");


                                Student student = new Student();
                                student.setStu_id(s_id);
                                student.setTeach_id(t_id);
                                student.setClass_id(class_id);
                                student.setSec_id(sec_id);

                                sign_in_success_user = new User();
                                sign_in_success_user.setUser_id(user_id);
                                sign_in_success_user.setAdmission_number(addmission_no);
                                sign_in_success_user.setUsername(user_email);
                                sign_in_success_user.setName(username);
                                sign_in_success_user.setUser_type(user_type);
                                sign_in_success_user.setLogin_status(login_stats);
                                sign_in_success_user.setStudent(student);

                                //Storing User Data into SQLite
                                DBHelper dbHelper = new DBHelper(getApplicationContext());
                                dbHelper.addUserData(sign_in_success_user);

                                FirebaseTokenStorage firebaseTokenStorage = new FirebaseTokenStorage(getApplicationContext());
                                if(firebaseTokenStorage.checkTokenExistence())
                                {
                                    Log.e(TAG, "Token : "+firebaseTokenStorage.getToken());
                                    //Updating UserId for firebase token
                                    updateIDForFirebaseToken();
                                }else{

                                    Log.e(TAG, "Token : " + "No Token");
                                    openDashboard();
                                }





                            }else{

                                //Getting Error Message
                                String errorMsg = response.getString("ErrorMsg");

                                //
                                new SweetAlertDialog(SignInActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText(errorMsg)
                                        .setContentText("Try Again")
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

    //Updating UserId for firebase notification token
    public void updateIDForFirebaseToken()
    {

        //Getting user id from SQLite
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String user_id    = dbHelper.getUserID();

        //Getting token from SQLite
        FirebaseTokenStorage firebaseTokenStorage = new FirebaseTokenStorage(getApplicationContext());
        String token = firebaseTokenStorage.getToken();

        //Getting Wifi Address
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo          = wifiManager.getConnectionInfo();
        String mac_id           = wInfo.getMacAddress();


        String url = Web_API_Config.update_user_id_fcm_notification;

        url = url + "?User_Id="+user_id+"&Token_Id="+token+"&Mac_Id="+mac_id;


        //Create JSONObjectRequest for Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        Log.e(TAG, "Update FCM Response : " + response.toString());

                        openDashboard();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);


    }

    public void  openDashboard()
    {
        //Set session
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.setLogin(true);

        //Start Dashboard Activity
        Intent dashboardIntent = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(dashboardIntent);
        finish();
    }
    //Opening SignUp Custom Dialog
    public void openSignUpDialog()
    {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(SignInActivity.this)
                .title("Sign Up")
                .titleColor(Color.BLACK)
                .customView(R.layout.custom_dialog_sign_up, true)
                .positiveText("Submit")
                .negativeText("Cancel")
                .positiveColorRes(R.color.black_color)
                .negativeColorRes(R.color.gray_btn_bg_color)
                .canceledOnTouchOutside(false)
                .autoDismiss(false);

        final MaterialDialog materialDialog = builder.build();
        materialDialog.show();

        View view1 = materialDialog.getCustomView();




        sign_up_name = (EditText) view1.findViewById(R.id.input_name);
        sign_up_email  = (EditText) view1.findViewById(R.id.input_email);
        sign_up_password = (EditText) view1.findViewById(R.id.input_password);
        sign_up_confirm_password = (EditText) view1.findViewById(R.id.input_confirm_password);
        sign_up_contact_num = (EditText) view1.findViewById(R.id.input_contact_number);
        sign_up_adm_num = (EditText) view1.findViewById(R.id.input_admission_number);
        sign_up_sub_btn = (Button) view1.findViewById(R.id.customButton1);

        sign_up_sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name = sign_up_name.getText().toString();
                String username = sign_up_email.getText().toString();
                String password = sign_up_password.getText().toString();
                String confirm_pwd = sign_up_confirm_password.getText().toString();
                String mobile_number = sign_up_contact_num.getText().toString();
                String admission_number = sign_up_adm_num.getText().toString();

                //Getting Wifi Address
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                WifiInfo wInfo          = wifiManager.getConnectionInfo();
                String macAddress       = wInfo.getMacAddress();

                User sign_up_user = new User();
                sign_up_user.setName(name);
                sign_up_user.setUsername(username);
                sign_up_user.setPassword(password);
                sign_up_user.setConfirm_password(confirm_pwd);
                sign_up_user.setMobile_number(mobile_number);
                sign_up_user.setAdmission_number(admission_number);
                sign_up_user.setMac_id(macAddress);


                //Checking Internet Connection
                CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
                checkInternetConnection.showNetworkIdentifier(getApplicationContext(),  SignInActivity.this);

                if(checkInternetConnection.checkingInternetConnection(getApplicationContext()))
                {

                    if (checkSignUpFields(sign_up_user)) {

                        if (checkPasswordEquality(sign_up_user)) {

                            signUpAlertDialog = new SweetAlertDialog(SignInActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                            signUpAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            signUpAlertDialog.setTitleText("Authenticating...");
                            signUpAlertDialog.setCancelable(false);
                            signUpAlertDialog.show();

                            //calling function for signing process
                            signUpProcess(sign_up_user);
                        } else {
                            new SweetAlertDialog(SignInActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Wrong Password")
                                    .setContentText("Confirm Password must same as Password")
                                    .show();
                        }
                    } else {

                        String msg = checkSignUpFieldsErrorMsg(sign_up_user);
                        new SweetAlertDialog(SignInActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(msg)
                                .show();
                    }
                }

            }
        });

        View positive  = materialDialog.getActionButton(DialogAction.POSITIVE);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Submit Clicked", Toast.LENGTH_LONG).show();



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


    //Sign Up Process using Volley Library
    public void signUpProcess(User sign_up_user){

        //String url = Web_API_Config.userLoginValidateAPI +"?Email="+sign_in_user.getUsername()+"&Password="+sign_in_user.getPassword();

        String url = Web_API_Config.userSignUpAPI+"?Email="+sign_up_user.getUsername()+"&Password="+sign_up_user.getPassword()+"&Name="+sign_up_user.getName()+"&Mobile="+sign_up_user.getMobile_number()+"&Addmission_No="+sign_up_user.getAdmission_number()+"&Mac_Id="+sign_up_user.getMac_id();
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            signUpAlertDialog.dismiss();
                            int status = Integer.parseInt(response.getString("Success").toString());

                            if(status==1){

                                String user_id     = response.getString("UserId");
                                String username    = response.getString("UserName");
                                String user_type   = response.getString("Type");
                                String s_id        = response.getString("SId");
                                String t_id        = response.getString("TId");
                                String login_stats = response.getString("LoginStatus");


                                //Start Dashboard Activity
                                Intent dashboardIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                                startActivity(dashboardIntent);

                                sign_up_sweet_dialog_success = new SweetAlertDialog(SignInActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                sign_up_sweet_dialog_success.setTitleText("You Have Successfully Registered");
                                sign_up_sweet_dialog_success.setContentText("Login With Your Credentials");
                                sign_up_sweet_dialog_success.show();

                                sign_up_sweet_dialog_success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Intent dashboardIntent = new Intent(getApplicationContext(), SignInActivity.class);
                                        startActivity(dashboardIntent);
                                    }
                                });

                            }else{

                                //Getting Error Message
                                String errorMsg = response.getString("ErrorMsg");

                                new SweetAlertDialog(SignInActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Try Again")
                                        .setContentText(errorMsg)
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





    //Checking Empty Fields Error message in Signin Activity
    public String checkSignInFieldsErrorMsg(User user){

        String msg = "";
        if((user.getUsername().equals("")) && (user.getPassword().equals(""))){
            msg = "Please fill both of the fields";
        }
        else if((user.getUsername().equals(""))){
            msg = "Username is empty";
        }
        else if((user.getPassword().equals(""))){
            msg = "Password is empty";
        }

        return msg;
    }

    //Checking Whether the SignIn fields are empty or not
    public boolean checkSignInFields(User user){
      if(user.getUsername().equals("") || user.getPassword().equals(""))
      {
            return false;
        }
        return true;
    }


    //Checking Empty Fields Error Message in Sign Up Activity
    public String checkSignUpFieldsErrorMsg(User user){

        String msg = "";

        if(user.getName().equals("") && user.getUsername().equals("") && user.getPassword().equals("") && user.getConfirm_password().equals("") && user.getMobile_number().equals("") && user.getAdmission_number().equals("")){
            msg = "Please fill all of the fields";
        }
        else if(user.getName().equals("")){
            msg = "Name is empty";
        }else if(user.getUsername().equals("")){
            msg = "Username is empty";
        }
        else if(user.getPassword().equals("")){
            msg = "Password is empty";
        }
        else if(user.getConfirm_password().equals("")){
            msg = "Confirm Password is empty";
        }
        else if(user.getMobile_number().equals("")){
            msg = "Mobile Number is empty";
        }else if(user.getAdmission_number().equals("")){
            msg = "Admission Number is empty";
        }
        return msg;
    }

    //Checking Whether the SignUp fields are empty or not
    public boolean checkSignUpFields(User user){
        if(user.getName().equals("") || user.getUsername().equals("") || user.getPassword().equals("") || user.getConfirm_password().equals("") || user.getMobile_number().equals("") || user.getAdmission_number().equals(""))
        {
            return false;
        }
        return true;
    }

    //Checking Whether Password and Confirm Password are same or not
    public boolean checkPasswordEquality(User user){
        if(user.getPassword().equals(user.getConfirm_password())){
            return true;
        }
        return false;
    }

}
