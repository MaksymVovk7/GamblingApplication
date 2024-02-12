package com.megawild.roboo

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.megawild.roboo.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findMinMaxValue()

        binding.buttonGames.setOnClickListener {
            startActivity(Intent(this@MenuActivity, GamesMenuActivity::class.java))
        }
        binding.buttonSettings.setOnClickListener {
            startActivity(Intent(this@MenuActivity, SettingsActivity::class.java))
        }

        binding.buttonPrivacy.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"))
            startActivity(browserIntent)
        }

        binding.buttonExit.setOnClickListener {
            finishAffinity()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("userName", null)
        if (userName != null) {
            finishAffinity()
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    // мусорный код
    private fun findMinMaxValue() {
        val numbers = arrayListOf(20,0,5,6,23,98,1,7,8,54)
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
}