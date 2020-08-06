package in.dailycrunch.quiztion;

/*
Adarsh Sodagudi (025281709)
Mansi Koul (018761247)
*/

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedList;

/*<Summary>Displays results based on aggregate of all the quizzes</Summary>*/
public class AdminLeaderboardActivity extends AppCompatActivity {

    private RecyclerView recyclerview;
    LinkedList<String> list=new LinkedList<>();
    LinkedList<String> scores=new LinkedList<>();
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_leaderboard);
        recyclerview=(RecyclerView)findViewById(R.id.userlistviewid);
        //url used to make ServiceCall that holds user scores and rankings
        URL="http://dailycrunch.in/quiztion/leaderboard.php";
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
                    //Parses the JSON String
                    JSONArray jsonarray=new JSONArray(jsonStr);
                    int n=jsonarray.length();
                    if(n!=0)
                    {
                        for(int i=0;i<n;i++)
                        {
                            //Retrieving quizname along with the score for all quizzes taken by all users
                            JSONArray jsonarray1=jsonarray.getJSONArray(i);
                            String quizname=jsonarray1.getString(0);
                            String score=jsonarray1.getString(1);
                            //Adding the quizname along with the corresponding score to the Linked List
                            list.add(quizname);
                            scores.add(score);

                        }
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
            UserListAdapter ULA=new UserListAdapter(getApplicationContext(),list);
            recyclerview.setAdapter(ULA);
            recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }

    }


    //RecyclerView Adapter
    public class UserListAdapter extends RecyclerView.Adapter<AdminLeaderboardActivity.UserListAdapter.UserViewHolder>{

        private Context mContext;

        private LinkedList<String> muserList;

        private LayoutInflater mInflater;

        public UserListAdapter(Context context,LinkedList<String> quizList)
        {
            mContext=context;
            mInflater=LayoutInflater.from(context);
            this.muserList=quizList;
        }


        @NonNull
        @Override
        public AdminLeaderboardActivity.UserListAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View mItemView = mInflater.inflate(R.layout.leaderboarditem,parent, false);
            return new AdminLeaderboardActivity.UserListAdapter.UserViewHolder(mItemView, this);
        }

        //Displaying user name along with their aggregate Score
        @Override
        public void onBindViewHolder(@NonNull AdminLeaderboardActivity.UserListAdapter.UserViewHolder holder, int position) {
            String mCurrent = muserList.get(position);
            String mscore = scores.get(position);
            holder.scoreItemView.setText("Aggregate Score: "+mscore);
            holder.userItemView.setText(mCurrent);
        }

        @Override
        public int getItemCount() {
            return muserList.size();
        }

        public class UserViewHolder extends RecyclerView.ViewHolder
        {
            public TextView userItemView=null;
            public TextView scoreItemView=null;
            AdminLeaderboardActivity.UserListAdapter mAdapter=null;

            public UserViewHolder(View view, AdminLeaderboardActivity.UserListAdapter adapter)
            {
                super(view);
                userItemView = view.findViewById(R.id.username_leader);
                scoreItemView = view.findViewById(R.id.score);
                this.mAdapter = adapter;
            }

        }
    }
}
