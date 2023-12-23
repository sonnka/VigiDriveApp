package com.vigidriveapp.ui.activity

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.vigidriveapp.R


class InfoPage : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_info_page, container, false)
        draw(v)
        return v
    }

    fun draw(v: View) {
        val pieChart: PieChart = v.findViewById(R.id.pieChart)

        val entries: ArrayList<PieEntry> = ArrayList<PieEntry>()

        entries.add(PieEntry(10.0f, "stress"))

        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.setColors(*ColorTemplate.MATERIAL_COLORS)
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.sliceSpace = 2f
        pieDataSet.valueTextSize = 20f

        val pieData = PieData(pieDataSet)

        val legend = pieChart.legend
        legend.form = Legend.LegendForm.SQUARE
        legend.textSize = 17f
        legend.yOffset = 20f
        legend.setDrawInside(false)

        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.centerText = "Витрати\n(грн)"
        pieChart.setCenterTextSize(20f)
        pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)
        pieChart.setCenterTextColor(Color.parseColor("#222222"))
        pieChart.animate()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoPage().apply {
                arguments = Bundle().apply {

                }
            }
    }
}