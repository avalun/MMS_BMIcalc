package com.example.mms_bmicalc

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlin.math.pow

class FirstFragment : Fragment() {

    val ORANGE = Color.rgb(255,140,0)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Button: Calculate BMI
        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            calculateAndUpdateBMI(view, false)

            // Hide virtual keyboard
            view.findViewById<EditText>(R.id.edit_height).onEditorAction(EditorInfo.IME_ACTION_DONE);
            view.findViewById<EditText>(R.id.edit_weight).onEditorAction(EditorInfo.IME_ACTION_DONE);
        }
    }

    fun assertHeight(height: Float, isImperial: Boolean): Boolean {
        var minHeight = if (isImperial) 40f else 100f
        var maxHeight = if (isImperial) 100f else 250f
        var unit = if (isImperial) "in" else "cm"

        if(height < minHeight || height > maxHeight) {
            var errorMessage = "Height should be between %.0f%s and %.0f%s.".format(minHeight, unit, maxHeight, unit)
            Toast.makeText(App.getAppContext(), errorMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun assertWeight(weight: Float, isImperial: Boolean): Boolean {
        var minWeight = if (isImperial) 40f else 20f
        var maxWeight = if (isImperial) 600f else 300f
        var unit = if (isImperial) "lbs" else "kg"

        if(weight < minWeight || weight > maxWeight) {
            var errorMessage = "Weight should be between %.0f%s and %.0f%s.".format(minWeight, unit, maxWeight, unit)
            Toast.makeText(App.getAppContext(), errorMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    // Takes bmi, evaluates it and returns:
    // Green if everything is okay
    // Orange if you are close to being at risk
    // Red if you are at risk
    // Data taken from the table found on https://en.wikipedia.org/wiki/Body_mass_index#Categories
    fun lookupBMI(bmi: Float):Int {
        // Lookup values for bmi
        var severelyUnderweight = 0.0..16.0
        var underweight = 16.0..18.5
        var overweight = 25.0..30.0
        var severelyOverweight = 30.0..100.0

        if(bmi in severelyOverweight || bmi in severelyUnderweight){
            return Color.RED
        }
        if (bmi in overweight || bmi in underweight){
            return ORANGE
        }
        // Else weight is in normal range
        return Color.GREEN
    }

    fun calculateAndUpdateBMI(view: View, isImperial: Boolean) {
        var editHeightText = view.findViewById<EditText>(R.id.edit_height).text.toString()
        var editWeightText = view.findViewById<EditText>(R.id.edit_weight).text.toString()

        // Make sure text fields are not empty
        if (TextUtils.isEmpty(editHeightText) || TextUtils.isEmpty(editWeightText)) {
            Toast.makeText(App.getAppContext(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show()
            return
        }

        var height = editHeightText.toFloat()
        var weight = editWeightText.toFloat()

        // Check if supplied values are valid
        if(assertHeight(height, isImperial) && assertWeight(weight, isImperial)){
            // Calculate BMI - 703 is a conversion factor when using imperial units
            var bmi = if(isImperial) 703 * weight / height.pow(2)
             else weight / (height/100f).pow(2)

            // Set text to display BMI
            view.findViewById<TextView>(R.id.calculated_bmi).text = "%.2f".format(bmi)
            view.findViewById<TextView>(R.id.calculated_bmi).setTextColor(lookupBMI(bmi))
        }
    }
}
