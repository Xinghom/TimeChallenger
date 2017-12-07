package xinghom.com.timechallenger_static;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.usage.UsageStatsManager.*;

/**
 * Created by xinghom on 12/6/17.
 */

public class ChallengeMode extends Activity {
    private static final String TAG = "ChallengeMode";
    UsageStatsManager mUsageStatsManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE); //Context.USAGE_STATS_SERVICE
        List<UsageStats> usageStatsList = getUsageStatistics(INTERVAL_DAILY);
        Log.i(TAG, usageStatsList.toString());

        //创建条形数据对象
        BarChart barChart = new BarChart(this);
        setContentView(barChart);
        //设置条形数据
        barChart.setData(getBarData());
        //设置描述
//        barChart.setDescription("Usage Time");
        //设置绘制bar的阴影
        barChart.setDrawBarShadow(true);
        //设置绘制的动画时间
        barChart.animateXY(3000,3000);
    }


    // intervalType = INTERVAL_DAILY
    public List<UsageStats> getUsageStatistics(int intervalType) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);

        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(intervalType, cal.getTimeInMillis(),
                        System.currentTimeMillis());
        return queryUsageStats;
    }

    public BarData getBarData() {
        int maxX = 10;
        //创建集合，存放每个柱子的数据
        List<BarEntry> list = new ArrayList<>();
        List<BarEntry> list2 = new ArrayList<>();
        for (int i = 0; i < maxX; i++) {
            //一个BarEntry就是一个柱子的数据对象
            BarEntry barEntry = new BarEntry(i + 5, i);
            list.add(barEntry);
            BarEntry barEntry2 = new BarEntry(i + 3, i);
            list2.add(barEntry2);
        }
        //创建BarDateSet对象，其实就是一组柱形数据
        BarDataSet barSet = new BarDataSet(list, "Android");
        BarDataSet barSet2 = new BarDataSet(list2, "iOS");
        //设置柱形的颜色
        barSet.setColor(Color.BLUE);
        //设置是否显示柱子上面的数值
        barSet.setDrawValues(false);
        //设置柱子阴影颜色
        barSet.setBarShadowColor(Color.GRAY);
        //创建集合，存放所有组的柱形数据
        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barSet);
        dataSets.add(barSet2);
//        BarData barData = new BarData(ChartData.generateXVals(0, maxX), dataSets);
        BarData barData = new BarData(dataSets);
        return barData;
    }
}