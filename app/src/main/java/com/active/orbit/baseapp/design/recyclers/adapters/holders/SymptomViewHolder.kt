package com.active.orbit.baseapp.design.recyclers.adapters.holders

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBReportSymptom
import com.active.orbit.baseapp.core.database.models.DBSymptom
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.activities.symptoms.ReportSymptomDetailsActivity
import com.active.orbit.baseapp.design.activities.symptoms.SymptomsActivity
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell
import com.active.orbit.baseapp.design.widgets.BaseTextView

class SymptomViewHolder(var activity: BaseActivity, itemView: View) : BaseRecyclerCell<DBReportSymptom>(itemView), View.OnTouchListener {

    private var symptomName: BaseTextView
    private var symptomSeverity: BaseTextView
    private var symptomTimestamp: BaseTextView

    private var rightIconFrame: FrameLayout

    init {
        symptomName = itemView.findViewById(R.id.name)
        symptomSeverity = itemView.findViewById(R.id.severity)
        symptomTimestamp = itemView.findViewById(R.id.timestamp)
        rightIconFrame = itemView.findViewById(R.id.rightIconFrame)
    }

    override fun bind(model: DBReportSymptom) {
        symptomName.text = model.symptomName
        symptomSeverity.text = model.symptomSeverity
        symptomTimestamp.text = TimeUtils.format(TimeUtils.getCurrent(model.symptomTimestamp), Constants.DATE_FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)

        itemView.setOnClickListener {
            onSymptomClicked(model)
        }

        itemView.setOnTouchListener(this)
        rightIconFrame.isClickable = false

    }

    private fun onSymptomClicked(model: DBReportSymptom) {
        val bundle = Bundle()
        bundle.putString(ReportSymptomDetailsActivity.EXTRA_SYMPTOM_ID, model.symptomId.toString())
        Router.getInstance()
            .activityAnimation(ActivityAnimation.LEFT_RIGHT)
            .startBaseActivityForResult(activity, Activities.REPORT_SYMPTOM_DETAILS, bundle, SymptomsActivity.SYMPTOM_REQUEST_CODE)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    when (v) {
                        itemView -> {
                            rightIconFrame.isPressed = true
                        }

                    }
                }
            }
        }
        return false
    }
}