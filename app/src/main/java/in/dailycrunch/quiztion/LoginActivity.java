package in.dailycrunch.quiztion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Adarsh Sodagudi(025281709).
 * Mansi Koul(018761247)
 */


//This handles events happening in the LoginActivity
public class LoginActivity extends AppCompatActivity {

    private String URL="";
    private int USER_ID;
    private EditText username;
    private EditText password;
    private Button signup;
    private Button login;
    private int USERID;
    private String USERNAME;
    private String EMAIL;

    public void gotoForgot(View view)
    {
        //Transferring control to handle forget password request from user
        Intent intent=new Intent(this,ForgotActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=(EditText)findViewById(R.id.log_usernameid);
        password=(EditText)findViewById(R.id.log_passwordid);
        signup=(Button)findViewById(R.id.signup_btn);
        login=(Button)findViewById(R.id.login_btn);
        //When user clicks on SignUp button he will be navigated to SignupAcctivity
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
            }
        });

        //Validates Login credentials, Gives access to welcome activity if user exist
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=username.getText().toString();
                String passcode=password.getText().toString();
                if(name.isEmpty() || passcode.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter Credentials",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    URL="http://dailycrunch.in/quiztion/login.php?username="+name+"&password="+passcode;
                    new GetQuizList().execute();

                }
            }
        });


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
            //String jsonStr = "[Asia,Africa]";


            if (jsonStr != null) {
                try {
                    /*Parses the JSON required to Spinner 1*/
                    JSONArray jsonarray=new JSONArray(jsonStr);

                        USER_ID= Integer.parseInt(jsonarray.getString(0));
                        USERNAME=jsonarray.getString(1);
                        EMAIL=jsonarray.getString(2);

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
            if(USER_ID>0)
            {
                DataBaseHandler DBH=new DataBaseHandler(getApplicationContext());
                DBH.onInsert(USER_ID+"",USERNAME,EMAIL);
                if(USER_ID==1)
                {
                    Intent intent=new Intent(getApplicationContext(),AdminWelcomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"No Record Found",Toast.LENGTH_SHORT).show();
            }

        }

    }


}
