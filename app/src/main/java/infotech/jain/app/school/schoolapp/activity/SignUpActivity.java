package infotech.jain.app.school.schoolapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import infotech.jain.app.school.schoolapp.R;

public class SignUpActivity extends AppCompatActivity {

    TextView link_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        //Getting Scroll View for Background Image
        ScrollView scrollView = (ScrollView) findViewById(R.id.sign_up_back_scroll_view);
        scrollView.setBackgroundResource(R.mipmap.sign_up_back);

        //Getting Screen Size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;

        //Setting Linear Layout position
        LinearLayout sign_up_back_linear_layout = (LinearLayout) findViewById(R.id.sign_up_back_linear_layout);
        /*sign_up_back_linear_layout.setBackgroundColor(Color.GRAY);*/
        sign_up_back_linear_layout.getBackground().setAlpha(127);
        sign_up_back_linear_layout.setY((deviceHeight * 10) / 100);

        link_signin = (TextView) findViewById(R.id.link_signin);
        link_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }
}
