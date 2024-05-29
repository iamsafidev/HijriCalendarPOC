package com.iamsafi.myapplication


import android.icu.util.IslamicCalendar
import android.icu.util.ULocale
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker

import android.widget.Spinner
import com.iamsafi.myapplication.utils.UmmalquraCalendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerMonth: Spinner
    private lateinit var spinnerDay: Spinner
    private lateinit var spinnerYear: Spinner

    private val months = listOf(
        "Muharram", "Safar", "Rabi' al-awwal", "Rabi' al-thani",
        "Jumada al-awwal", "Jumada al-thani", "Rajab", "Sha'ban",
        "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerMonth = findViewById(R.id.spinnerMonth)
        spinnerDay = findViewById(R.id.spinnerDay)
        spinnerYear = findViewById(R.id.spinnerYear)

        setupMonthSpinner()
        setupYearSpinner()

        spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                updateDaySpinner()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
               updateDaySpinner()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupMonthSpinner() {
        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMonth.adapter = monthAdapter
    }

    private fun setupYearSpinner() {
        val currentYear = UmmalquraCalendar().get(UmmalquraCalendar.YEAR)
        val years = (currentYear - 100..currentYear + 100).toList()
        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerYear.adapter = yearAdapter
        spinnerYear.setSelection(years.indexOf(currentYear))
    }

    private fun updateDaySpinner() {
        val selectedMonth = spinnerMonth.selectedItemPosition
        val selectedYear = spinnerYear.selectedItem as Int

        val calendar = UmmalquraCalendar(Locale("ar","SA","@calendar=islamic-umalqura"))
        calendar.set(UmmalquraCalendar.YEAR, selectedYear)
        calendar.set(UmmalquraCalendar.MONTH, selectedMonth)

        val daysInMonth = calendar.lengthOfMonth()
        val days = (1..daysInMonth).toList()

        val dayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, days)
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDay.adapter = dayAdapter
    }

}