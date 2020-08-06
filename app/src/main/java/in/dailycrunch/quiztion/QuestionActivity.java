package in.dailycrunch.quiztion;

/**
 * Created by Adarsh Sodagudi(025281709).
 * Mansi Koul(018761247)
 */


import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/*Retrieves the question bank of the particular quiz and displays in it randomly one after another*/
public class QuestionActivity extends AppCompatActivity {

    private TextView header;
    private TextView question;
    private TextView choice1;
    private TextView choice2;
    private TextView choice3;
    private TextView choice4;
    private TextView progressqno;
    private ProgressBar progressbar;
    private int PASTPROGRESS;
    private int NOOFQ;
    //private int percentInc;
    private String DISCOVER;
    private String QUIZLIST;
    private int SCORE;
    private String URL;
    private ArrayList<QuestionData> qdata=new ArrayList<QuestionData>();
    private ArrayList<String> useranswers=new ArrayList<>();
    private ArrayList<String> correctanswers=new ArrayList<>();

    private int shuffle[];


    public void selectchoice(View view) throws InterruptedException {
        String clicked=view.getTag().toString();
        useranswers.add(clicked);
        int qno=Integer.parseInt(header.getText().toString().split(" ")[1]);//headerqno
        int pqno=Integer.parseInt(progressqno.getText().toString().split("/")[0]);//progressqnonumerator
        int totalqno=Integer.parseInt(progressqno.getText().toString().split("/")[1]);//progressqnodenominator
        if(correctanswers.get(qno-1).equals(useranswers.get(qno-1)))
        {
            SCORE++;
          }
        else
        {
            /*Wrong Answer Block*/
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
            /*vibration code*/

        }
        if(pqno>=totalqno)
        {
            int ScorePercentage=(int)Math.floor((SCORE/(NOOFQ*1.0))*100);
            //Toast.makeText(getApplicationContext(),SCORE+" "+ScorePercentage,Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getApplicationContext(),UserReportActivity.class);
            intent.putExtra("SCORE",ScorePercentage+"");
            intent.putExtra("ANSWERS",useranswers);
            intent.putExtra("QUIZ",QUIZLIST);
            startActivity(intent);
            finish();
        }
        else
        {
            /*header*/
            qno++;
            header.setText("Question "+qno);
            /*header*/
            /*set question and choice*/
            QuestionData temp=qdata.get(shuffle[qno-1]);
            question.setText(temp.getQUESTION());
            choice1.setText(temp.getCHOICE1());
            choice2.setText(temp.getCHOICE2());
            choice3.setText(temp.getCHOICE3());
            choice4.setText(temp.getCHOICE4());
            /*set question and choice*/
            /*progress*/
            pqno++;
            progressqno.setText(pqno+"/"+totalqno);
            /*progress*/
            /*progress bar*/
            int percentInc = (int) Math.ceil((1 / (NOOFQ * 1.0) * 100));
            percentInc=percentInc*qno*10;
            ObjectAnimator.ofInt(progressbar, "progress", PASTPROGRESS,percentInc).start();
            PASTPROGRESS=percentInc;
            //progressbar.setProgress(percentInc);
            /*progress bar*/
        }
    }

    public void setQuestion1()
    {
        QuestionData temp=qdata.get(shuffle[0]);
        header.setText("Question 1");
        question.setText(temp.getQUESTION());
        choice1.setText(temp.getCHOICE1());
        choice2.setText(temp.getCHOICE2());
        choice3.setText(temp.getCHOICE3());
        choice4.setText(temp.getCHOICE4());
        progressqno.setText("1/"+NOOFQ);
        /*progress bar*/
        int percentInc = (int) Math.ceil((1 / (NOOFQ * 1.0) * 100));
        percentInc*=10;
        PASTPROGRESS=percentInc;
        //progressbar.setProgress(percentInc);
        ObjectAnimator.ofInt(progressbar, "progress", 0,percentInc).start();
        /*progress bar*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        header=findViewById(R.id.questionno);
        question=findViewById(R.id.question);
        choice1=findViewById(R.id.choice1);
        choice2=findViewById(R.id.choice2);
        choice3=findViewById(R.id.choice3);
        choice4=findViewById(R.id.choice4);
        progressqno=findViewById(R.id.progressqno);
        progressbar=findViewById(R.id.progressbar);
        Intent intent=this.getIntent();
        DISCOVER=intent.getStringExtra("DISCOVER");
        QUIZLIST=intent.getStringExtra("QUIZLIST");
        //Displaying quizList in a Category
        URL="http://dailycrunch.in/quiztion/question.php?quizname="+QUIZLIST;
        Log.e("URL QUIZ",URL);
        new GetQuizList().execute();
    }

    /*shuffle function*/
    public static int[] ShuffleArray(int a, int b){
        Random rgen = new Random();  // Random number generator
        b=b-1;
        int size = b-a+1;
        int[] array = new int[size];

        /*inserting values*/
        for(int i=0; i< size; i++){
            array[i] = a+i;
        }
        /*swapping*/
        for (int i=0; i<array.length; i++) {
            int randomPosition = rgen.nextInt(array.length);
            int temp = array[i];
            array[i] = array[randomPosition];
            array[randomPosition] = temp;
        }
        for(int s: array)
            Log.e("Shuffle",s+"");
        return array;
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
                    int n=jsonarray.length();
                    NOOFQ=n;//total no of questions
                    shuffle=ShuffleArray(0,NOOFQ);
                    for(int i=0;i<n;i++)
                    {
                        JSONArray jsonarray1=jsonarray.getJSONArray(i);
                        String q=jsonarray1.getString(0);
                        String c1=jsonarray1.getString(1);
                        String c2=jsonarray1.getString(2);
                        String c3=jsonarray1.getString(3);
                        String c4=jsonarray1.getString(4);
                        String a=jsonarray1.getString(5);
                        correctanswers.add(a);
                        QuestionData temp=new QuestionData(q,c1,c2,c3,c4,a);
                        qdata.add(temp);
                    }
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
            setQuestion1();
        }

    }
}
