package in.dailycrunch.quiztion;

/**
 * Created by Adarsh Sodagudi(025281709).
 * Mansi Koul(018761247)
 */


import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Cartesian3d;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Column3d;
import com.anychart.data.Mapping;
import com.anychart.enums.Anchor;
import com.anychart.data.Set;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.SolidFill;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


//Class to Display user Performance Bar Chart
public class PerformanceActivity extends AppCompatActivity {

    List<DataEntry> data = new ArrayList<>();
    List<DataEntry> seriesData = new ArrayList<>();
    private String URL="";
    private String USERID;
    private int FLAG=0;
    AnyChartView anyChartView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        DataBaseHandler DBH =new DataBaseHandler(getApplicationContext());
        //Get userid and append to URL
        USERID=DBH.getId();
        URL="http://dailycrunch.in/quiztion/performance.php?id="+USERID;

        anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        //initiate_graph();
        new GetQuizList().execute();
        }

    private void initiate_graph()
    {
        Cartesian3d column3d = AnyChart.column3d();

        column3d.yScale().stackMode(ScaleStackMode.VALUE);

        column3d.animation(true);
        column3d.title().padding(0d, 0d, 15d, 0d);


        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2' }");


        Column3d series1 = column3d.column(series1Data);
        series1.name("% of Wrong Answers");
        series1.fill(new SolidFill("#B00020", 1d));
        series1.stroke("1 #f7f3f3");
        series1.hovered().stroke("3 #f7f3f3");

        Column3d series2 = column3d.column(series2Data);
        series2.name("% of Correct Answers");
        series2.fill(new SolidFill("#0336FF", 1d));
        series2.stroke("1 #f7f3f3");
        series2.hovered().stroke("3 #f7f3f3");

        column3d.legend().enabled(true);
        column3d.legend().fontSize(13d);
        column3d.legend().padding(0d, 0d, 20d, 0d);

        column3d.yScale().ticks("[0, 20, 40, 60, 80, 100]");
        column3d.xAxis(0).stroke("1 #a18b7e");
        column3d.xAxis(0).labels().fontSize("#a18b7e");
        column3d.yAxis(0).stroke("1 #a18b7e");
        column3d.yAxis(0).labels().fontColor("#a18b7e");
        column3d.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        column3d.yAxis(0).title().enabled(true);
        column3d.yAxis(0).title().text("Scores(%)");
        column3d.yAxis(0).title().fontColor("#a18b7e");

        column3d.interactivity().hoverMode(HoverMode.BY_X);

        column3d.tooltip()
                .displayMode(TooltipDisplayMode.UNION)
                .format("{%Value} {%SeriesName}");

        column3d.yGrid(0).stroke("#a18b7e", 1d, null, null, null);
        column3d.xGrid(0).stroke("#a18b7e", 1d, null, null, null);

        anyChartView.setChart(column3d);

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
                    if(n!=0)
                    {
                        for(int i=0;i<n;i++)
                        {
                            //USER_ID = Integer.parseInt();
                            JSONArray jsonarray1=jsonarray.getJSONArray(i);
                            String quizname=jsonarray1.getString(0);
                            String score=jsonarray1.getString(1);
                            int scoreint=Integer.parseInt(score);
                            Log.e("Eror",quizname+""+scoreint);
                            seriesData.add(new CustomDataEntry(quizname, (100-scoreint), scoreint));
                            FLAG=1;

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
            if(FLAG==1)
            {
                initiate_graph();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"You have not taken any quiz yet", Toast.LENGTH_LONG).show();
            }
        }

    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);

        }
    }
}