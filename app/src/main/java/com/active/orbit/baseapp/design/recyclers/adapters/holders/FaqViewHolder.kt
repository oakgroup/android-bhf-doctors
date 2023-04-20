package com.active.orbit.baseapp.design.recyclers.adapters.holders

import android.content.Context
import android.os.Bundle
import android.view.View
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.databinding.ItemFaqBinding
import com.active.orbit.baseapp.design.activities.Activities
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell
import com.active.orbit.baseapp.design.recyclers.models.FaqModel

class FaqViewHolder(private var context: Context, itemView: View) : BaseRecyclerCell<FaqModel>(itemView) {

    private val binding = ItemFaqBinding.bind(itemView)
    private var mPosition: Int = 0

    override fun bind(model: FaqModel) {
        mPosition = model.position
        binding.faqText.text = model.category

        binding.root.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(Extra.MODEL_ID.key, model.position)
            Router.getInstance()
                .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                .startBaseActivity(context, Activities.FAQ, bundle)
        }
    }
}