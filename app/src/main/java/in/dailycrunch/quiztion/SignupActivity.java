package in.dailycrunch.quiztion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Adarsh Sodagudi(025281709).
 * Mansi Koul(018761247)
 */

public class SignupActivity extends AppCompatActivity {

    private String URL="";
    private EditText username;
    private EditText password;
    private EditText retype;
    private EditText institutionid;
    private EditText email;
    private EditText phone;
    private Button signup;
    private String RESPONSE;

    //This handles events happening in the SignUpActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //Reading user input entered in the EditText
        username =(EditText)findViewById(R.id.usernameid);
        password =(EditText)findViewById(R.id.passwordid);
        retype =(EditText)findViewById(R.id.retypeid);
        institutionid =(EditText)findViewById(R.id.institutionid);
        email =(EditText)findViewById(R.id.emailid);
        phone =(EditText)findViewById(R.id.phoneid);
        signup=(Button)findViewById(R.id.signup_btn);
        //Method called when the user clicks the Signup button</summary>
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                boolean response=validate_insert(username.getText().toString(),password.getText().toString(),retype.getText().toString(),institutionid.getText().toString(),email.getText().toString(),phone.getText().toString());
                if(response)
                {
                    Notification("Account Created!");
                }
            }
        });
        //Method is called, For every keydown in the username EditText field
        username.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                String name=username.getText().toString();
                if(username.length()>=5)
                {
                    username.setError(null);
                }
                else
                {
                    username.setError("Username should be atleast 5 characters");
                }
                return false;
            }
        });
        //Method is called, For every keydown in the password EditText field
        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                String password1=password.getText().toString();
                if(password.length()>=6 || password.length()==0)
                {
                    password.setError(null);
                }
                else
                {
                    password.setError("Password should be atleast 6 characters");

                }
                return false;
            }
        });
        //Method is called, For every keydown in the retype EditText field
        retype.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                String password1=password.getText().toString();
                String retypepassword=retype.getText().toString();
                if(password1.equals(retypepassword) || retypepassword.length()==0)
                {
                    retype.setError(null);
                }
                else
                {
                    retype.setError("Both Passwords should match");
                }
                return false;
            }
        });


    }

    // Validates user inputs in signup form and makes a call to Notification method with appropriate message.
    private boolean validate_insert(String username,String password,String retype,String institutionid,String email,String phone)
    {
        if(username.isEmpty() || password.isEmpty() || retype.isEmpty() || institutionid.isEmpty() || email.isEmpty() || phone.isEmpty())
        {
            Notification("All fields are mandatory");
            return false;
        }

        if(!password.equals(retype))
        {
            Notification("Passwords Mismatch");
            return false;
        }
        else
        {
            if(password.length()<6)
            {
                Notification("Password should be atleast 6 characters");
                return false;
            }
        }
        if(institutionid.length()<3)
        {
            Notification("Check the Institution format");
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Notification("Check the email format");
            return false;
        }

        if(phone.length()!=10)
        {
            Notification("Phone number should be 10 digits");
            return false;
        }
        URL="http://dailycrunch.in/quiztion/signup.php?username="+username+"&password="+password+"&institution="+institutionid+"&email="+email+"&phone="+phone;
        Log.e("Error alert",URL);
        new GetQuizList().execute();
        //dbh.onInsert(username,password,email,phone);
        return true;
    }


    //Displaying toast
    private void Notification(String message)
    {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
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
                Notification("Try Again!");
            }
            else
            {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }

        }

    }
}
