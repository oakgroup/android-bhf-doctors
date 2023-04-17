package com.active.orbit.baseapp.demo.design.recyclers.cells

import android.os.Bundle
import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.routing.enums.RequestCode
import com.active.orbit.baseapp.demo.core.deserialization.DemoImageMap
import com.active.orbit.baseapp.demo.design.activities.DemoActivities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell
import com.active.orbit.baseapp.design.widgets.BaseImageView

class DemoImageCell(private val activity: BaseActivity, itemView: View) : BaseRecyclerCell<DemoImageMap.DemoImageMapItem>(itemView) {

    private val demoImageImageView = itemView.findViewById<BaseImageView>(R.id.demoImageImageView)

    override fun bind(model: DemoImageMap.DemoImageMapItem) {
        demoImageImageView.setImageUrl(activity, model.getImageUrl()!!, R.drawable.ic_progress)

        itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Extra.MODEL_ID.key, model.identifier())
            Router.getInstance()
                .activityAnimation(ActivityAnimation.BOTTOM_TOP)
                .startBaseActivityForResult(activity, DemoActivities.DEMO_VOTE, bundle, RequestCode.VOTE.value)
        }
    }
}