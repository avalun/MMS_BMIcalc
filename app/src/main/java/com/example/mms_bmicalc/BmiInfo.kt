package com.example.mms_bmicalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class BmiInfo : AppCompatActivity() {

    var bmi = 0f
    var classification = BmiHandler.Classification.UNDEFINED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_info)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = "About your BMI"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bmi = intent.extras?.get("bmi") as Float
        classification = intent.extras?.get("class") as BmiHandler.Classification

        showInfoAndBmi()
    }

    fun showInfoAndBmi() {
        findViewById<TextView>(R.id.bmi_show).text = getString(R.string.float2decimals).format(bmi)

        findViewById<TextView>(R.id.bmi_info).text = when (classification) {
            BmiHandler.Classification.SEVERELYUNDERWEIGHT -> getString(R.string.text_severelyUnderweight)
            BmiHandler.Classification.UNDERWEIGHT -> getString(R.string.text_underweight)
            BmiHandler.Classification.OVERWEIGHT -> getString(R.string.text_ovrweight)
            BmiHandler.Classification.SEVERELYOVERWEIGHT -> getString(R.string.text_severelyOverweight)
            BmiHandler.Classification.NORMAL -> getString(R.string.text_normal)
            else -> "Error"
        }

    }
}
