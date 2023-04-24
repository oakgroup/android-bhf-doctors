package com.active.orbit.baseapp.design.widgets

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.design.listeners.PinEntryViewListener

class PinEntryView : LinearLayoutCompat, TextWatcher, View.OnKeyListener, View.OnFocusChangeListener {

    private var maxLength = 6
    private var editTexts = ArrayList<BaseEditText>()
    private var containers = ArrayList<View>()
    private var disableUpdates = false
    private var wasEmptyBeforeChange = true

    var listener: PinEntryViewListener? = null

    constructor(context: Context) : super(context) {
        customize(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        customize(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        customize(attrs)
    }

    private fun customize(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PinEntryView, 0, 0)
        try {
            maxLength = ta.getInt(R.styleable.PinEntryView_maxLength, maxLength)
        } finally {
            ta.recycle()
        }

        inflate(context, R.layout.pin_entry_view, this)

        val pinContainer = findViewById<LinearLayout>(R.id.pinContainer)

        for (i in 0 until maxLength) {
            val pinView = LayoutInflater.from(context).inflate(R.layout.item_pin_entry_view, pinContainer, false)
            pinContainer.addView(pinView)
            editTexts.add(pinView.findViewById(R.id.pin))
            containers.add(pinView.findViewById(R.id.container))
        }
        pinContainer.invalidate()

        for (editText in editTexts) {
            editText.isCursorVisible = false
            editText.addTextChangedListener(this)
            editText.setOnKeyListener(this)
            editText.onFocusChangeListener = this
        }

//        editTexts.firstOrNull()?.requestFocus()
    }

    fun setPin(pin: String?) {
        disableUpdates = true
        if (TextUtils.isEmpty(pin)) {
            for (editText in editTexts) editText.clear()
        } else {
            val charArray = pin!!.toCharArray()
            if (charArray.size > maxLength) {
                Logger.e("Code inserted is greater than max length, it will be truncated")
            }
            for ((index, char) in charArray.withIndex()) {
                if (index < maxLength) {
                    editTexts[index].setText(char.toString())
                }
            }
        }
        disableUpdates = false
    }

    fun getPin(): String {
        val builder = StringBuilder()
        for (editText in editTexts) builder.append(editText.text)
        return builder.toString()
    }

    fun isComplete(): Boolean {
        for (editText in editTexts) {
            if (TextUtils.isEmpty(editText.textTrim))
                return false
        }
        return true
    }

    override fun setEnabled(enabled: Boolean) {
        for (editText in editTexts) {
            editText.isEnabled = enabled
            editText.setTextColor(
                if (enabled) ContextCompat.getColor(context, R.color.textColorPrimaryDark)
                else ContextCompat.getColor(context, R.color.textColorGray)
            )
        }
        for (container in containers) {
            container.isEnabled = enabled
            container.setBackgroundResource(
                if (enabled) R.drawable.bg_pin_entry_idle
                else R.drawable.bg_pin_entry_idle
            )
        }
        super.setEnabled(enabled)
    }

    override fun afterTextChanged(p0: Editable?) {
        if (!disableUpdates) {
            if (!TextUtils.isEmpty(p0?.toString())) {
                // number inserted, focus next edittext
                var found = false
                for ((index, editText) in editTexts.withIndex()) {
                    if (found) {
                        editText.requestFocus()
                        break
                    } else {
                        if (editText.hasFocus() && index != editTexts.size - 1)
                            found = true
                    }
                }
            }

            if (isComplete()) {
                listener?.onPinInserted(getPin())
            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        wasEmptyBeforeChange = TextUtils.isEmpty(p0)
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        // do nothing
    }

    override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
        if (!disableUpdates) {
            when (p2?.action) {
                KeyEvent.ACTION_DOWN -> {
                    if (p0 is BaseEditText) wasEmptyBeforeChange = TextUtils.isEmpty(p0.text)
                }
                KeyEvent.ACTION_UP -> {
                    if (p1 == KeyEvent.KEYCODE_DEL) {
                        if (p0 is BaseEditText) {
                            if (wasEmptyBeforeChange) {
                                wasEmptyBeforeChange = false
                                // number deleted, focus previous edittext
                                var previousIndex = 0
                                for ((index, editText) in editTexts.withIndex()) {
                                    if (editText.hasFocus()) {
                                        previousIndex = index - 1
                                        break
                                    }
                                }
                                if (previousIndex >= 0) {
                                    editTexts[previousIndex].requestFocus()
                                    editTexts[previousIndex].clear()
                                }
                                return true
                            }
                        }
                    }
                }
            }
        }
        return false
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus && v is BaseEditText)
            v.post(Runnable { v.setSelection(v.length()) })

        for ((index, editText) in editTexts.withIndex()) {
            if (editText.hasFocus()) containers[index].setBackgroundResource(R.drawable.bg_pin_entry_pressed)
            else containers[index].setBackgroundResource(R.drawable.bg_pin_entry_idle)
        }
    }
}