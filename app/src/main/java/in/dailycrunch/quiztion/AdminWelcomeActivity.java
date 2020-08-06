package in.dailycrunch.quiztion;

/**
 * Created by Adarsh Sodagudi(025281709).
 * Mansi Koul(018761247)
 */


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/*Admins Dashboard*/
public class AdminWelcomeActivity extends AppCompatActivity {

    /*Handles the onclick events*/
    public void followadmin(View view)
    {
        int tag= Integer.parseInt(view.getTag().toString());
        Intent intent;
        if(tag==0)
        {
            //Transferring flow to Process of adding a new quiz
            intent=new Intent(getApplicationContext(),AdminAsksQuestionNo.class);
            startActivity(intent);
        }
        else if(tag==2)
        {
            //Admin Logout
            DataBaseHandler DBH=new DataBaseHandler(getApplicationContext());
            DBH.logout();
            intent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            //Displaying Leaderboard to admin
            intent=new Intent(getApplicationContext(),AdminLeaderboardActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_welcome);
    }
}
