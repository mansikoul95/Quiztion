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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

/*admins sets question in the quiz, one at a time*/
public class AdminPaperSetActivity extends AppCompatActivity {

    private String TOPIC;
    private String QUIZNAME;
    private String NOOFQ;
    private int CURRENTQNO;
    private String RESPONSE;
    private String URL;
    private TextView psqno;
    private EditText psq;
    private EditText psch1;
    private EditText psch2;
    private EditText psch3;
    private EditText psch4;
    private EditText psa;
    private String gpsq;
    private String gpsch1;
    private String gpsch2;
    private String gpsch3;
    private String gpsch4;
    private String gpsa;

    /*Uploads the question to the database*/
    public void setquestion(View view)
    {
        int qno=Integer.parseInt(psqno.getText().toString().split(" ")[1]);//headerqno
        CURRENTQNO=qno;
        gpsq=psq.getText().toString();
        gpsch1=psch1.getText().toString();
        gpsch2=psch2.getText().toString();
        gpsch3=psch3.getText().toString();
        gpsch4=psch4.getText().toString();
        gpsa=psa.getText().toString();
        if(!(CURRENTQNO>Integer.parseInt(NOOFQ)))
        {
            //URL to input topic,quizname,question, options and correct answer into the database
            URL="http://dailycrunch.in/quiztion/questionbank.php?topic="+TOPIC+"&quizname="+QUIZNAME+"&question="+gpsq+"&choice1="+gpsch1+"&choice2="+gpsch2+"&choice3="+gpsch3+"&choice4="+gpsch4+"&answer="+gpsa;
            new GetQuizList().execute();
            if(CURRENTQNO>=Integer.parseInt(NOOFQ))
            {
                Intent intent = new Intent(getApplicationContext(),AdminWelcomeActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                qno++;
                psqno.setText("Question "+qno);
            }
        }
        else
        {

        }
        /*after saving the question*/
        psq.setText("");
        psch1.setText("");
        psch2.setText("");
        psch3.setText("");
        psch4.setText("");
        psa.setText("");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_paper_set);
        Intent intent=this.getIntent();
        //Accepting values from AdminAskQuestionNo Activity
        TOPIC=intent.getStringExtra("topic");
        QUIZNAME=intent.getStringExtra("quiz");
        NOOFQ=intent.getStringExtra("noofq");
        //Reading values from EditText for question,options and correct answers
        psqno=findViewById(R.id.psqno);
        psq=findViewById(R.id.psq);
        psch1=findViewById(R.id.psch1);
        psch2=findViewById(R.id.psch2);
        psch3=findViewById(R.id.psch3);
        psch4=findViewById(R.id.psch4);
        psa=findViewById(R.id.psa);


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
                Toast.makeText(getApplicationContext(),"Try Again!",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Question has been recorded",Toast.LENGTH_SHORT).show();
            }

        }

    }
}
