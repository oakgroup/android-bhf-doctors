package com.active.orbit.baseapp.design.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.providers.FontProvider
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger

class BaseTextView : AppCompatTextView {

    val textTrim: String
        get() = text?.toString()?.trim() ?: Constants.EMPTY

    constructor(context: Context) : super(context) {
        customize(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        customize(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        customize(attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun customize(attrs: AttributeSet?) {
        var typefaceIndex = Constants.TYPEFACE_REGULAR_INDEX
        val ta = context.obtainStyledAttributes(attrs, R.styleable.BaseText, 0, 0)
        try {
            typefaceIndex = ta.getInt(R.styleable.BaseText_typeface, Constants.TYPEFACE_REGULAR_INDEX)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            ta.recycle()
        }

        try {
            typeface = FontProvider.getFromIndex(context, typefaceIndex)
        } catch (e: Exception) {
            Logger.e("Error loading font ${e.localizedMessage}")
        }
    }

    fun clear() {
        text = Constants.EMPTY
    }

    /**
     * Use to show clickable links as a part of a textView
     */
    fun showLinks(vararg links: Pair<String, OnClickListener>) {
        val spannableString = SpannableString(this.text)
        for (link in links) {
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    Selection.setSelection((view as TextView).text as Spannable, 0)
                    view.invalidate()
                    link.second.onClick(view)
                }
            }
            val startIndexOfLink = this.text.toString().indexOf(link.first)
            spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        movementMethod = LinkMovementMethod.getInstance()
        setText(spannableString, BufferType.SPANNABLE)
    }
}