package in.dailycrunch.quiztion;


/**
 * Created by Adarsh Sodagudi(025281709).
 * Mansi Koul(018761247)
 */
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

/*Displays the answer sheet*/
public class AnswerSheetActivity extends AppCompatActivity {

    private ArrayList<QuestionData> qdata=new ArrayList<QuestionData>();
    ArrayList<String> ANSWERS;
    LinearLayout linearlayout;
    private int NOOFQ;
    private String QUIZ;
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_sheet);
        Intent intent=this.getIntent();
        linearlayout=(LinearLayout)findViewById(R.id.llmain);
        ANSWERS=intent.getStringArrayListExtra("ANSWERS");
        QUIZ=intent.getStringExtra("QUIZ");
        URL="http://dailycrunch.in/quiztion/answers.php?quizname="+QUIZ;
        new GetQuizList().execute();
    }

    public void setAnswers()
    {
        for(int i=0;i<NOOFQ;i++)
        {
            String useranswer,correctanswer;
            QuestionData temp=qdata.get(i);
            TextView textView1 = new TextView(this);
            textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            /*user asnwers block*/
            if(ANSWERS.get(i).equals("A"))
            {
                useranswer=temp.getCHOICE1();
            }
            else if(ANSWERS.get(i).equals("B"))
            {
                useranswer=temp.getCHOICE2();
            }
            else if(ANSWERS.get(i).equals("C"))
            {
                useranswer=temp.getCHOICE3();
            }
            else
            {
                useranswer=temp.getCHOICE4();
            }
            /*correct answers block*/
            if(temp.getANSWER().equals("A"))
            {
                correctanswer=temp.getCHOICE1();
            }
            else if(temp.getANSWER().equals("B"))
            {
                correctanswer=temp.getCHOICE2();
            }
            else if(temp.getANSWER().equals("C"))
            {
                correctanswer=temp.getCHOICE3();
            }
            else
            {
                correctanswer=temp.getCHOICE4();
            }

            textView1.setText((i+1)+") "+temp.getQUESTION());
            textView1.setTextSize(18);
            textView1.setTypeface(textView1.getTypeface(), Typeface.BOLD);
            textView1.setTextColor(Color.parseColor("#000000"));
            textView1.setPadding(10, 30, 0, 20);
            linearlayout.addView(textView1);
            TextView textView2 = new TextView(this);
            textView2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView2.setText("Your Answer: "+useranswer);
            textView2.setTextSize(16);
            textView2.setTextColor(Color.parseColor("#000000"));
            textView1.setPadding(10, 10, 0, 10);
            linearlayout.addView(textView2);
            TextView textView3 = new TextView(this);
            textView3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView3.setText("Correct Answer: "+correctanswer);
            textView3.setTextSize(16);
            textView3.setTextColor(Color.parseColor("#000000"));
            textView1.setPadding(10, 10, 0, 40);
            linearlayout.addView(textView3);
        }

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
                    for(int i=0;i<n;i++)
                    {
                        //USER_ID = Integer.parseInt();
                        JSONArray jsonarray1=jsonarray.getJSONArray(i);
                        String q=jsonarray1.getString(0);
                        String c1=jsonarray1.getString(1);
                        String c2=jsonarray1.getString(2);
                        String c3=jsonarray1.getString(3);
                        String c4=jsonarray1.getString(4);
                        String a=jsonarray1.getString(5);
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
            setAnswers();
        }

    }
}
