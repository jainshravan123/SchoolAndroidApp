package infotech.jain.app.school.schoolapp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.session.SessionManager;

public class SplashActivity extends AppCompatActivity {

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

        //setting the content view
        setContentView(R.layout.activity_splash);

        //Counter for holding splash screens
        new CountDownTimer(3000,1000) {

            @Override
            public void onFinish() {


                SessionManager sessionManager = new SessionManager(getApplicationContext());

                //Checking Session
                if(sessionManager.isLoggedIn()){

                    //Opening Dashboard
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    //Opening User SignIn Activity
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(intent);
                    finish();
                }

            }


            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();


    }
}
