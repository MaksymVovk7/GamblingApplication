package com.megawild.roboo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.megawild.roboo.databinding.ActivityGamesMenuBinding

class GamesMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGamesMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamesMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findMinMaxValue()

        val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("userName", null)
        if (userName != null) {
            binding.imageViewLabelLevel1.visibility = View.VISIBLE

            binding.imageViewFlash1Game.setImageResource(R.drawable.flash1_registred_menu)
            binding.imageViewFlash2Game.setImageResource(R.drawable.flash2_registred_menu)
            binding.imageViewFlash3Game.setImageResource(R.drawable.flash3_registred_menu)

            binding.imageViewFlash1Game.setOnClickListener {
                startActivity(Intent(this, BonusGameActivity::class.java))
            }
            binding.imageViewFlash2Game.setOnClickListener {
                startActivity(Intent(this, SecondBonusGameActivity::class.java))
            }

            binding.imageViewSlot1Game.setImageResource(R.drawable.slot1_registred_menu)
            binding.imageViewSlot2Game.setImageResource(R.drawable.slot2_registred_menu)
            binding.imageViewSlot3Game.setImageResource(R.drawable.slot3_registred_menu)

            binding.imageViewSlot1Game.setOnClickListener {
                startActivity(Intent(this, FirstGameActivity::class.java))
            }
            binding.imageViewSlot2Game.setOnClickListener {
                startActivity(Intent(this, SecondGameActivity::class.java))
            }

            val list3 = arrayListOf(
                binding.imageViewFlash3Game, binding.imageViewSlot3Game
            )

            list3.forEach {
                it.setOnClickListener {
                    Toast.makeText(this, "Coming soon!", Toast.LENGTH_SHORT).show()
                }
            }

        } else {
            val list = arrayListOf(
                binding.imageViewFlash1Game,
                binding.imageViewFlash2Game,
                binding.imageViewFlash3Game,
                binding.imageViewSlot1Game,
                binding.imageViewSlot2Game,
                binding.imageViewSlot3Game
            )
            list.forEach {
                it.setOnClickListener {
                    Toast.makeText(this, "You need to register to play this game!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.imageViewMainGame.setOnClickListener {
            startActivity(Intent(this, ThirdGameActivity::class.java))
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