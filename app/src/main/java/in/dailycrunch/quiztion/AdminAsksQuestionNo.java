package in.dailycrunch.quiztion;


/*
Adarsh Sodagudi (025281709)
Mansi Koul (018761247)
*/

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
/*<Summary>Accepting values for topic,quizname and number of questions</Summary>*/
public class AdminAsksQuestionNo extends AppCompatActivity {

    private Spinner topic;
    private EditText quizname;
    private EditText noofquestions;
    private String topicname="";
    private String quiz;
    private String noofq;

    /*<Summary>Read topic, quiz name and number of questions for new Quiz on click</Summary>*/
    public void gotoPaperSet(View view)
    {
       quiz  =quizname.getText().toString();
       noofq =noofquestions.getText().toString();
       //Passing variables to AdminPaperSetActivity
        Intent intent =new Intent(this,AdminPaperSetActivity.class);
        intent.putExtra("topic",topicname);
        intent.putExtra("quiz",quiz);
        intent.putExtra("noofq",noofq);
        //Passing control to AdminPaperSetActivity
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_asks_question_no);
        //Getting admin inputs
        topic=findViewById(R.id.topicid);
        quizname=findViewById(R.id.quiznameid);
        noofquestions=findViewById(R.id.noofquestionsid);
        //<summary>Reading Spinner selection</summary>
        topic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Converting Spinner selection to String
                topicname=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
