package com.lets.app.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.lets.app.R
import com.lets.app.activities.MainActivity

object CategoriesUtils {

    fun getEventCategory(viewId: Int): EventCategory {
        return when (viewId) {
            R.id.otherImageView -> EventCategory.OTHER
            R.id.sportImageView -> EventCategory.SPORT
            R.id.cultureImageView -> EventCategory.CULTURE
            R.id.entertainmentImageView -> EventCategory.ENTERTAINMENT
            R.id.recreationImageView -> EventCategory.RECREATION
            R.id.learnImageView -> EventCategory.LEARN
            else -> EventCategory.OTHER
        }
    }

    private fun getTextViewForImageView(viewId: Int): Int {
        return when (viewId) {
            R.id.otherImageView -> R.id.otherTextView
            R.id.sportImageView -> R.id.sportTextView
            R.id.cultureImageView -> R.id.cultureTextView
            R.id.entertainmentImageView -> R.id.entertainmentTextView
            R.id.recreationImageView -> R.id.recreationTextView
            R.id.learnImageView -> R.id.learnTextView
            else -> R.id.otherTextView
        }
    }


    fun animateCategoryView(imageView: ImageView) {
        imageView.setImageColor(R.color.colorPrimary)
        imageView.resize(0.15f)
        imageView.moveYBy(-8f)
        val textView = (imageView.context as MainActivity).findViewById<TextView>(getTextViewForImageView(imageView.id))
        textView.setColorText(R.color.colorPrimary)
        textView.resize(0.15f)
        textView.moveYBy(8f)
    }

    fun revertCategoryViewAnimation(imageView: ImageView) {
        imageView.setImageColor(R.color.secondaryText)
        imageView.resize(-0.15f)
        imageView.moveYBy(8f)
        val textView = (imageView.context as MainActivity).findViewById<TextView>(getTextViewForImageView(imageView.id))
        textView.resize(-0.15f)
        textView.setColorText(R.color.secondaryText)
        textView.moveYBy(-8f)
    }


    fun ImageView.hasImageColor(color: Int): Boolean {
        return this.imageTintList.defaultColor == ContextCompat.getColor(context!!, color)
    }

    private fun ImageView.setImageColor(color: Int) {
        this.imageTintList = ContextCompat.getColorStateList(context!!, color)
    }

    private fun TextView.setColorText(color: Int) {
        return this.setTextColor(ContextCompat.getColor(context!!, color))
    }

    private fun View.resize(scale: Float) {
        this.animate().scaleXBy(scale).setDuration(100).start()
        this.animate().scaleYBy(scale).setDuration(100).start()
    }

    private fun View.moveYBy(distance: Float) {
        this.animate().translationYBy(distance).setDuration(100).start()
    }

    enum class EventCategory(val id: Int) {
        OTHER(0),
        SPORT(1),
        CULTURE(2),
        ENTERTAINMENT(3),
        RECREATION(4),
        LEARN(5)
    }
}