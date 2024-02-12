package com.megawild.roboo

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import com.megawild.roboo.databinding.ImageViewScrollingFirstGameBinding

class ImageViewScrollingFirstGame : FrameLayout {

    private lateinit var binding: ImageViewScrollingFirstGameBinding

    internal lateinit var eventEnd: IEventEnd

    internal var lastResult = 0
    internal var oldValue = 0


    companion object {
        private val ANIMATION_DURATION = 70
    }

    val value: Int
        get() = Integer.parseInt(binding.nextImage1.tag.toString())

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
        val layoutInflater = LayoutInflater.from(context).inflate(R.layout.image_view_scrolling_first_game, this)
        binding = ImageViewScrollingFirstGameBinding.bind(layoutInflater)

        binding.nextImage1.translationY = height.toFloat()

    }

    fun setValueRandom(image: Int, numRotate: Int) {
        setAnimationCurrentImage1()
        binding.nextImage1.animate().translationY(0f).setDuration(ANIMATION_DURATION.toLong())
            .setListener(object : Animator.AnimatorListener {

                override fun onAnimationEnd(animation: Animator) {
                    setImageAnimationEnd()
                    if (oldValue != numRotate)
                    {
                        setValueRandom(image, numRotate)
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

    private fun setAnimationCurrentImage1() {
        binding.currentImage1.animate()
            .translationY((-height).toFloat())
            .setDuration(ANIMATION_DURATION.toLong()).start()

        binding.nextImage1.translationY = binding.nextImage1.height.toFloat()
    }

    private fun setImageAnimationEnd() {
        setImage(binding.currentImage1, oldValue % 5)
        binding.currentImage1.translationY = 0f
    }

    private fun resultAnimationEnd(image: Int, numRotate: Int) {
        lastResult = 0
        oldValue = 0
        setImage(binding.nextImage1, image)
        eventEnd.eventEnd(image % 5, numRotate)
    }

   private fun setImage(img: ImageView?, value: Int) {
       when (value) {
           Utils.roboIcon1Game1 -> img!!.setImageResource(R.drawable.robo_icon1_game1)
           Utils.roboIcon2Game1 -> img!!.setImageResource(R.drawable.robo_icon2_game1)
           Utils.roboIcon3Game1 -> img!!.setImageResource(R.drawable.robo_icon3_game1)
           Utils.roboIcon4Game1 -> img!!.setImageResource(R.drawable.robo_icon4_game1)
           Utils.roboIcon5Game1 -> img!!.setImageResource(R.drawable.robo_icon5_game1)
       }

        img!!.tag = value
        lastResult = value
    }

    fun setStartImage(value: Int) {
        setImage(binding.nextImage1, value)
    }
}