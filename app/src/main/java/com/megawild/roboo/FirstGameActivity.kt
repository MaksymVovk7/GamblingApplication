package com.megawild.roboo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.megawild.roboo.databinding.ActivityFirstGameBinding
import kotlin.random.Random

class FirstGameActivity : AppCompatActivity(), IEventEnd {

    private lateinit var binding: ActivityFirstGameBinding

    private var countDown = 0

    private var price = 200

    private var isSpinning = false

    private var image1Value = 0
    private var image2Value = 0
    private var image3Value = 0
    private var image4Value = 0
    private var image5Value = 0
    private var image6Value = 0
    private var image7Value = 0
    private var image8Value = 0
    private var image9Value = 0

    companion object {
        private const val KEY_SPINNING_STATE = "Spinning state"
        private const val KEY_IMAGE_1 = "image1"
        private const val KEY_IMAGE_2 = "image2"
        private const val KEY_IMAGE_3 = "image3"
        private const val KEY_IMAGE_4 = "image4"
        private const val KEY_IMAGE_5 = "image5"
        private const val KEY_IMAGE_6 = "image6"
        private const val KEY_IMAGE_7 = "image7"
        private const val KEY_IMAGE_8 = "image8"
        private const val KEY_IMAGE_9 = "image9"
        private const val KEY_BET_VALUE = "bet value"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViews()
        startSpin()
    }

    private fun setUpViews() {
        val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val balance = sharedPref.getString("balanceGame", null)
        if (balance != null) {
            Scores.BALANCE = Integer.parseInt(balance)
        }
        binding.textViewBetGame1.text = price.toString()
        binding.textViewNumTotalGame1.text = Scores.BALANCE.toString()
        binding.textViewNumWinGame1.text = Scores.WIN.toString()
        binding.imageViewBtnMinus.setOnClickListener {
            if (price >= 50) {
                price -= 50
                binding.textViewBetGame1.text = price.toString()
            }
        }
        binding.imageViewBtnPlus.setOnClickListener {
            price += 50
            binding.textViewBetGame1.text = price.toString()
        }
    }

    private fun startSpin() {

        val iconListReel1 = arrayListOf(
            binding.imageViewIcon1Game1,
            binding.imageViewIcon2Game1,
            binding.imageViewIcon3Game1
        )

        val iconListReel2 = arrayListOf(
            binding.imageViewIcon4Game1,
            binding.imageViewIcon5Game1,
            binding.imageViewIcon6Game1
        )

        val iconListReel3 = arrayListOf(
            binding.imageViewIcon7Game1,
            binding.imageViewIcon8Game1,
            binding.imageViewIcon9Game1
        )

        val iconsList = arrayListOf(iconListReel1, iconListReel2, iconListReel3)

        iconsList.forEach { list ->
            list.forEach { image ->
                image.setEventEnd(this@FirstGameActivity)
            }
        }

        binding.imageViewBtnSpinGame1.setOnClickListener {

            if (Scores.BALANCE == 0 || Scores.BALANCE < price) {
                Scores.resetBalance()
            }

            if (Integer.parseInt(binding.textViewBetGame1.text.toString()) != 0) {
                isSpinning = true
                binding.imageViewBtnSpinGame1.isEnabled = false
                Scores.BALANCE -= Integer.parseInt(binding.textViewBetGame1.text.toString())
                binding.textViewNumTotalGame1.text = Scores.BALANCE.toString()

                iconListReel1.forEach {
                    it.setValueRandom(Random.nextInt(5), 15)
                }

                iconListReel2.forEach {
                    it.setValueRandom(Random.nextInt(5), 20)
                }

                iconListReel3.forEach {
                    it.setValueRandom(Random.nextInt(5), 25)
                }
            } else {
                Toast.makeText(this@FirstGameActivity, "The rate should be greater then 0!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun eventEnd(result: Int, count: Int) {

        if (countDown < 8) {
            countDown++
        } else {
            countDown = 0
            isSpinning = false
            binding.imageViewBtnSpinGame1.isEnabled = true

            image1Value = binding.imageViewIcon1Game1.value
            image2Value = binding.imageViewIcon2Game1.value
            image3Value = binding.imageViewIcon3Game1.value
            image4Value = binding.imageViewIcon4Game1.value
            image5Value = binding.imageViewIcon5Game1.value
            image6Value = binding.imageViewIcon6Game1.value
            image7Value = binding.imageViewIcon7Game1.value
            image8Value = binding.imageViewIcon8Game1.value
            image9Value = binding.imageViewIcon9Game1.value

            if (binding.imageViewIcon4Game1.value == binding.imageViewIcon5Game1.value
                && binding.imageViewIcon5Game1.value == binding.imageViewIcon6Game1.value
            ) {
                setWinScore(3)
            } else if (binding.imageViewIcon4Game1.value == binding.imageViewIcon5Game1.value
                || binding.imageViewIcon5Game1.value == binding.imageViewIcon6Game1.value
                || binding.imageViewIcon4Game1.value == binding.imageViewIcon6Game1.value
            ) {
                setWinScore(2)
            }
        }

    }

    private fun setWinScore(coff: Int) {
        val winCoff = price * coff
        Scores.WIN += winCoff
        binding.textViewNumWinGame1.text = Scores.WIN.toString()
        Scores.BALANCE += winCoff
        binding.textViewNumTotalGame1.text = Scores.BALANCE.toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_SPINNING_STATE, isSpinning)
        outState.putInt(KEY_IMAGE_1, image1Value)
        outState.putInt(KEY_IMAGE_2, image2Value)
        outState.putInt(KEY_IMAGE_3, image3Value)
        outState.putInt(KEY_IMAGE_4, image4Value)
        outState.putInt(KEY_IMAGE_5, image5Value)
        outState.putInt(KEY_IMAGE_6, image6Value)
        outState.putInt(KEY_IMAGE_7, image7Value)
        outState.putInt(KEY_IMAGE_8, image8Value)
        outState.putInt(KEY_IMAGE_9, image9Value)
        outState.putString(KEY_BET_VALUE, binding.textViewBetGame1.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        //binding.textViewNumTotalGame1.text = Scores.BALANCE.toString()
        //binding.textViewNumWinGame1.text = Scores.WIN.toString()
        val betValue = savedInstanceState.getString(KEY_BET_VALUE)
        binding.textViewBetGame1.text = betValue

        isSpinning = savedInstanceState.getBoolean(KEY_SPINNING_STATE)

        image1Value = savedInstanceState.getInt(KEY_IMAGE_1)
        image2Value = savedInstanceState.getInt(KEY_IMAGE_2)
        image3Value = savedInstanceState.getInt(KEY_IMAGE_3)
        image4Value = savedInstanceState.getInt(KEY_IMAGE_4)
        image5Value = savedInstanceState.getInt(KEY_IMAGE_5)
        image6Value = savedInstanceState.getInt(KEY_IMAGE_6)
        image7Value = savedInstanceState.getInt(KEY_IMAGE_7)
        image8Value = savedInstanceState.getInt(KEY_IMAGE_8)
        image9Value = savedInstanceState.getInt(KEY_IMAGE_9)

        binding.imageViewIcon1Game1.setStartImage(image1Value)
        binding.imageViewIcon2Game1.setStartImage(image2Value)
        binding.imageViewIcon3Game1.setStartImage(image3Value)
        binding.imageViewIcon4Game1.setStartImage(image4Value)
        binding.imageViewIcon5Game1.setStartImage(image5Value)
        binding.imageViewIcon6Game1.setStartImage(image6Value)
        binding.imageViewIcon7Game1.setStartImage(image7Value)
        binding.imageViewIcon8Game1.setStartImage(image8Value)
        binding.imageViewIcon9Game1.setStartImage(image9Value)

        if (isSpinning) {
            val iconListReel1 = arrayListOf(
                binding.imageViewIcon1Game1,
                binding.imageViewIcon2Game1,
                binding.imageViewIcon3Game1
            )

            val iconListReel2 = arrayListOf(
                binding.imageViewIcon4Game1,
                binding.imageViewIcon5Game1,
                binding.imageViewIcon6Game1
            )

            val iconListReel3 = arrayListOf(
                binding.imageViewIcon7Game1,
                binding.imageViewIcon8Game1,
                binding.imageViewIcon9Game1
            )

            val iconsList = arrayListOf(iconListReel1, iconListReel2, iconListReel3)

            iconsList.forEach { list ->
                list.forEach { image ->
                    image.setEventEnd(this@FirstGameActivity)
                }
            }

            isSpinning = true
            binding.imageViewBtnSpinGame1.isEnabled = false
            Scores.BALANCE -= Integer.parseInt(binding.textViewBetGame1.text.toString())
            binding.textViewNumTotalGame1.text = Scores.BALANCE.toString()

            iconListReel1.forEach {
                it.setValueRandom(Random.nextInt(5), 15)
            }

            iconListReel2.forEach {
                it.setValueRandom(Random.nextInt(5), 20)
            }

            iconListReel3.forEach {
                it.setValueRandom(Random.nextInt(5), 25)
            }

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
}