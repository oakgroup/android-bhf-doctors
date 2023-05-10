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
import com.active.orbit.baseapp.databinding.ComponentSecondaryPanelBinding

class SecondaryPanelComponent : FrameLayout, View.OnTouchListener {

    private lateinit var binding: ComponentSecondaryPanelBinding
    private var panelType = SecondaryPanelType.UNDEFINED

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
        panelType = type

        binding.panelTitle.text = context.getString(panelType.title)

        if (panelType.isProgressView) {
            binding.progressLayout.visibility = VISIBLE
            binding.imageLayout.visibility = GONE

            binding.progress.setBackgroundLineColor(ContextCompat.getColor(context, R.color.colorBackground))
            binding.progress.setProgressLineColor(ContextCompat.getColor(context, R.color.colorAccent))
            binding.progress.setProgressIconResource(R.drawable.ic_activity)
            binding.progress.setLineWidth(40f)
            binding.progress.setMaxProgress(200.toFloat())


            //TODO use observers from ActivityActivity
            binding.progress.setProgress(20.toFloat())
        } else {
            binding.progressLayout.visibility = GONE
            binding.imageLayout.visibility = VISIBLE

            binding.image.setImageResource(panelType.image)
        }


        if (panelType.detailsOne != Constants.INVALID) {
            binding.detailsOne.visibility = VISIBLE
            binding.detailsOne.text = context.getString(panelType.detailsOne)
        }
        if (panelType.detailsTwo != Constants.INVALID) {
            binding.detailsTwo.visibility = VISIBLE
            binding.detailsTwo.text = context.getString(panelType.detailsTwo)
        }
        if (panelType.detailsThree != Constants.INVALID) {
            binding.detailsThree.visibility = VISIBLE
            binding.detailsThree.text = context.getString(panelType.detailsThree)
        }


        when(panelType) {
            SecondaryPanelType.ACTIVITY -> {
                //DetailsOne: Move minutes, DetailsTwo: Heart minutes, DetailsThree: Distance walked
                binding.detailsOne.text = context.getString(panelType.detailsOne, 20)
                binding.detailsTwo.text = context.getString(panelType.detailsTwo, 10)
                binding.detailsThree.text = context.getString(panelType.detailsThree, 2)

            }
            SecondaryPanelType.MOBILITY -> {
                //DetailsOne: Away from home, DetailsTwo: motor vehicle trips, DetailsThree: none
                binding.detailsOne.text = context.getString(panelType.detailsOne, 2)
                binding.detailsTwo.text = context.getString(panelType.detailsTwo, 4)
            }
            SecondaryPanelType.PHYSIOLOGY -> {
                //DetailsOne: awake heart rate, DetailsTwo: sleeping heart rate, DetailsThree: none
                binding.detailsOne.text = context.getString(panelType.detailsOne, 90)
                binding.detailsTwo.text = context.getString(panelType.detailsTwo, 90)
            }
            SecondaryPanelType.UNDEFINED -> {}
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