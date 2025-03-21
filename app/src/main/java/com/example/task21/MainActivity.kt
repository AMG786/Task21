package com.example.task21

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.text.Editable
import android.text.TextWatcher
/*
Created by Abdul Mueez on 3/13/2025
 */
class MainActivity : AppCompatActivity() {

    // Declare arrays as class level variables
    private lateinit var lengthUnits: Array<String>
    private lateinit var weightUnits: Array<String>
    private lateinit var tempUnits: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize arrays
        lengthUnits = resources.getStringArray(R.array.length_units)
        weightUnits = resources.getStringArray(R.array.weight_units)
        tempUnits = resources.getStringArray(R.array.temperature_units)

        // Set up category spinner
        val categorySpinner = findViewById<Spinner>(R.id.spinnerCategory)
        val categories = resources.getStringArray(R.array.categories)
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        // Set up unit spinners
        val sourceUnitSpinner = findViewById<Spinner>(R.id.spinnerSourceUnit)
        val destinationUnitSpinner = findViewById<Spinner>(R.id.spinnerDestinationUnit)

        // Initial setup to prevent null selection
        val unitAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, lengthUnits)
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sourceUnitSpinner.adapter = unitAdapter
        destinationUnitSpinner.adapter = unitAdapter

        // Handle category selection
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = parent?.getItemAtPosition(position).toString()
                val units = when (selectedCategory) {
                    "Length" -> lengthUnits
                    "Weight" -> weightUnits
                    "Temperature" -> tempUnits
                    else -> emptyArray()
                }

                val unitAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, units)
                unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sourceUnitSpinner.adapter = unitAdapter
                destinationUnitSpinner.adapter = unitAdapter

                // Reset result text
                findViewById<TextView>(R.id.tvResult).text = "0"
                findViewById<TextView>(R.id.tvResult).setTextColor(resources.getColor(R.color.primary, theme))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Add TextWatcher to clear errors when user starts typing
        findViewById<EditText>(R.id.etInputValue).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                findViewById<EditText>(R.id.etInputValue).error = null
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Set up button click listener
        findViewById<Button>(R.id.btnConvert).setOnClickListener {
            convertUnits()
        }
    }

    private fun convertUnits() {
        val inputEditText = findViewById<EditText>(R.id.etInputValue)
        val inputStr = inputEditText.text.toString().trim()
        val sourceUnit = findViewById<Spinner>(R.id.spinnerSourceUnit).selectedItem.toString()
        val destinationUnit = findViewById<Spinner>(R.id.spinnerDestinationUnit).selectedItem.toString()
        val resultTextView = findViewById<TextView>(R.id.tvResult)

        // Validate input is not empty
        if (inputStr.isEmpty()) {
            showError(resultTextView, "Please enter a value")
            inputEditText.error = "Required field"
            return
        }

        // Validate input is a valid number
        val inputValue = try {
            inputStr.toDouble()
        } catch (e: NumberFormatException) {
            showError(resultTextView, "Invalid number format")
            inputEditText.error = "Enter a valid number"
            return
        }

        // Check for negative values where they don't make sense
        if (inputValue < 0 && sourceUnit in tempUnits && sourceUnit != "Celsius" && sourceUnit != "Fahrenheit") {
            showError(resultTextView, "Value cannot be negative for this unit")
            return
        }

        // Handle case where source and destination units are the same
        if (sourceUnit == destinationUnit) {
            resultTextView.text = formatResult(inputValue)
            resultTextView.setTextColor(resources.getColor(R.color.primary, theme))
            return
        }

        // Perform conversion based on unit types
        try {
            val result = when {
                sourceUnit in lengthUnits && destinationUnit in lengthUnits ->
                    convertLength(inputValue, sourceUnit, destinationUnit)
                sourceUnit in weightUnits && destinationUnit in weightUnits ->
                    convertWeight(inputValue, sourceUnit, destinationUnit)
                sourceUnit in tempUnits && destinationUnit in tempUnits ->
                    convertTemperature(inputValue, sourceUnit, destinationUnit)
                else -> throw IllegalArgumentException("Incompatible unit types")
            }

            resultTextView.text = formatResult(result)
            resultTextView.setTextColor(resources.getColor(R.color.primary, theme))
        } catch (e: Exception) {
            showError(resultTextView, "Conversion error: ${e.message}")
        }
    }

    // Helper function to display error messages
    private fun showError(textView: TextView, message: String) {
        textView.text = message
        // Use a standard error color that's available in your resources
        textView.setTextColor(resources.getColor(android.R.color.holo_red_dark, theme))
        // Or you could define your own error color in colors.xml and use:
        // textView.setTextColor(ContextCompat.getColor(this, R.color.error))
    }

    private fun convertLength(value: Double, source: String, destination: String): Double {
        // Convert everything to meters first, then to target unit
        val inMeters = when (source) {
            "Inch" -> value * 0.0254
            "Foot" -> value * 0.3048
            "Yard" -> value * 0.9144
            "Mile" -> value * 1609.34
            "Centimeter" -> value * 0.01
            "Kilometer" -> value * 1000
            else -> value // Same unit, no conversion needed
        }

        return when (destination) {
            "Inch" -> inMeters / 0.0254
            "Foot" -> inMeters / 0.3048
            "Yard" -> inMeters / 0.9144
            "Mile" -> inMeters / 1609.34
            "Centimeter" -> inMeters / 0.01
            "Kilometer" -> inMeters / 1000
            else -> inMeters // Same unit, no conversion needed
        }
    }

    private fun convertWeight(value: Double, source: String, destination: String): Double {
        // Convert everything to kilograms first, then to target unit
        val inKilograms = when (source) {
            "Pound" -> value * 0.453592
            "Ounce" -> value * 0.0283495
            "Ton" -> value * 907.185
            "Gram" -> value * 0.001
            "Kilogram" -> value
            else -> value // Same unit, no conversion needed
        }

        return when (destination) {
            "Pound" -> inKilograms / 0.453592
            "Ounce" -> inKilograms / 0.0283495
            "Ton" -> inKilograms / 907.185
            "Gram" -> inKilograms / 0.001
            "Kilogram" -> inKilograms
            else -> inKilograms // Same unit, no conversion needed
        }
    }

    private fun convertTemperature(value: Double, source: String, destination: String): Double {
        // Convert everything to Kelvin first, then to target unit
        val inKelvin = when (source) {
            "Celsius" -> value + 273.15
            "Fahrenheit" -> (value - 32) * 5/9 + 273.15
            "Kelvin" -> value
            else -> value // Same unit, no conversion needed
        }

        return when (destination) {
            "Celsius" -> inKelvin - 273.15
            "Fahrenheit" -> (inKelvin - 273.15) * 9/5 + 32
            "Kelvin" -> inKelvin
            else -> inKelvin // Same unit, no conversion needed
        }
    }

    // Enhanced format function to handle potential floating point issues
    private fun formatResult(value: Double): String {
        // Handle potential floating point precision issues
        val roundedValue = if (Math.abs(value) < 0.00001) {
            0.0
        } else {
            value
        }

        // Format to 4 decimal places and remove trailing zeros
        return if (roundedValue == roundedValue.toLong().toDouble()) {
            roundedValue.toLong().toString()
        } else {
            String.format("%.4f", roundedValue).replace(Regex("0+$"), "").replace(Regex("\\.$"), "")
        }
    }
}