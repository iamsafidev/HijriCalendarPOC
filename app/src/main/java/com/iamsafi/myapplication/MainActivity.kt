package com.iamsafi.myapplication


import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.iamsafi.myapplication.utils.UmmalquraCalendar
import java.time.chrono.HijrahDate
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerMonth: Spinner
    private lateinit var spinnerDay: Spinner
    private lateinit var spinnerYear: Spinner

    private val months =listOf(
        "محرم", "صفر", "ربيع الأول", "ربيع الثاني",
        "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
        "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerMonth = findViewById(R.id.spinnerMonth)
        spinnerDay = findViewById(R.id.spinnerDay)
        spinnerYear = findViewById(R.id.spinnerYear)

        findViewById<NumberPicker>(R.id.monthPicker).apply {
            val currentMonth = UmmalquraCalendar().get(UmmalquraCalendar.MONTH)
            displayedValues = months.toTypedArray()
            wrapSelectorWheel = false
            minValue = 1
            maxValue = 12
            value = currentMonth + 1

            setOnValueChangedListener { _, _, newYear ->
                updateDayPicker()
            }
        }

        setupYearPicker()
        updateDayPicker()

        val hijrahDate = HijrahDate.now()
        setupMonthSpinner()
        setupYearSpinner()

        spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateDaySpinner()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateDaySpinner()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupMonthSpinner() {
        val currentMonth = UmmalquraCalendar().get(UmmalquraCalendar.MONTH)
        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMonth.adapter = monthAdapter
        spinnerMonth.setSelection(currentMonth)
    }

    private fun setupYearPicker() {
        val currentYear = UmmalquraCalendar().get(UmmalquraCalendar.YEAR)+1
        val minYear = currentYear - 100
        val maxYear = currentYear + 100

        val numberPickerYear = findViewById<NumberPicker>(R.id.yearPicker)

        numberPickerYear.minValue = minYear
        numberPickerYear.maxValue = maxYear

        numberPickerYear.setFormatter { value ->
            String.format("%d AH", value) // Format the value as "XXXX AH"
        }
        numberPickerYear.value = currentYear-1


        numberPickerYear.setOnValueChangedListener { a, b, newYear ->
            updateDayPicker()
        }
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

        val calendar = UmmalquraCalendar(Locale("ar", "SA", "@calendar=islamic-umalqura"))
        calendar.set(UmmalquraCalendar.YEAR, selectedYear)
        calendar.set(UmmalquraCalendar.MONTH, selectedMonth)

        val daysInMonth = calendar.lengthOfMonth()
        val days = (1..daysInMonth).toList()

        val dayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, days)
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDay.adapter = dayAdapter
    }

    private fun updateDayPicker() {
        val selectedMonth =
            findViewById<NumberPicker>(R.id.monthPicker).value - 1 // NumberPicker values start at 0, UmmalquraCalendar months at 1
        val selectedYear = findViewById<NumberPicker>(R.id.yearPicker).value

        val calendar = UmmalquraCalendar(Locale("ar", "SA", "@calendar=islamic-umalqura"))
        calendar.set(UmmalquraCalendar.YEAR, selectedYear)
        calendar.set(UmmalquraCalendar.MONTH, selectedMonth)

        val daysInMonth = calendar.lengthOfMonth()

        val numberPickerDay = findViewById<NumberPicker>(R.id.dayPicker)

        numberPickerDay.minValue = 1
        numberPickerDay.maxValue = daysInMonth

        // Reset the day if the current day is out of range
        if (numberPickerDay.value > daysInMonth) {
            numberPickerDay.value = daysInMonth
        }

    }
}