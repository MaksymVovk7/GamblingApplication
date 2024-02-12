package com.megawild.roboo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.megawild.roboo.databinding.ActivitySecondBonusGameBinding
import kotlin.random.Random

class SecondBonusGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBonusGameBinding

    private var price = 200
    private var count = 0
    private var total = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBonusGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        findMinMaxValue()
        setUpView()
        startSpin()
    }

    private fun setUpView() {
        val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val balance = sharedPref.getString("balanceGame", null)
        if (balance != null) {
            Scores.BALANCE = Integer.parseInt(balance)
        }
        binding.textViewBetBonus2.text = price.toString()
        binding.textViewBalanceNumBonus2Game.text = Scores.BALANCE.toString()
        binding.textViewWinNumBonus2Game.text = Scores.WIN.toString()
        binding.imageViewBtnMinusBonus2Game.setOnClickListener {
            if (price >= 50) {
                price -= 50
                binding.textViewBetBonus2.text = price.toString()
            }
        }
        binding.imageViewBtnPlusBonus2Game.setOnClickListener {
            price += 50
            binding.textViewBetBonus2.text = price.toString()
        }
    }

    private fun startSpin() {

        val iconList = arrayListOf(
            binding.imageViewIcon1Bonus2, binding.imageViewIcon2Bonus2, binding.imageViewIcon3Bonus2,
            binding.imageViewIcon4Bonus2, binding.ImageViewIcon5Bonus2, binding.imageViewIcon6Bonus2,
            binding.imageViewIcon7Bonus2, binding.imageViewIcon8Bonus2, binding.imageViewIcon9Bonus2
        )

        binding.imageViewBtnSpinBonus2Game.setOnClickListener {
            if (Integer.parseInt(binding.textViewBetBonus2.text.toString()) != 0) {
                setEnabled(false)
                resetBonusGame()
                showToast("The game is starting! You can interact with game field!")
                Scores.BALANCE -= price
                binding.textViewBalanceNumBonus2Game.text = Scores.BALANCE.toString()
                iconList.forEach { image ->
                    image.isEnabled = true
                    image.setOnClickListener {
                        when (Random.nextInt(0, 5)) {
                            Utils.icon5LoseSecondBonus -> {
                                //image.setImageResource(R.drawable.empty_slot_bonus2)
                                image.visibility = View.INVISIBLE
                                showToast("You lose! To play again press button Spin!")
                                count = 0
                                total = 0
                                setInactive()
                                setEnabled(true)
                            }

                            Utils.icon1WinSecondBonus -> {
                                count++
                                total += price
                                image.setImageResource(R.drawable.robo_icon1_bonus2)
                                image.isEnabled = false
                                showWinMessage(count)
                            }

                            Utils.icon2WinSecondBonus -> {
                                count++
                                total += price
                                image.setImageResource(R.drawable.robo_icon2_bonus2)
                                image.isEnabled = false
                                showWinMessage(count)
                            }

                            Utils.icon3WinSecondBonus -> {
                                count++
                                total += price
                                image.setImageResource(R.drawable.robo_icon3_bonus2)
                                image.isEnabled = false
                                showWinMessage(count)
                            }

                            Utils.icon4WinSecondBonus -> {
                                count++
                                total += price
                                image.setImageResource(R.drawable.robo_icon4_bonus2)
                                image.isEnabled = false
                                showWinMessage(count)
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this, "The rate should be greater then 0!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setScores(message: String) {
        showToast(message)
        Scores.WIN += price
        binding.textViewWinNumBonus2Game.text = Scores.WIN.toString()
        Scores.BALANCE += total
        binding.textViewBalanceNumBonus2Game.text = Scores.BALANCE.toString()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showWinMessage(count: Int) {
        if (count == 9) {
            setScores("You win! To play again press button Spin!")
            this.count = 0
            this.total = 0
            setEnabled(true)
        } else {
            setScores("You win,continue!")
        }
    }

    private fun setEnabled(enabled: Boolean) {
        binding.imageViewBtnSpinBonus2Game.isEnabled = enabled
        binding.imageViewBtnPlusBonus2Game.isEnabled = enabled
        binding.imageViewBtnMinusBonus2Game.isEnabled = enabled
    }

    private fun resetBonusGame() {
        val iconList = arrayListOf(
            binding.imageViewIcon1Bonus2, binding.imageViewIcon2Bonus2, binding.imageViewIcon3Bonus2,
            binding.imageViewIcon4Bonus2, binding.ImageViewIcon5Bonus2, binding.imageViewIcon6Bonus2,
            binding.imageViewIcon7Bonus2, binding.imageViewIcon8Bonus2, binding.imageViewIcon9Bonus2
        )

        iconList.forEach {
            it.setImageResource(R.drawable.robo_icon_unknown_bonus2)
            it.isEnabled = false
            it.visibility = View.VISIBLE
        }
        count = 0
    }

    private fun setInactive() {
        val iconList = arrayListOf(
            binding.imageViewIcon1Bonus2, binding.imageViewIcon2Bonus2, binding.imageViewIcon3Bonus2,
            binding.imageViewIcon4Bonus2, binding.ImageViewIcon5Bonus2, binding.imageViewIcon6Bonus2,
            binding.imageViewIcon7Bonus2, binding.imageViewIcon8Bonus2, binding.imageViewIcon9Bonus2
        )

        iconList.forEach {
            it.isEnabled = false
        }
    }

    override fun onPause() {
        super.onPause()
        val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("userName", null)
        val editor = sharedPref.edit()
        if (userName != null) {
            editor.apply {
                putString("balanceGame", Scores.BALANCE.toString())
                apply()
            }
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