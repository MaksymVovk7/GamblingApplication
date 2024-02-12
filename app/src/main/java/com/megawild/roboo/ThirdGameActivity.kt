package com.megawild.roboo

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import com.megawild.roboo.databinding.ActivityThirdGameBinding

class ThirdGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdGameBinding

    private val sectors = intArrayOf(3,2,0,1,2,1,0,1,2,0)
    private var sectorDegrees = IntArray(sectors.size)

    private var randomSectorIndex = 0
    private var spinning = false

    private var price = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViews()
        generateSectorDegrees()
        binding.imageViewBtnSpinThirdGame.setOnClickListener {
            if (Integer.parseInt(binding.textViewBetThirdGame.text.toString()) != 0) {
                if (!spinning) {
                    binding.imageViewBtnSpinThirdGame.isEnabled = false
                    Scores.BALANCE -= price
                    binding.textViewBalanceNumThirdGame.text = Scores.BALANCE.toString()
                    spinWheel()
                    spinning = true
                }
            } else {
                Toast.makeText(this, "The rate should be greater then 0!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpViews() {
        val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val balance = sharedPref.getString("balanceGame", null)
        if (balance != null) {
            Scores.BALANCE = Integer.parseInt(balance)
        }
        binding.textViewBetThirdGame.text = price.toString()
        binding.textViewBalanceNumThirdGame.text = Scores.BALANCE.toString()
        binding.textViewWinNumThirdGame.text = Scores.WIN.toString()
        binding.imageViewBtnMinusThirdGame.setOnClickListener {
            if (price >= 50) {
                price -= 50
                binding.textViewBetThirdGame.text = price.toString()
            }
        }
        binding.imageViewBtnPlusThirdGame.setOnClickListener {
            price += 50
            binding.textViewBetThirdGame.text = price.toString()
        }
    }

    private fun generateSectorDegrees() {
        val sectorDegree = 362 / sectors.size
        for (i in 0 until sectors.size) {
            sectorDegrees[i] = (i + 1) * sectorDegree
        }
    }

    private fun spinWheel() {
        randomSectorIndex = (0 until sectors.size).random()

        val randomDegree = getRandomDegreeSpinWheel()

        val rotateAnimation = RotateAnimation(
            0f,
            randomDegree.toFloat(),
            RotateAnimation.RELATIVE_TO_SELF,
            0.5f,
            RotateAnimation.RELATIVE_TO_SELF,
            0.5f)

        rotateAnimation.duration = 3600
        rotateAnimation.fillAfter = true
        rotateAnimation.interpolator = DecelerateInterpolator()
        rotateAnimation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                val earnedCoff = sectors[sectors.size - (randomSectorIndex + 1)]
                setBalanceWin(earnedCoff)
                binding.imageViewBtnSpinThirdGame.isEnabled = true
                spinning = false
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
        binding.imageViewWheel.startAnimation(rotateAnimation)
    }

    private fun getRandomDegreeSpinWheel() : Int {
        return (362 * sectors.size) + sectorDegrees[randomSectorIndex]
    }

    private fun setBalanceWin(coff : Int) {
        val winCoff = price * coff
        Scores.WIN += winCoff
        binding.textViewWinNumThirdGame.text = Scores.WIN.toString()
        Scores.BALANCE += winCoff
        binding.textViewBalanceNumThirdGame.text = Scores.BALANCE.toString()
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
}