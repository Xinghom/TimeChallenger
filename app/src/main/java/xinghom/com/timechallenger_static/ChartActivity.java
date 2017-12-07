package xinghom.com.timechallenger_static;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    private static final String TAG = "ChartActivity";
    UsageStatsManager mUsageStatsManager;
    private PieChart pieChart;
    List<UsageStats> mUsageStatsList;
    String span;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_statistics);
        Spinner mSpinner = (Spinner) findViewById(R.id.statistic_time_span);
        String[] mItems ={"Daily", "Weekly", "Monthly", "Yearly"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        mSpinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                span = adapter.getItem(position);
                mUsageStatsList = initialize(span);
                setData(mUsageStatsList, span);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mUsageStatsManager = (UsageStatsManager)getSystemService("usagestats"); //Context.USAGE_STATS_SERVICE

        mUsageStatsList  = initialize("Daily");
        setData(mUsageStatsList, span);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(10f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setTextSize(14f);
        Log.d(TAG, "size : " + pieChart.getData().getDataSets().size());
    }

    public List<UsageStats> getUsageStatistics(int intervalType) {
        // Get the app statistics since one year ago from the current time.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);

        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(intervalType, cal.getTimeInMillis(),
                        System.currentTimeMillis());
        return queryUsageStats;
    }

    public List<UsageStats> initialize(String span){
        int intervalType = UsageStatsManager.INTERVAL_DAILY;
        switch (span){
            case "Daily": {
                intervalType = UsageStatsManager.INTERVAL_DAILY;
                break;
            }
            case "Weekly": {
                intervalType = UsageStatsManager.INTERVAL_WEEKLY;
                break;
            }
            case "Monthly": {
                intervalType = UsageStatsManager.INTERVAL_MONTHLY;
                break;
            }
            case "Yearly": {
                intervalType = UsageStatsManager.INTERVAL_YEARLY;
                break;
            }
            default:break;
        }
        List<UsageStats> usageStatsList =
                getUsageStatistics(intervalType);
        Collections.sort(usageStatsList, new ChartActivity.TotalTimeLaunchedComparatorDesc());
        return usageStatsList;
    }

    private static class TotalTimeLaunchedComparatorDesc implements Comparator<UsageStats> {
        @Override
        public int compare(UsageStats left, UsageStats right) {
            return Long.compare(right.getTotalTimeInForeground(), left.getTotalTimeInForeground());
        }
    }

    private void setData(List<UsageStats> usageStatsList, String span) {
        //float mult = range;
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        if (usageStatsList.size() > 10){
            for (int i = 0; i < 10; i++){
                String name = usageStatsList.get(i).getPackageName();
                StringBuilder sb = new StringBuilder();
                for (int j = name.length() - 1; name.charAt(j) != '.'; j--){
                    sb.append(name.charAt(j));
                }
                PieEntry entry = new PieEntry(usageStatsList.get(i).getTotalTimeInForeground() / 1000, sb.reverse().toString());
                entries.add(entry);
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Top10 Usage");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.BLACK);
       // data.setValueTypeface(mTfLight);
        pieChart = (PieChart)findViewById(R.id.pic_chart);
        pieChart.setUsePercentValues(false);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setDrawEntryLabels(false);
        pieChart.setExtraOffsets(5, 25, 40, 20);
        pieChart.setCenterText(span);
        pieChart.setCenterTextSize(22f);

        pieChart.setData(data);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
        for (IDataSet<?> set : pieChart.getData().getDataSets()){
            set.setDrawValues(false);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater(). inflate( R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.statisticsItem: {

                break;
            }
            case R.id.challengeModeItem: Toast.makeText( this, "ChallengeMode", Toast.LENGTH_SHORT). show();
                break;
            default:
        }
        return true;
    }
}
