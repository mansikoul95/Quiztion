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
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
/*edits the user profile*/
public class EditProfile extends AppCompatActivity {

    private TextView username;
    private EditText password;
    private EditText institutionid;
    private EditText email;
    private EditText phone;
    private String susername;
    private String spassword;
    private String semail;
    private String sinstitution;
    private String sphone;
    private String URL;
    private String USERID;
    private String RESPONSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Intent intent=this.getIntent();
        ArrayList<String> data=intent.getStringArrayListExtra("data");
        //Read existing values in EditText
        username =findViewById(R.id.usernameid);
        password =findViewById(R.id.passwordid);
        institutionid =findViewById(R.id.institutionid);
        email =findViewById(R.id.emailid);
        phone =findViewById(R.id.phoneid);
        //Adding New Values from ArrayList
        username.setText(data.get(0));
        password.setText(data.get(1));
        institutionid.setText(data.get(2));
        email.setText(data.get(3));
        phone.setText(data.get(4));

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Username cannot be changed", Toast.LENGTH_LONG).show();
            }
        });
    }
    /*validates and makes a request*/
    public void save(View view)
    {
        int flag=0;
        if(password.getText().toString().isEmpty())
        {
            password.setError("Enter Password");
            flag=1;
        }
        if(institutionid.getText().toString().isEmpty())
        {
            institutionid.setError("Enter Institution");
            flag=1;
        }
        if(email.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
        {
            email.setError("Enter Email Properly");
            flag=1;
        }
        if(phone.getText().toString().isEmpty() || phone.getText().toString().length()!=10)
        {
            phone.setError("Enter Phone Number Properly");
            flag=1;
        }
        if(flag==0)
        {
            //Converting EditText Value to String
            susername=username.getText().toString();
            spassword=password.getText().toString();
            sinstitution=institutionid.getText().toString();
            semail=email.getText().toString();
            sphone=phone.getText().toString();
            DataBaseHandler DBH=new DataBaseHandler(getApplicationContext());
            USERID=DBH.getId();
            //URL to input changed variable values to database
            URL="http://dailycrunch.in/quiztion/editprofile.php?userid="+USERID+"&username="+susername+"&password="+spassword+"&institution="+sinstitution+"&email="+semail+"&phone="+sphone;
            new GetQuizList().execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Check Credentials",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(),"Changes Saved",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }

        }

    }
}
