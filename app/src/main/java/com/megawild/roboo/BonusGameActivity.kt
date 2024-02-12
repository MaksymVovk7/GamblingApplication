package com.megawild.roboo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.megawild.roboo.databinding.ActivityBonusGameBinding
import kotlin.random.Random

class BonusGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBonusGameBinding

    private var price = 200
    private var count = 0
    private var total = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBonusGameBinding.inflate(layoutInflater)
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
        binding.textViewBetBonusGame.text = price.toString()
        binding.textViewBalanceNumBonusGame.text = Scores.BALANCE.toString()
        binding.textViewWinNumBonusGame.text = Scores.WIN.toString()
        binding.imageViewBtnMinusBonusGame.setOnClickListener {
            if (price >= 50) {
                price -= 50
                binding.textViewBetBonusGame.text = price.toString()
            }
        }
        binding.imageViewBtnPlusBonusGame.setOnClickListener {
            price += 50
            binding.textViewBetBonusGame.text = price.toString()
        }
    }

    private fun startSpin() {

        val iconList = arrayListOf(
            binding.imageViewIcon1BonusGame,
            binding.imageViewIcon2BonusGame,
            binding.imageViewIcon3BonusGame,
            binding.imageViewIcon4BonusGame
        )

        binding.imageViewBtnSpinBonusGame.setOnClickListener {
            if (Integer.parseInt(binding.textViewBetBonusGame.text.toString()) != 0) {
                setEnabled(false)
                setRoboBonusGame()
                showToast("The game is starting! You can interact with game field!")
                Scores.BALANCE -= price
                binding.textViewBalanceNumBonusGame.text = Scores.BALANCE.toString()
                iconList.forEach { image ->
                    image.isEnabled = true
                    image.setOnClickListener {
                        when (Random.nextInt(0, 3)) {
                            Utils.icon3LoseBonus -> {
                                image.setImageResource(R.drawable.robo_icon3_bonus)
                                showToast("You lose! To play again press button Spin!")
                                count = 0
                                total = 0
                                setNotEnablePlayArea()
                                setEnabled(true)
                            }

                            Utils.icon1WinBonus -> {
                                count++
                                total += price
                                image.setImageResource(R.drawable.robo_icon1_bonus)
                                image.isEnabled = false
                                showWinMessage(count)
                            }

                            Utils.icon2WinBonus -> {
                                count++
                                total += price
                                image.setImageResource(R.drawable.robo_icon2_bonus)
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
        binding.textViewWinNumBonusGame.text = Scores.WIN.toString()
        Scores.BALANCE += total
        binding.textViewBalanceNumBonusGame.text = Scores.BALANCE.toString()
    }

    private fun showToast(message: String) {
        Toast.makeText(this@BonusGameActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun showWinMessage(count: Int) {
        if (count == 4) {
            setScores("You win! To play again press button Spin!")
            this.count = 0
            this.total = 0
            setEnabled(true)
        } else {
            setScores("You win,continue!")
        }
    }

    private fun setEnabled(enabled: Boolean) {
        binding.imageViewBtnSpinBonusGame.isEnabled = enabled
        binding.imageViewBtnPlusBonusGame.isEnabled = enabled
        binding.imageViewBtnMinusBonusGame.isEnabled = enabled
    }

    private fun setRoboBonusGame() {
        val listOfImages = getListOfImages()

        for(imageItem in listOfImages) {
            imageItem.setImageResource(R.drawable.robo_icon_unknown)
            imageItem.isEnabled = false
        }
        count = 0
    }

    private fun setNotEnablePlayArea() {
        val listGameIcons = getListOfImages()

        for (item in listGameIcons) {
            item.isEnabled = false
        }
    }

    private fun getListOfImages(): ArrayList<ImageView> {
        return arrayListOf(
            binding.imageViewIcon1BonusGame,
            binding.imageViewIcon2BonusGame,
            binding.imageViewIcon3BonusGame,
            binding.imageViewIcon4BonusGame
        )
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