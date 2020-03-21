package com.example.mms_bmicalc

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    private val ORANGE = Color.rgb(255,140,0)
    private var isImperial = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        findViewById<Button>(R.id.button_first).setOnClickListener {
            calculateAndUpdateBMI()

            // Hide virtual keyboard
            findViewById<EditText>(R.id.edit_height).onEditorAction(EditorInfo.IME_ACTION_DONE)
            findViewById<EditText>(R.id.edit_weight).onEditorAction(EditorInfo.IME_ACTION_DONE)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_metric -> {
                switchToMetric()
                return true
            }
            R.id.action_imperial -> {
                switchToImperial()
                return true
            }
            R.id.action_aboutAuthor -> {
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Button: Calculate BMI
        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            calculateAndUpdateBMI(view)

            // Hide virtual keyboard
            view.findViewById<EditText>(R.id.edit_height).onEditorAction(EditorInfo.IME_ACTION_DONE);
            view.findViewById<EditText>(R.id.edit_weight).onEditorAction(EditorInfo.IME_ACTION_DONE);
        }
    }*/

    private fun assertHeight(height: Float): Boolean {
        val minHeight = if (isImperial) 40f else 100f
        val maxHeight = if (isImperial) 100f else 250f
        val unit = if (isImperial) "in" else "cm"

        if(height < minHeight || height > maxHeight) {
            val errorMessage = getString(R.string.errormsg_invHeight).format(minHeight, unit, maxHeight, unit)
            Toast.makeText(App.appContext, errorMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun assertWeight(weight: Float): Boolean {
        val minWeight = if (isImperial) 40f else 20f
        val maxWeight = if (isImperial) 600f else 300f
        val unit = if (isImperial) "lbs" else "kg"

        if(weight < minWeight || weight > maxWeight) {
            val errorMessage = getString(R.string.errormsg_invWeight).format(minWeight, unit, maxWeight, unit)
            Toast.makeText(App.appContext, errorMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    // Takes bmi, evaluates it and returns:
    // Green if everything is okay
    // Orange if you are close to being at risk
    // Red if you are at risk
    // Data taken from the table found on https://en.wikipedia.org/wiki/Body_mass_index#Categories
    private fun lookupBMI(bmi: Float):Int {
        // Lookup values for bmi
        val severelyUnderweight = 0.0..16.0
        val underweight = 16.0..18.5
        val overweight = 25.0..30.0
        val severelyOverweight = 30.0..100.0

        if(bmi in severelyOverweight || bmi in severelyUnderweight){
            return Color.RED
        }
        if (bmi in overweight || bmi in underweight){
            return ORANGE
        }
        // Else weight is in normal range
        return Color.GREEN
    }

    private fun calculateAndUpdateBMI() {
        val editHeightText = findViewById<EditText>(R.id.edit_height).text.toString()
        val editWeightText = findViewById<EditText>(R.id.edit_weight).text.toString()

        // Make sure text fields are not empty
        if (TextUtils.isEmpty(editHeightText) || TextUtils.isEmpty(editWeightText)) {
            Toast.makeText(App.appContext, getString(R.string.errormsg_emptyFields), Toast.LENGTH_SHORT).show()
            return
        }

        val height = editHeightText.toFloat()
        val weight = editWeightText.toFloat()

        // Check if supplied values are valid
        if(assertHeight(height) && assertWeight(weight)){
            // Calculate BMI - 703 is a conversion factor when using imperial units
            val bmi = if(isImperial) 703 * weight / height.pow(2)
            else weight / (height/100f).pow(2)

            // Set text to display BMI
            findViewById<TextView>(R.id.calculated_bmi).text = getString(R.string.float2decimals).format(bmi)
            findViewById<TextView>(R.id.calculated_bmi).setTextColor(lookupBMI(bmi))
        }
    }

    private fun switchToMetric() {
        isImperial = false
        findViewById<TextView>(R.id.label_height).text = getString(R.string.label_heightMetric)
        findViewById<EditText>(R.id.edit_height).hint = getString(R.string.label_heightMetric)

        findViewById<TextView>(R.id.label_weight).text = getString(R.string.label_weightMetric)
        findViewById<EditText>(R.id.edit_weight).hint = getString(R.string.label_weightMetric)

        clearEditTexts()
    }

    private fun switchToImperial() {
        isImperial = true
        findViewById<TextView>(R.id.label_height).text = getString(R.string.label_heightImperial)
        findViewById<EditText>(R.id.edit_height).hint = getString(R.string.label_heightImperial)

        findViewById<TextView>(R.id.label_weight).text = getString(R.string.label_weightImperial)
        findViewById<EditText>(R.id.edit_weight).hint = getString(R.string.label_weightImperial)

        clearEditTexts()
    }

    private fun clearEditTexts() {
        findViewById<EditText>(R.id.edit_height).text.clear()
        findViewById<EditText>(R.id.edit_weight).text.clear()
    }
}

