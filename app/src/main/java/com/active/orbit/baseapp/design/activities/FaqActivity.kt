package com.active.orbit.baseapp.design.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.providers.FaqProvider
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.databinding.ActivityFaqBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.recyclers.engine.adapters.FaqAdapter
import com.active.orbit.baseapp.design.recyclers.engine.managers.BaseLinearLayoutManager
import com.active.orbit.baseapp.design.recyclers.engine.models.FaqModel

class FaqActivity : BaseActivity() {

    private lateinit var binding: ActivityFaqBinding
    var faqModel: FaqModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarTitle(R.string.frequently_asked_questions)
        showBackButton()

        val modelId = activityBundle.getInt(Extra.MODEL_ID.key)
        if (modelId != Constants.INVALID) {
            faqModel = FaqProvider.getInstance(this).getById(modelId)
            if (faqModel!!.hasSubCategories()) {
                setToolbarTitle(faqModel!!.category)
            }
        } else {
            faqModel = FaqProvider.getInstance(this).faqs.first()
        }

        prepare()
    }



    private fun prepare() {
        if (faqModel!!.hasSubCategories()) {
            val linearLayoutManager = BaseLinearLayoutManager(this)
            binding.faqCategories.layoutManager = linearLayoutManager
            val adapter = FaqAdapter(this, faqModel!!.subCategories.toTypedArray())
            binding.faqCategories.adapter = adapter
            val dividerItemDecoration = DividerItemDecoration(this, linearLayoutManager.orientation)
            binding.faqCategories.addItemDecoration(dividerItemDecoration)
        } else {
            showResponse()
            setToolbarTitle(Constants.EMPTY)
        }
    }

    private fun showResponse() {
        binding.faqCategories.visibility = View.GONE
        binding.questionAnswerContainer.visibility = View.VISIBLE
        binding.questionAnswer.text = faqModel!!.response!!.answer
        setFaqTitle(faqModel!!.response!!.question)
    }

    private fun setFaqTitle(title: String) {
        binding.faqTitle.text = title
        binding.faqTitle.visibility = View.VISIBLE
    }
}