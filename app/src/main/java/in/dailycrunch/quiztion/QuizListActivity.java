package in.dailycrunch.quiztion;

/**
 * Created by Adarsh Sodagudi(025281709).
 * Mansi Koul(018761247)
 */


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedList;
import java.util.Random;

public class QuizListActivity extends AppCompatActivity {

    private RecyclerView recyclerview;
    LinkedList<String> list=new LinkedList<>();
    private String DISCOVER;
    private String QUIZLIST;
    private String URL;
    public int past=0;
    private String[] color={"#9BD7D1","#68797B","#96CEB4","#6C5B7B","#355C7D"};/*colors*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);
        Intent intent=this.getIntent();
        String msg=intent.getStringExtra("quizlist");
        DISCOVER=msg;
        recyclerview=findViewById(R.id.quizlistviewid);
        //URL to Display Quizlist by appending the value selected by user in Discover Page
        URL="http://www.dailycrunch.in/quiztion/quizlist.php?topic="+DISCOVER;
        Log.d("URL",URL);
        new GetQuizList().execute();
    }

    public void gotoQuestions()
    {
        //Passing flow to Question Activity to display questions along with the topic and quizlist
        Intent intent= new Intent(getApplicationContext(),QuestionActivity.class);
        intent.putExtra("DISCOVER",DISCOVER);
        intent.putExtra("QUIZLIST",QUIZLIST);
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
            //String jsonStr = "[Asia,Africa]";


            if (jsonStr != null) {
                try {
                    /*Parses the JSON required to Spinner 1*/
                        JSONArray jsonarray=new JSONArray(jsonStr);
                        for(int i=0;i<jsonarray.length();i++)
                        {
                            String quiz_name = jsonarray.getString(i);
                            list.add(quiz_name);
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
            QuizListAdapter QLA=new QuizListAdapter(getApplicationContext(),list);
            recyclerview.setAdapter(QLA);
            recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }

    }

///<summary>This is a RecyclerView Adapter</summary>
    public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizViewHolder>{

        private Context mContext;

        private LinkedList<String> mquizList;

        private LayoutInflater mInflater;

        public QuizListAdapter(Context context,LinkedList<String> quizList)
        {
            mContext=context;
            mInflater=LayoutInflater.from(context);
            this.mquizList=quizList;
        }

        @NonNull
        @Override
        public QuizListAdapter.QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View mItemView = mInflater.inflate(R.layout.quizlist_item,parent, false);
            return new QuizViewHolder(mItemView, this);
        }

        @Override
        public void onBindViewHolder(@NonNull QuizListAdapter.QuizViewHolder holder, int position) {
            String mCurrent = mquizList.get(position);
            holder.quizItemView.setText(mCurrent);


        }

        @Override
        public int getItemCount() {
            return mquizList.size();
        }

        public class QuizViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {
            public TextView quizItemView=null;
            public LinearLayout ll=null;
            QuizListAdapter mAdapter=null;

            public QuizViewHolder(View view,QuizListAdapter adapter)
            {
                super(view);
                quizItemView = view.findViewById(R.id.quizname);
                ll=view.findViewById(R.id.llitem);
                Random rand=new Random();
                int colorpick=rand.nextInt(5);
                if(past==colorpick)
                {
                    if(colorpick==4)
                    {
                        colorpick--;
                    }
                    else if(colorpick==0)
                    {
                        colorpick++;
                    }
                    else
                    {
                        colorpick++;
                    }
                }
                past=colorpick;
//                Log.e("ERROR",colorpick+"");
                String pickedcolor=color[colorpick];
                ll.setBackgroundColor(Color.parseColor(pickedcolor));
                this.mAdapter = adapter;
                quizItemView.setOnClickListener(this);
            }

            /*<summary>OnClick listener for the recycler view</summary>*/
            @Override
            public void onClick(View view) {
                Context context;
                //Get the position of the item that was clicked.
                int mPosition = getLayoutPosition();
                // Use that to access the affected item in mWordList.
                String RecyclerView_Item_Text = mquizList.get(mPosition);
                QUIZLIST=RecyclerView_Item_Text;
                //Toast.makeText(getApplicationContext(),DISCOVER+RecyclerView_Item_Text,Toast.LENGTH_SHORT).show();
                // Change the word in the mWordList.
//            mquizList.set(mPosition, "Clicked! " + element);
                // Notify the adapter, that the data has changed so it can
                // update the RecyclerView to display the data.
                gotoQuestions();
                mAdapter.notifyDataSetChanged();


            }
        }
    }

}
