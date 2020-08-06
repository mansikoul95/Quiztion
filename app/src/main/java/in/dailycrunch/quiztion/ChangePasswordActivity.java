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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldpasswordid;
    private EditText newpasswordid;
    private EditText retypenewpasswordid;

    private String oldpassword;
    private String newpassword;
    private String retypenewpassword;
    private String USERID;
    private String URL;
    private String RESPONSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        //Read user input of existing password, new password and retype password
        oldpasswordid=findViewById(R.id.oldpasswordid);
        newpasswordid=findViewById(R.id.newpasswordid);
        retypenewpasswordid=findViewById(R.id.retypenewpasswordid);
        DataBaseHandler DBH=new DataBaseHandler(getApplicationContext());
        USERID=DBH.getId();
    }

    //validates the input and makes a request to the url
    public void changepassword(View view)
    {
        //Convert values to String
        oldpassword=oldpasswordid.getText().toString();
        newpassword=newpasswordid.getText().toString();
        retypenewpassword=retypenewpasswordid.getText().toString();
        int flag=0;
        if(oldpassword.isEmpty())
        {
            oldpasswordid.setError("Enter Old Password");
            flag=1;
        }
        if(newpassword.isEmpty())
        {
            newpasswordid.setError("Enter New Password");
            flag=1;
        }
        if(retypenewpassword.isEmpty())
        {
            retypenewpasswordid.setError("Retype New Password");
            flag=1;
        }
        if(flag==0)
        {
            if(newpassword.equals(retypenewpassword) && !oldpassword.isEmpty())
            {
                //URL to update datebase with new password values
                URL="http://www.dailycrunch.in/quiztion/changepassword.php?userid="+USERID+"&oldpassword="+oldpassword+"&newpassword="+newpassword;
                new GetQuizList().execute();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Type Correctly",Toast.LENGTH_SHORT).show();
            }
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
                Toast.makeText(getApplicationContext(),"Password Changed",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }

        }

    }
}
