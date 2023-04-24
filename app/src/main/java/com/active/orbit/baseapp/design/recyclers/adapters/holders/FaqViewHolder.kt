package com.active.orbit.baseapp.design.recyclers.adapters.holders

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell
import com.active.orbit.baseapp.design.recyclers.models.FaqModel
import com.active.orbit.baseapp.design.widgets.BaseTextView

class FaqViewHolder(private var context: Context, itemView: View) : BaseRecyclerCell<FaqModel>(itemView), View.OnTouchListener {

    private var faqText: BaseTextView
    private var faqIcon: FrameLayout

    private var mPosition: Int = 0

    init {
        faqText = itemView.findViewById(R.id.faqText)
        faqIcon = itemView.findViewById(R.id.faqIcon)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun bind(model: FaqModel) {
        mPosition = model.position
        faqText.text = model.category

        itemView.setOnTouchListener(this)

        itemView.setOnClickListener {
            openNextFaq(model)
        }

        faqIcon.isClickable = false
    }

    fun openNextFaq(model: FaqModel) {
        val bundle = Bundle()
        bundle.putInt(Extra.IDENTIFIER.key, model.position)
        Router.getInstance()
            .activityAnimation(ActivityAnimation.LEFT_RIGHT)
            .startBaseActivity(context, Activities.FAQ, bundle)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    when (v) {
                        itemView -> {
                            faqIcon.isPressed = true
                        }
                    }
                }
            }
        }
        return false
    }
}