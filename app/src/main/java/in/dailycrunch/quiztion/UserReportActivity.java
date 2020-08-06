package in.dailycrunch.quiztion;

/**
 * Created by Adarsh Sodagudi(025281709).
 * Mansi Koul(018761247)
 */


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

//Class to Display User Report
public class UserReportActivity extends AppCompatActivity {

    private TextView score;
    private TextView quiz;
    ArrayList<String> ANSWERS;
    private String QUIZ;
    private String URL;
    private String USERID;
    private String RESPONSE;
    DataBaseHandler DBH;

    /*goes to the Discover activity*/
    public void gotohome(View view)
    {
        Intent intent =new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void gotoanswers(View view)
    {   //Transfer flow to AnswerSheetActivity that display all correct answers for the quiz
        Intent intent = new Intent(this,AnswerSheetActivity.class);
        intent.putExtra("ANSWERS",ANSWERS);
        intent.putExtra("QUIZ",QUIZ);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report);
        DBH=new DataBaseHandler(getApplicationContext());
       // Get user id
        USERID=DBH.getId();
        //Read user score and particular quiz id
        score=findViewById(R.id.scoreid);
        quiz=findViewById(R.id.quizid);
        Intent intent=this.getIntent();
        String Score=intent.getStringExtra("SCORE");
        ANSWERS=intent.getStringArrayListExtra("ANSWERS");
        QUIZ=intent.getStringExtra("QUIZ");

        score.setText("Score: "+Score+"%");
        quiz.setText("Quiz: "+QUIZ);
        //URL to display user report by appending user id and score
        URL="http://dailycrunch.in/quiztion/report.php?userid="+USERID+"&score="+Score+"&quizname="+QUIZ;
        new GetQuizList().execute();
    }

    private class GetQuizList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(URL);


            if (jsonStr != null) {
                try {
                    /*Parses the JSON required to Spinner 1*/
                    JSONArray jsonarray=new JSONArray(jsonStr);
                    RESPONSE= jsonarray.getString(0);
                }
                catch (final JSONException e) {
                    Log.e("PARSING ERROR", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            else {
                Log.e("SERVER CONNECTION ERROR", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(RESPONSE.equals("0"))
            {
                Toast.makeText(getApplicationContext(),"Failed to save result",Toast.LENGTH_SHORT).show();
            }
            else if(RESPONSE.equals("2"))
            {
                Toast.makeText(getApplicationContext(),"Only first attempt score is considered",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Score Captured",Toast.LENGTH_SHORT).show();
            }

        }

    }
}
