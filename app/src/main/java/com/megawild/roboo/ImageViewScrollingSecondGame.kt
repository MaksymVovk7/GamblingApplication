package com.megawild.roboo

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import com.megawild.roboo.databinding.ImageViewScrollingSecondGameBinding

class ImageViewScrollingSecondGame : FrameLayout {

    private lateinit var binding: ImageViewScrollingSecondGameBinding

    internal lateinit var eventEnd: IEventEnd

    internal var lastResult = 0
    internal var oldValue = 0


    companion object {
        private val ANIMATION_DURATION = 70
    }

    val value: Int
        get() = Integer.parseInt(binding.nextImage2.tag.toString())

    fun setEventEnd(eventEnd: IEventEnd) {
        this.eventEnd = eventEnd
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        val layoutInflater = LayoutInflater.from(context).inflate(R.layout.image_view_scrolling_second_game, this)
        binding = ImageViewScrollingSecondGameBinding.bind(layoutInflater)

        binding.nextImage2.translationY = height.toFloat()

    }

    fun initRandomValueSecondGame(image: Int, numRotate: Int) {

        setAnimationCurrentImage2()
        binding.nextImage2.animate().translationY(0f).setDuration(ANIMATION_DURATION.toLong())
            .setListener(object : Animator.AnimatorListener {

                override fun onAnimationEnd(animation: Animator) {
                    setImageAnimationEnd()
                    if (oldValue != numRotate)
                    {
                        initRandomValueSecondGame(image, numRotate)
                        oldValue++
                    } else {
                        resultAnimationEnd(image, numRotate)
                    }
                }

                override fun onAnimationStart(animation: Animator) {

                }
                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }

            }).start()
    }

    private fun setAnimationCurrentImage2() {
        binding.currentImage2.animate()
            .translationY((-height).toFloat())
            .setDuration(ANIMATION_DURATION.toLong()).start()

        binding.nextImage2.translationY = binding.nextImage2.height.toFloat()
    }

    private fun setImageAnimationEnd() {
        setImage(binding.currentImage2, oldValue % 5)
        binding.currentImage2.translationY = 0f
    }

    private fun resultAnimationEnd(image: Int, numRotate: Int) {
        lastResult = 0
        oldValue = 0
        setImage(binding.nextImage2, image)
        eventEnd.eventEnd(image % 5, numRotate)
    }

    private fun setImage(img: ImageView?, value: Int) {
        when (value) {
            Utils.roboIcon1Game2 -> img!!.setImageResource(R.drawable.robo_icon1_game2)
            Utils.roboIcon2Game2 -> img!!.setImageResource(R.drawable.robo_icon2_game2)
            Utils.roboIcon3Game2 -> img!!.setImageResource(R.drawable.robo_icon3_game2)
            Utils.roboIcon4Game2 -> img!!.setImageResource(R.drawable.robo_icon4_game2)
            Utils.roboIcon5Game2 -> img!!.setImageResource(R.drawable.robo_icon5_game2)
        }

        img!!.tag = value
        lastResult = value
    }

    fun setStartImage(value: Int) {
        setImage(binding.nextImage2, value)
    }
}