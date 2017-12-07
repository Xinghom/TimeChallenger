package xinghom.com.timechallenger_static;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
        Map<String, UsageStats> usageStatsMap = getUsageStatistics(INTERVAL_BEST);
        Log.i(TAG, usageStatsMap.toString());

        BarChart barChart = new BarChart(this);
        setContentView(barChart);
        //设置条形数据
        barChart.setData(getBarData(usageStatsMap));
        //设置绘制的动画时间
        barChart.animateXY(3000,3000);

        barChart.setExtraBottomOffset(10);//距视图窗口底部的偏移，类似与paddingbottom
        barChart.setExtraTopOffset(30);//距视图窗口顶部的偏移，类似与paddingtop
        barChart.setFitBars(true);//使两侧的柱图完全显示
        //x坐标轴设置
        XAxis xAxis = barChart.getXAxis();//获取x轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴标签显示位置
        xAxis.setDrawGridLines(false);//不绘制格网线
        xAxis.setGranularity(1f);//设置最小间隔，防止当放大时，出现重复标签。
        xAxis.setTextSize(10f);//设置标签字体大小
        xAxis.setLabelCount(10);//设置标签显示的个数
    }


    public Map<String, UsageStats> getUsageStatistics(int intervalType) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        List<UsageStats> queryUsageStats = mUsageStatsManager.queryUsageStats(intervalType, cal.getTimeInMillis(), System.currentTimeMillis());
        Map<String, UsageStats> map_usageStats = mUsageStatsManager.queryAndAggregateUsageStats(cal.getTimeInMillis(), System.currentTimeMillis());
        return map_usageStats;
    }

    private static class TotalTimeLaunchedComparatorDesc implements Comparator<UsageStats> {
        @Override
        public int compare(UsageStats left, UsageStats right) {
            return Long.compare(right.getTotalTimeInForeground(), left.getTotalTimeInForeground());
        }
    }

    public BarData getBarData(Map<String, UsageStats> usageStatsMap) {
        int maxX = 4;
        //创建集合，存放每个柱子的数据
        List<BarEntry> list = new ArrayList<>();
        for (int i = 0; i < maxX; i++) {
            //一个BarEntry就是一个柱子的数据对象
            long temp = usageStatsMap.get("com.google.android.youtube").getTotalTimeInForeground() / 1000;
            Random rand = new Random();
            int  n = rand.nextInt(150) + 1;
            BarEntry barEntry = new BarEntry(i, n);
            list.add(barEntry);
        }
        BarEntry barEntry4 = new BarEntry(4,  1270);
        BarEntry barEntry5 = new BarEntry(5, 1399);
        BarEntry barEntry6 = new BarEntry(6, 0);
        BarEntry barEntry7 = new BarEntry(7, 0);
        list.add(barEntry4);
        list.add(barEntry5);
        list.add(barEntry6);
        list.add(barEntry7);
        //创建BarDateSet对象，其实就是一组柱形数据
        BarDataSet barSet = new BarDataSet(list, "Usage Time of Youtube");
        //设置柱形的颜色
        barSet.setColor(R.color.bar);
        //设置是否显示柱子上面的数值
        barSet.setDrawValues(false);
        //创建集合，存放所有组的柱形数据
        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barSet);
//        BarData barData = new BarData(ChartData.generateXVals(0, maxX), dataSets);
        BarData barData = new BarData(dataSets);
        return barData;
    }


    //=================================================//
    /**
     * 单数据集。设置柱状图样式，X轴为字符串，Y轴为数值
     *
     * @param barChart
     * @param xAxisValue
     * @param yAxisValue
     * @param title 图例文字
     * @param xAxisTextSize x轴标签字体大小
     * @param barColor
     */
    public static void setBarChart(BarChart barChart, List<String> xAxisValue, List<Float> yAxisValue, String title, float xAxisTextSize, Integer barColor) {
        barChart.getDescription().setEnabled(false);//设置描述
        barChart.setPinchZoom(true);//设置按比例放缩柱状图

        //设置自定义的markerView
        MPChartMarkerView markerView = new MPChartMarkerView(barChart.getContext(), R.layout.custom_marker_view);
        barChart.setMarker(markerView);

        //x坐标轴设置
        IAxisValueFormatter xAxisFormatter = new StringAxisValueFormatter(xAxisValue);//设置自定义的x轴值格式化器
        XAxis xAxis = barChart.getXAxis();//获取x轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴标签显示位置
        xAxis.setDrawGridLines(false);//不绘制格网线
        xAxis.setGranularity(1f);//设置最小间隔，防止当放大时，出现重复标签。
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setTextSize(xAxisTextSize);//设置标签字体大小
        xAxis.setLabelCount(xAxisValue.size());//设置标签显示的个数

        //y轴设置
        YAxis leftAxis = barChart.getAxisLeft();//获取左侧y轴
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);//设置y轴标签显示在外侧
        leftAxis.setAxisMinimum(0f);//设置Y轴最小值
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);//禁止绘制y轴标签
        leftAxis.setDrawAxisLine(false);//禁止绘制y轴

        barChart.getAxisRight().setEnabled(false);//禁用右侧y轴

        //图例设置
        Legend legend = barChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);//图例水平居中
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);//图例在图表上方
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);//图例的方向为水平
        legend.setDrawInside(false);//绘制在chart的外侧
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);//图例中的文字方向

        legend.setForm(Legend.LegendForm.SQUARE);//图例窗体的形状
        legend.setFormSize(0f);//图例窗体的大小
        legend.setTextSize(16f);//图例文字的大小
        //legend.setYOffset(-2f);

        //设置柱状图数据
        setBarChartData(barChart, yAxisValue, title, barColor);

        barChart.setExtraBottomOffset(10);//距视图窗口底部的偏移，类似与paddingbottom
        barChart.setExtraTopOffset(30);//距视图窗口顶部的偏移，类似与paddingtop
        barChart.setFitBars(true);//使两侧的柱图完全显示
        barChart.animateX(1500);//数据显示动画，从左往右依次显示
    }

    /**
     * 设置柱图
     *
     * @param barChart
     * @param yAxisValue
     * @param title
     * @param barColor
     */
    private static void setBarChartData(BarChart barChart, List<Float> yAxisValue, String title, Integer barColor) {

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0, n = yAxisValue.size(); i < n; ++i) {
            entries.add(new BarEntry(i, yAxisValue.get(i)));
        }

        BarDataSet set1;

        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(entries);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setBarWidth(0.9f);
            data.setValueTextSize(10f);
            barChart.setData(data);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(entries, title);
            if (barColor == null) {
                set1.setColor(ContextCompat.getColor(barChart.getContext(), R.color.bar));//设置set1的柱的颜色
            } else {
                set1.setColor(barColor);
            }

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);
            data.setValueFormatter(new MyValueFormatter());

            barChart.setData(data);
        }
    }


}