package com.megawild.roboo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.megawild.roboo.databinding.ActivitySecondGameBinding
import kotlin.random.Random

class SecondGameActivity : AppCompatActivity(),IEventEnd {

    private lateinit var binding: ActivitySecondGameBinding

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
        binding = ActivitySecondGameBinding.inflate(layoutInflater)
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
        binding.textViewBetGame2.text = price.toString()
        binding.textViewNumTotalGame2.text = Scores.BALANCE.toString()
        binding.textViewNumWinGame2.text = Scores.WIN.toString()
        binding.imageViewBtnMinusGame2.setOnClickListener {
            if (price >= 50) {
                price -= 50
                binding.textViewBetGame2.text = price.toString()
            }
        }
        binding.imageViewBtnPlusGame2.setOnClickListener {
            price += 50
            binding.textViewBetGame2.text = price.toString()
        }
    }

    private fun startSpin() {

        val iconListReel1 = arrayListOf(
            binding.imageViewIcon1Game2,
            binding.imageViewIcon2Game2,
            binding.imageViewIcon3Game2
        )

        val iconListReel2 = arrayListOf(
            binding.imageViewIcon4Game2,
            binding.imageViewIcon5Game2,
            binding.imageViewIcon6Game2
        )

        val iconListReel3 = arrayListOf(
            binding.imageViewIcon7Game2,
            binding.imageViewIcon8Game2,
            binding.imageViewIcon9Game2
        )

        val iconsList = arrayListOf(iconListReel1, iconListReel2, iconListReel3)

        iconsList.forEach { list ->
            list.forEach { image ->
                image.setEventEnd(this@SecondGameActivity)
            }
        }

        binding.imageViewBtnSpinGame2.setOnClickListener {

            if (Scores.BALANCE == 0 || Scores.BALANCE < price) {
                Scores.resetBalance()
            }

            if (Integer.parseInt(binding.textViewBetGame2.text.toString()) != 0) {
                isSpinning = true
                binding.imageViewBtnSpinGame2.isEnabled = false
                Scores.BALANCE -= Integer.parseInt(binding.textViewBetGame2.text.toString())
                binding.textViewNumTotalGame2.text = Scores.BALANCE.toString()

                iconListReel1.forEach {
                    it.initRandomValueSecondGame(Random.nextInt(5), 15)
                }

                iconListReel2.forEach {
                    it.initRandomValueSecondGame(Random.nextInt(5), 20)
                }

                iconListReel3.forEach {
                    it.initRandomValueSecondGame(Random.nextInt(5), 25)
                }
            } else {
                Toast.makeText(this@SecondGameActivity, "The rate should be greater then 0!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun eventEnd(result: Int, count: Int) {
        if (countDown < 8) {
            countDown++
        } else {
            countDown = 0
            isSpinning = false

            image1Value = binding.imageViewIcon1Game2.value
            image2Value = binding.imageViewIcon2Game2.value
            image3Value = binding.imageViewIcon3Game2.value
            image4Value = binding.imageViewIcon4Game2.value
            image5Value = binding.imageViewIcon5Game2.value
            image6Value = binding.imageViewIcon6Game2.value
            image7Value = binding.imageViewIcon7Game2.value
            image8Value = binding.imageViewIcon8Game2.value
            image9Value = binding.imageViewIcon9Game2.value

            binding.imageViewBtnSpinGame2.isEnabled = true
            if (binding.imageViewIcon4Game2.value == binding.imageViewIcon5Game2.value
                && binding.imageViewIcon5Game2.value == binding.imageViewIcon6Game2.value
            ) {
                setWinScore(3)
            } else if (binding.imageViewIcon4Game2.value == binding.imageViewIcon5Game2.value
                || binding.imageViewIcon5Game2.value == binding.imageViewIcon6Game2.value
                || binding.imageViewIcon4Game2.value == binding.imageViewIcon6Game2.value
            ) {
                setWinScore(2)
            }
        }
    }

    private fun setWinScore(coff: Int) {
        val winCoff = price * coff
        Scores.WIN += winCoff
        binding.textViewNumWinGame2.text = Scores.WIN.toString()
        Scores.BALANCE += winCoff
        binding.textViewNumTotalGame2.text = Scores.BALANCE.toString()

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
        outState.putString(KEY_BET_VALUE, binding.textViewBetGame2.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val betValue = savedInstanceState.getString(KEY_BET_VALUE)
        binding.textViewBetGame2.text = betValue

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

        binding.imageViewIcon1Game2.setStartImage(image1Value)
        binding.imageViewIcon2Game2.setStartImage(image2Value)
        binding.imageViewIcon3Game2.setStartImage(image3Value)
        binding.imageViewIcon4Game2.setStartImage(image4Value)
        binding.imageViewIcon5Game2.setStartImage(image5Value)
        binding.imageViewIcon6Game2.setStartImage(image6Value)
        binding.imageViewIcon7Game2.setStartImage(image7Value)
        binding.imageViewIcon8Game2.setStartImage(image8Value)
        binding.imageViewIcon9Game2.setStartImage(image9Value)

        if (isSpinning) {
            val iconListReel1 = arrayListOf(
                binding.imageViewIcon1Game2,
                binding.imageViewIcon2Game2,
                binding.imageViewIcon3Game2
            )

            val iconListReel2 = arrayListOf(
                binding.imageViewIcon4Game2,
                binding.imageViewIcon5Game2,
                binding.imageViewIcon6Game2
            )

            val iconListReel3 = arrayListOf(
                binding.imageViewIcon7Game2,
                binding.imageViewIcon8Game2,
                binding.imageViewIcon9Game2
            )

            val iconsList = arrayListOf(iconListReel1, iconListReel2, iconListReel3)

            iconsList.forEach { list ->
                list.forEach { image ->
                    image.setEventEnd(this@SecondGameActivity)
                }
            }

            isSpinning = true
            binding.imageViewBtnSpinGame2.isEnabled = false
            Scores.BALANCE -= Integer.parseInt(binding.textViewBetGame2.text.toString())
            binding.textViewNumTotalGame2.text = Scores.BALANCE.toString()

            iconListReel1.forEach {
                it.initRandomValueSecondGame(Random.nextInt(5), 15)
            }

            iconListReel2.forEach {
                it.initRandomValueSecondGame(Random.nextInt(5), 20)
            }

            iconListReel3.forEach {
                it.initRandomValueSecondGame(Random.nextInt(5), 25)
            }

        }
    }

    override fun onPause() {
        super.onPause()
        val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val phoneNumber = sharedPref.getString("phoneNumber", null)
        val editor = sharedPref.edit()
        if (phoneNumber != null) {
            editor.apply {
                putString("balanceGame", Scores.BALANCE.toString())
                apply()
            }
        }
    }
}