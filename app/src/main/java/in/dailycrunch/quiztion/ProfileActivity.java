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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/*displays the user profile*/
public class ProfileActivity extends AppCompatActivity {

    private TextView username;
    private TextView password;
    private TextView institutionid;
    private TextView email;
    private TextView phone;
    private ArrayList<String> data=new ArrayList<>();

    private String USERID;
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        DataBaseHandler DBH=new DataBaseHandler(getApplicationContext());
        USERID=DBH.getId();
        URL="http://dailycrunch.in/quiztion/getuser.php?userid="+USERID;
        //Reading User data
        username =(TextView)findViewById(R.id.pusernameid);
        password =(TextView)findViewById(R.id.ppasswordid);
        institutionid =(TextView)findViewById(R.id.pinstitutionid);
        email =(TextView)findViewById(R.id.pemailid);
        phone =(TextView)findViewById(R.id.pphoneid);
        new GetQuizList().execute();
    }

    public void gotoeditprofile(View view)
    {
        //Transfer flow to Edit Profile Activity
        Intent intent=new Intent(this,EditProfile.class);
        intent.putExtra("data",data);
        startActivity(intent);
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
                    for(int i=0;i<jsonarray.length();i++)
                    {
                        data.add(jsonarray.getString(i));
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
            username.setText(data.get(0));
            password.setText(data.get(1));
            institutionid.setText(data.get(2));
            email.setText(data.get(3));
            phone.setText(data.get(4));
            }

        }

    }
