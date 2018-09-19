package com.lets.app.utils

import android.widget.ImageView
import android.widget.TextView
import com.lets.app.R
import com.lets.app.activities.MainActivity
import com.lets.app.utils.ExtFunctions.moveYBy
import com.lets.app.utils.ExtFunctions.resize
import com.lets.app.utils.ExtFunctions.setColorText
import com.lets.app.utils.ExtFunctions.setImageColor

object CategoriesUtils {

    fun getEventCategoryFromImage(viewId: Int): EventCategory {
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

    fun getEventCategoryFromSpinner(index: Int): EventCategory {
        return when (index) {
            0 -> EventCategory.ALL
            1 -> EventCategory.SPORT
            2 -> EventCategory.CULTURE
            3 -> EventCategory.ENTERTAINMENT
            4 -> EventCategory.RECREATION
            5 -> EventCategory.LEARN
            6 -> EventCategory.OTHER
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


    enum class EventCategory(val id: Int) {
        OTHER(0),
        SPORT(1),
        CULTURE(2),
        ENTERTAINMENT(3),
        RECREATION(4),
        LEARN(5),
        ALL(6)
    }

}