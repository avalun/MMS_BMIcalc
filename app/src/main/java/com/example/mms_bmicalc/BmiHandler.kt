package com.example.mms_bmicalc

import android.graphics.Color
import kotlin.math.pow

class BmiHandler {

    enum class Classification{
        SEVERELYUNDERWEIGHT,
        UNDERWEIGHT,
        NORMAL,
        OVERWEIGHT,
        SEVERELYOVERWEIGHT,
        UNDEFINED
    }

    private val ORANGE = Color.rgb(255,140,0)

    var BMI = -1f
    var classification = Classification.UNDEFINED

    fun calculate(height: Float, weight: Float, isImperial: Boolean): Float {
        // Calculate BMI - 703 is a conversion factor when using imperial units
        BMI = if(isImperial) 703 * weight / height.pow(2)
        else weight / (height/100f).pow(2)

        classify()

        return BMI
    }

    // Takes bmi, evaluates it and returns classification according to data
    // found on https://en.wikipedia.org/wiki/Body_mass_index#Categories
    fun classify() {
        // Ranges for classification
        val severelyUnderweight = 0.0..16.0
        val underweight = 16.0..18.5
        val overweight = 25.0..30.0
        val severelyOverweight = 30.0..100.0

        classification = when (BMI) {
            in severelyUnderweight -> Classification.SEVERELYUNDERWEIGHT
            in underweight -> Classification.UNDERWEIGHT
            in overweight -> Classification.OVERWEIGHT
            in severelyOverweight -> Classification.SEVERELYOVERWEIGHT
            else -> Classification.NORMAL
        }
    }

    fun colorBMI(): Int {
        return when (classification) {
            Classification.SEVERELYUNDERWEIGHT -> Color.RED
            Classification.UNDERWEIGHT -> ORANGE
            Classification.OVERWEIGHT -> ORANGE
            Classification.SEVERELYOVERWEIGHT -> Color.RED
            Classification.UNDEFINED -> Color.BLACK
            else -> Color.GREEN
        }
    }

}