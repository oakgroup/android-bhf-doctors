package com.active.orbit.baseapp.design.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.active.orbit.baseapp.core.enums.MainPanelType
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.databinding.ComponentMainPanelBinding

class MainPanelComponent : FrameLayout, View.OnTouchListener{

    private lateinit var binding: ComponentMainPanelBinding
    private var panelType = MainPanelType.UNDEFINED

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
        binding = ComponentMainPanelBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

        binding.panel.setOnTouchListener(this)
    }

    fun disableClick() {
        binding.panel.isClickable = false
        binding.icon.isClickable = false
    }


    fun setPanel(type: MainPanelType, programmeName: String? = Constants.EMPTY) {
        panelType = type
        binding.panelButton.text = context.getString(panelType.buttonText)
        binding.icon.setImageResource(panelType.icon)
        binding.panelDescription.text = context.getString(panelType.description)
        binding.image.setBackgroundResource(panelType.image)

        if (panelType == MainPanelType.START_PROGRAMME_WITH_NAME) {
            if (programmeName != null) binding.panelDescription.text = context.getString(panelType.description, programmeName)
        } else {
            binding.panelDescription.text = context.getString(panelType.description)
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    when (v) {
                        binding.panel -> {
                            binding.icon.isPressed = true
                            binding.panelButton.isPressed = true
                        }

                    }
                }
            }
        }
        return false
    }

}