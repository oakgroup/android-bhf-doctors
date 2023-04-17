package com.active.orbit.baseapp.demo.design.activities

import android.os.Bundle
import android.text.TextUtils
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.engine.Database
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.routing.enums.ResultCode
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.ThreadHandler.backgroundThread
import com.active.orbit.baseapp.core.utils.ThreadHandler.mainThread
import com.active.orbit.baseapp.databinding.ActivityDemoVoteBinding
import com.active.orbit.baseapp.demo.core.database.models.DBDemo
import com.active.orbit.baseapp.demo.design.dialogs.VoteDialog
import com.active.orbit.baseapp.demo.design.dialogs.VoteDialogListener
import com.active.orbit.baseapp.design.activities.engine.BaseActivity

class DemoVoteActivity : BaseActivity() {

    private lateinit var binding: ActivityDemoVoteBinding
    private var model: DBDemo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoVoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()

        backgroundThread {
            var error = false
            val modelId = activityBundle.getString(Extra.MODEL_ID.key)
            if (!TextUtils.isEmpty(modelId)) {
                model = Database.getInstance(this).getDemo().getById(modelId!!)
                if (model?.isValid() != true) {
                    Logger.e("Model is not valid on on ${javaClass.name}")
                    error = true
                }
            } else {
                Logger.e("Model id is null on ${javaClass.name}")
                error = true
            }

            mainThread {
                if (error) finish()
                else prepare()
            }
        }
    }

    private fun prepare() {
        binding.demoVoteImageView.setImageUrl(this, model!!.demoId, R.drawable.ic_progress)
        binding.demoVoteButton.setOnClickListener {
            val arguments = Bundle()
            arguments.putInt(VoteDialog.ARGUMENT_VOTE, model!!.demoVote)

            val voteDialog = VoteDialog()
            voteDialog.isCancelable = true
            voteDialog.arguments = arguments
            voteDialog.listener = object : VoteDialogListener {
                override fun onConfirm(vote: Int) {
                    backgroundThread {
                        model!!.demoVote = vote
                        Database.getInstance(this@DemoVoteActivity).getDemo().upsert(model!!)
                        mainThread {
                            setResult(ResultCode.RESULT_OK.value)
                            finish()
                        }
                    }
                }
            }
            voteDialog.show(supportFragmentManager, VoteDialog::javaClass.name)
        }
    }
}