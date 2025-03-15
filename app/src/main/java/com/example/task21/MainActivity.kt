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
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Set up button click listener
        findViewById<Button>(R.id.btnConvert).setOnClickListener {
            convertUnits()
        }
    }

    private fun convertUnits() {
        val inputValue = findViewById<EditText>(R.id.etInputValue).text.toString().toDoubleOrNull()
        val sourceUnit = findViewById<Spinner>(R.id.spinnerSourceUnit).selectedItem.toString()
        val destinationUnit = findViewById<Spinner>(R.id.spinnerDestinationUnit).selectedItem.toString()

        if (inputValue == null) {
            findViewById<TextView>(R.id.tvResult).text = "Invalid input"
            return
        }

        val result = when {
            sourceUnit in lengthUnits && destinationUnit in lengthUnits -> convertLength(inputValue, sourceUnit, destinationUnit)
            sourceUnit in weightUnits && destinationUnit in weightUnits -> convertWeight(inputValue, sourceUnit, destinationUnit)
            sourceUnit in tempUnits && destinationUnit in tempUnits -> convertTemperature(inputValue, sourceUnit, destinationUnit)
            else -> "Invalid conversion"
        }

        findViewById<TextView>(R.id.tvResult).text = result.toString()
    }

    private fun convertLength(value: Double, source: String, destination: String): Double {
        // Implement length conversion logic
        return when (source to destination) {
            "Inch" to "Centimeter" -> value * 2.54
            "Foot" to "Centimeter" -> value * 30.48
            "Yard" to "Centimeter" -> value * 91.44
            "Mile" to "Kilometer" -> value * 1.60934
            else -> value // Default case (no conversion)
        }
    }

    private fun convertWeight(value: Double, source: String, destination: String): Double {
        // Implement weight conversion logic
        return when (source to destination) {
            "Pound" to "Kilogram" -> value * 0.453592
            "Ounce" to "Gram" -> value * 28.3495
            "Ton" to "Kilogram" -> value * 907.185
            else -> value // Default case (no conversion)
        }
    }

    private fun convertTemperature(value: Double, source: String, destination: String): Double {
        // Implement temperature conversion logic
        return when (source to destination) {
            "Celsius" to "Fahrenheit" -> (value * 1.8) + 32
            "Fahrenheit" to "Celsius" -> (value - 32) / 1.8
            "Celsius" to "Kelvin" -> value + 273.15
            "Kelvin" to "Celsius" -> value - 273.15
            else -> value // Default case (no conversion)
        }
    }
}