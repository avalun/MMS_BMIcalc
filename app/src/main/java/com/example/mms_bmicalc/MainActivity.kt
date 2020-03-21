package com.example.mms_bmicalc

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text


class MainActivity : AppCompatActivity() {

    private var bmiHandler = BmiHandler()
    private var isImperial = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // On click listenes for BMI calculate button
        findViewById<Button>(R.id.button_first).setOnClickListener {
            updateBMI()

            // Hide virtual keyboard
            findViewById<EditText>(R.id.edit_height).onEditorAction(EditorInfo.IME_ACTION_DONE)
            findViewById<EditText>(R.id.edit_weight).onEditorAction(EditorInfo.IME_ACTION_DONE)
        }

        // On click listener for BMI value
        findViewById<TextView>(R.id.calculated_bmi).setOnClickListener {
            if(bmiHandler.BMI > 0) {
                val intent = Intent(this, BmiInfo::class.java)
                // To pass any data to next activity
                intent.putExtra("bmi", bmiHandler.BMI)
                intent.putExtra("class", bmiHandler.classification)
                // start your next activity
                startActivity(intent)
            }
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
                val intent = Intent(this, AuthorActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

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

    private fun updateBMI() {
        // Get values of text fields
        val editHeightText = findViewById<EditText>(R.id.edit_height).text.toString()
        val editWeightText = findViewById<EditText>(R.id.edit_weight).text.toString()

        // Make sure text fields are not empty
        if (TextUtils.isEmpty(editHeightText) || TextUtils.isEmpty(editWeightText)) {
            Toast.makeText(App.appContext, getString(R.string.errormsg_emptyFields), Toast.LENGTH_SHORT).show()
            return
        }

        // Cast to float for further usage
        val height = editHeightText.toFloat()
        val weight = editWeightText.toFloat()

        // Check if supplied values are in valid range
        if(assertHeight(height) && assertWeight(weight)){
            // Set text to display BMI
            findViewById<TextView>(R.id.calculated_bmi).text = getString(R.string.float2decimals).format(
                bmiHandler.calculate(height, weight, isImperial))
            // With the appropriate color
            findViewById<TextView>(R.id.calculated_bmi).setTextColor(bmiHandler.colorBMI())
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

