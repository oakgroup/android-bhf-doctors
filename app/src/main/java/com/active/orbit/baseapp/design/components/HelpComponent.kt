package com.active.orbit.baseapp.design.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.databinding.ComponentHelpBinding

class HelpComponent : FrameLayout, View.OnTouchListener {

    private lateinit var binding: ComponentHelpBinding

    constructor(context: Context) : super(context) {
        prepare(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        prepare(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        prepare(attrs)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun prepare(attrs: AttributeSet?) {
        binding = ComponentHelpBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)
        setLayout(attrs)

        binding.panel.setOnTouchListener(this)

    }

    fun disableClick() {
        binding.panel.isClickable = false
        binding.panelIcon.isClickable = false
    }

    fun setLayout(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.HelpComponent, 0, 0)
        try {
            binding.title.text = ta.getString(R.styleable.HelpComponent_helpTitle)
        } finally {
            ta.recycle()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    when (v) {
                        binding.panel -> {
                            binding.panelIcon.isPressed = true
                        }

                    }
                }
            }
        }
        return false
    }
}