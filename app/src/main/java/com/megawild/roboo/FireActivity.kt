package com.megawild.roboo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.megawild.roboo.databinding.ActivityMainBinding
import io.michaelrocks.paranoid.Obfuscate

@Obfuscate
class FireActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findMinMaxValue()

        val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("userName", null)
        if (userName != null) {
            startActivity(Intent(this@FireActivity, MenuActivity::class.java))
            finish()
        }
        val editor = sharedPref.edit()

        binding.buttonLoginStart.setOnClickListener {
            if (isValidName(binding.editTextUserName.text.toString())) {
                editor.apply {
                    putString("userName", binding.editTextUserName.text.toString())
                    apply()
                }
                Scores.resetScore()
                startActivity(Intent(this@FireActivity, MenuActivity::class.java))
            } else {
                Toast.makeText(
                    this@FireActivity,
                    "Name should be at least 3 alphabetic characters!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.textViewMode.setOnClickListener {
            startActivity(Intent(this@FireActivity, MenuActivity::class.java))
        }
    }

    private fun isValidName(name: String): Boolean {
        if (name.length < 3) {
            return false
        }

        if (!name.matches("[a-zA-Z]+".toRegex())) {
            return false
        }

        return true
    }

    // мусорный код
    private fun findMinMaxValue() {
        val numbers = arrayListOf(20, 0, 5, 6, 23, 98, 1, 7, 8, 54)
        var max = numbers[0]
        var min = numbers[0]

        for (i in 0 until numbers.size) {
            if (max < numbers[i]) {
                max = numbers[i]
            }

            if (min > numbers[i]) {
                min = numbers[i]
            }
        }

        Log.d("MaxMinValue", "Max value = $max, min value = $min")
    }

    fun runMain() {
        //val romanNumeral = "D"
        //https://pl.kotl.in/wkoaOR2pg
        val listOfRoman = arrayListOf("XD", "MCMXC", "XIX", "RFZ")
        listOfRoman.forEach { romanNumeral ->
            if (isValidRoman(romanNumeral.uppercase())) {
                val decimalNumber = romanToDecimal(romanNumeral.uppercase())
                println("$romanNumeral = $decimalNumber")
            } else {
                println("$romanNumeral = incorrect roman format")
            }
        }
//        if (isValidRoman(romanNumeral.uppercase())) {
//            val decimalNumber = romanToDecimal(romanNumeral.uppercase())
//            println("$romanNumeral = $decimalNumber")
//        } else {
//            println("$romanNumeral = incorrect roman format")
//        }

    }

    fun romanToDecimal(roman: String): Int {
        val romanNumerals = mapOf(
            'I' to 1,
            'V' to 5,
            'X' to 10,
            'L' to 50,
            'C' to 100,
            'D' to 500,
            'M' to 1000
        )

        var result = 0
        var prevValue = 0

        for (i in roman.length - 1 downTo 0) {
            val currentSymbol = roman[i]
            val currentValue = romanNumerals[currentSymbol] ?: 0

            if (currentValue < prevValue) {
                result -= currentValue
            } else {
                result += currentValue
            }

            prevValue = currentValue
        }

        return result
    }

    fun isValidRoman(roman: String): Boolean {
        val validRomanRegex = "^[IVXLCDM]*$".toRegex()
        return roman.matches(validRomanRegex)
    }
}