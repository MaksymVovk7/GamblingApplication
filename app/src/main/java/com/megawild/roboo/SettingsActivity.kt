package com.megawild.roboo

import android.content.Context
import android.media.AudioManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.Toast
import com.megawild.roboo.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findMinMaxValue()

        toggleSoundBar()

        toggleMusicBar()

        binding.buttonReset.setOnClickListener {
            Scores.resetScore()
            val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
            val userName = sharedPref.getString("userName", null)
            val editor = sharedPref.edit()
            if (userName != null) {
                editor.apply {
                    putString("balanceGame", Scores.BALANCE.toString())
                    apply()
                }
            }
            showToast("Score was reset to default!")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@SettingsActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSoundBarOn() {
        binding.imageViewSoundBarOn.visibility = View.GONE
        binding.imageViewSoundBarOff.visibility = View.VISIBLE
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val minVolume = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            audioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC)
        } else {
            0
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, minVolume, 0)
        showToast("Sound is turn off!")
    }

    private fun setSoundBarOff() {
        binding.imageViewSoundBarOff.visibility = View.GONE
        binding.imageViewSoundBarOn.visibility = View.VISIBLE
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
        showToast("Sound is turn on!")
    }

    private fun setMusicBarOn() {
        binding.imageViewMusicBarOn.visibility = View.GONE
        binding.imageViewMusicBarOff.visibility = View.VISIBLE
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.cancel()
        showToast("Vibrations is turn off!")
    }

    private fun setMusicBarOff() {
        binding.imageViewMusicBarOff.visibility = View.GONE
        binding.imageViewMusicBarOn.visibility = View.VISIBLE
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(500)
        }
        showToast("Vibrations is turn on!")
    }

    private fun toggleSoundBar() {

        binding.imageViewSoundBarOn.setOnClickListener {
            setSoundBarOn()
        }

        binding.imageViewSoundBarOff.setOnClickListener {
            setSoundBarOff()
        }
    }

    private fun toggleMusicBar() {
        binding.imageViewMusicBarOn.setOnClickListener {
            setMusicBarOn()
        }

        binding.imageViewMusicBarOff.setOnClickListener {
            setMusicBarOff()
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