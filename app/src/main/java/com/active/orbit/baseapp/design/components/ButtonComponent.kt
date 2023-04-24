package com.active.orbit.baseapp.design.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.active.orbit.baseapp.databinding.ComponentButtonBinding

class ButtonComponent : FrameLayout, View.OnTouchListener {

    private lateinit var binding: ComponentButtonBinding

    constructor(context: Context) : super(context) {
        prepare()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        prepare()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        prepare()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun prepare() {

        binding = ComponentButtonBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

        binding.frame.setOnTouchListener(this)
    }

    fun disableClick() {
        binding.layout.isClickable = false
        binding.frame.isClickable = false
        binding.frameIcon.isClickable = false
    }

    @SuppressLint("ClickableViewAccessibility")
    fun disableTouch() {
        binding.frame.setOnTouchListener(null)
    }

    fun setIcon(resource: Int) {
        binding.icon.setImageResource(resource)
    }

    fun setText(text: String?) {
        binding.textSelected.text = text
    }

    fun showProgress() {
        binding.icon.visibility = View.GONE
        binding.progress.visibility = View.VISIBLE
    }

    fun hideProgress() {
        binding.icon.visibility = View.VISIBLE
        binding.progress.visibility = View.GONE
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    when (v) {
                        binding.frame -> {
                            binding.frameIcon.isPressed = true
                        }
                    }
                }
            }
        }
        return false
    }

}