package in.dailycrunch.quiztion;

/**
 * Created by Adarsh Sodagudi(025281709).
 * Mansi Koul(018761247)
 */


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;


//Activity to Display SplashScreen
public class SplashScreenActivity extends AppCompatActivity {
    private String USERID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        DataBaseHandler DBH=new DataBaseHandler(getApplicationContext());
        USERID=DBH.getId();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        /*validates whether a user is logged in or not and sends him to the specific activity*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!USERID.equals("Failed"))
                {
                    if(USERID.equals("1"))
                    {
                        Intent intent= new Intent(getApplicationContext(),AdminWelcomeActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }

                }
                else
                {
                    Intent intent= new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        },3000);
    }
}
