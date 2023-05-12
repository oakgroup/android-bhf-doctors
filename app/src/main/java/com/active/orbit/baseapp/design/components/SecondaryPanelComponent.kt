package com.active.orbit.baseapp.design.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.enums.SecondaryPanelType
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.databinding.ComponentSecondaryPanelBinding
import com.active.orbit.baseapp.design.models.PanelModel

class SecondaryPanelComponent : FrameLayout, View.OnTouchListener {

    private lateinit var binding: ComponentSecondaryPanelBinding

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
        binding = ComponentSecondaryPanelBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

        binding.panel.setOnTouchListener(this)
        binding.panelButton.paintFlags = binding.panelButton.paintFlags or Paint.UNDERLINE_TEXT_FLAG

    }

    fun disableClick() {
        binding.panel.isClickable = false
        binding.panelButton.isClickable = false
    }

    fun setPanel(type: SecondaryPanelType) {

        binding.panelTitle.text = context.getString(type.title)

        if (type.isProgressView) {
            binding.progressLayout.visibility = VISIBLE
            binding.imageLayout.visibility = GONE

            binding.progress.setBackgroundLineColor(ContextCompat.getColor(context, R.color.colorBackground))
            binding.progress.setProgressLineColor(ContextCompat.getColor(context, R.color.colorAccent))
            binding.progress.setProgressIconResource(R.drawable.ic_activity)
            binding.progress.setLineWidth(40f)
            binding.progress.setMaxProgress(200.toFloat())

        } else {
            binding.progressLayout.visibility = GONE
            binding.imageLayout.visibility = VISIBLE

            binding.image.setImageResource(type.image)
        }

        if (type.detailsOne != Constants.INVALID) {
            binding.detailsOne.visibility = VISIBLE
        } else {
            binding.detailsOne.visibility = INVISIBLE
        }

        if (type.detailsTwo != Constants.INVALID) {
            binding.detailsTwo.visibility = VISIBLE
        } else {
            binding.detailsTwo.visibility = INVISIBLE
        }

        if (type.detailsThree != Constants.INVALID) {
            binding.detailsThree.visibility = VISIBLE
        } else {
            binding.detailsThree.visibility = INVISIBLE
        }

        updatePanel(type, PanelModel())
    }

    fun updatePanel(type: SecondaryPanelType, model: PanelModel) {

        if (type.isProgressView) {
            binding.progress.setProgress(model.value1.toFloat())
        }

        when (type) {
            SecondaryPanelType.ACTIVITY -> {
                // DetailsOne: Move minutes, DetailsTwo: Heart minutes, DetailsThree: Distance walked
                binding.detailsOne.text = context.getString(type.detailsOne, model.value1.toString())
                binding.detailsTwo.text = context.getString(type.detailsTwo, model.value2.toString())
                binding.detailsThree.text = context.getString(type.detailsThree, model.value3.toString())
            }
            SecondaryPanelType.MOBILITY -> {
                // DetailsOne: Away from home, DetailsTwo: motor vehicle trips, DetailsThree: none
                binding.detailsOne.text = context.getString(type.detailsOne, model.value1.toString())
                binding.detailsTwo.text = context.getString(type.detailsTwo, model.value2.toString())
            }
            SecondaryPanelType.PHYSIOLOGY -> {
                // DetailsOne: awake heart rate, DetailsTwo: sleeping heart rate, DetailsThree: none
                binding.detailsOne.text = context.getString(type.detailsOne, model.value1.toString())
                binding.detailsTwo.text = context.getString(type.detailsTwo, model.value2.toString())
            }
            SecondaryPanelType.UNDEFINED -> {
                Logger.e("Undefined panel type")
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    when (v) {
                        binding.panel -> {
                            binding.panelButton.isPressed = true
                        }

                    }
                }
            }
        }
        return false
    }
}