/**
 * code created by Fabio Ciravegna, The University of Sheffield.
 * refactored by Omar Brugna and George Stavrou
 */
package com.active.orbit.baseapp.design.utils

import android.graphics.Color
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.design.recyclers.models.TripModel
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.gms.location.DetectedActivity
import uk.ac.shef.tracker.core.computation.data.MobilityData.Companion.INVALID_VALUE
import uk.ac.shef.tracker.core.utils.TimeUtils

class CadenceGraphDisplay(private var combinedChart: CombinedChart, private val currentTrip: TripModel) {

    val xEntries: ArrayList<String?> = ArrayList()

    init {
        initChart()
        showBarChart()
    }

    private fun showBarChart() {
        val yEntries: ArrayList<BarEntry> = ArrayList()
        val lineEntries: ArrayList<Entry> = ArrayList()
        val title = "Title"

        //input data
        val base = currentTrip.startTime
        var counter =0
        for (index in base..currentTrip.endTime) {
            if (currentTrip.chart[index].steps != INVALID_VALUE) {
                val barEntry = BarEntry(counter.toFloat(), currentTrip.chart[index].cadence.toFloat())
                yEntries.add(barEntry)
                Logger.i("Steps: ${currentTrip.chart[index].cadence}")
                val lineEntry = Entry(counter.toFloat(), if (currentTrip.activityType == DetectedActivity.ON_BICYCLE) 40f else 100f)
                lineEntries.add(lineEntry)
                Logger.i("${TimeUtils.formatMillis(currentTrip.chart[index].timeInMSecs, Constants.DATE_FORMAT_HOUR_MINUTE_SECONDS)} --- $barEntry")
                xEntries.add(TimeUtils.formatMillis(currentTrip.chart[index].timeInMSecs, Constants.DATE_FORMAT_HOUR_MINUTE_SECONDS))
                counter++
            }
        }

        val barDataSet = BarDataSet(yEntries, title)
        val barData = BarData()
        barData.addDataSet(barDataSet)

        val lineDataSet = LineDataSet(lineEntries, title)
        lineDataSet.setDrawCircles(false)
        val lineData = LineData()
        lineData.addDataSet(lineDataSet)
        initBarDataSet(barDataSet, lineDataSet)

        val data = CombinedData()
        data.setData(barData)
        data.setData(lineData)

        combinedChart.data = data
        combinedChart.invalidate()
    }

    private fun initBarDataSet(barDataSet: BarDataSet, lineDataSet: LineDataSet) {
        //Changing the color of the bar
        barDataSet.color = Color.parseColor("#3448B7")
        //Setting the size of the form in the legend
        barDataSet.formSize = 15f
        //showing the value of the bar, default true if not set
        barDataSet.setDrawValues(false)
        //setting the text size of the value of the bar
        barDataSet.valueTextSize = 12f

        //Changing the color of the line
        lineDataSet.color = Color.RED
        //Setting the size of the form in the legend
        lineDataSet.formSize = 15f
        //showing the value of the line, default true if not set
        lineDataSet.setDrawValues(false)
        //setting the text size of the value of the line
        lineDataSet.valueTextSize = 12f
    }

    private fun initChart() {
        //hiding the grey background of the chart, default false if not set
        //        combinedChart.setDrawGridBackground(false)
        combinedChart.setBackgroundColor(Color.WHITE) //set whatever color you prefer
        combinedChart.setDrawGridBackground(false)// this is a must
        //remove the bar shadow, default false if not set
        combinedChart.setDrawBarShadow(false)
        //remove border of the chart, default false if not set
        combinedChart.setDrawBorders(false)

        //remove the description label text located at the lower right corner
        val description = Description()
        description.isEnabled = false
        combinedChart.description = description

        //setting animation for y-axis, the bar will pop up from 0 to its value within the time we set
        //        combinedChart.animateY(1000)
        //setting animation for x-axis, the bar will pop up separately within the time we set
        //        combinedChart.animateX(1000)

        val xAxis = combinedChart.xAxis
        //change the position of x-axis to the bottom
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //set the horizontal distance of the grid line
        xAxis.isGranularityEnabled = true
        xAxis.granularity = 1f
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                if (xEntries.size > value.toInt())
                    return xEntries[value.toInt()]!!
                else return ""
            }
        }

        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(true)
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false)
        val leftAxis = combinedChart.axisLeft
        //hiding the left y-axis line, default true if not set
        leftAxis.setDrawAxisLine(false)
        leftAxis.setDrawGridLines(false)

        val rightAxis = combinedChart.axisRight
        //hiding the right y-axis line, default true if not set
        rightAxis.setDrawAxisLine(false)
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawLabels(false)

        val legend: Legend = combinedChart.legend
        //setting the shape of the legend form to line, default square shape
        legend.form = Legend.LegendForm.LINE
        //setting the text size of the legend
        legend.textSize = 11f
        //setting the alignment of legend toward the chart
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        //setting the stacking direction of legend
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        //setting the location of legend outside the chart, default false if not set
        legend.setDrawInside(false)
        legend.isEnabled = false
    }
}
