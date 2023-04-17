package com.active.orbit.baseapp.demo.design.recyclers.cells

import android.os.Bundle
import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.routing.enums.RequestCode
import com.active.orbit.baseapp.demo.core.database.models.DBDemo
import com.active.orbit.baseapp.demo.design.activities.DemoActivities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell
import com.active.orbit.baseapp.design.widgets.BaseImageView
import com.active.orbit.baseapp.design.widgets.BaseTextView

class DemoCell(private val activity: BaseActivity, itemView: View) : BaseRecyclerCell<DBDemo>(itemView) {

    private val demoImageView = itemView.findViewById<BaseImageView>(R.id.demoImageView)
    private val demoVoteTextView = itemView.findViewById<BaseTextView>(R.id.demoVoteTextView)

    override fun bind(model: DBDemo) {
        demoImageView.setImageUrl(activity, model.demoId, R.drawable.ic_progress)
        demoVoteTextView.text = model.demoVote.toString()

        itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Extra.MODEL_ID.key, model.identifier())
            Router.getInstance()
                .activityAnimation(ActivityAnimation.BOTTOM_TOP)
                .startBaseActivityForResult(activity, DemoActivities.DEMO_VOTE, bundle, RequestCode.VOTE.value)
        }
    }
}