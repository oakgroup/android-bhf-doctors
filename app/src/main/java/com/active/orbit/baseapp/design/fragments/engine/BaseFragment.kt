package com.active.orbit.baseapp.design.fragments.engine

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.design.activities.engine.BaseActivity

abstract class BaseFragment : Fragment() {

    private lateinit var baseActivity: BaseActivity

    private var mProgressView: ViewGroup? = null
    private var isProgressViewVisible = false

    abstract fun prepare()

    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        baseActivity = activity as BaseActivity
        prepare()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (view is ViewGroup) {
            // inflate progress view
            mProgressView = layoutInflater.inflate(R.layout.progress, view, false) as ViewGroup
            view.addView(mProgressView)
            mProgressView?.setOnClickListener { /* invalidate clicks on below views */ }
            mProgressView?.visibility = ViewGroup.GONE
        }
    }

    fun showProgressView() {
        if (mProgressView != null) {
            mProgressView!!.visibility = ViewGroup.VISIBLE
            isProgressViewVisible = true
        }
    }

    fun hideProgressView() {
        if (isProgressViewVisible && mProgressView != null) {
            mProgressView!!.visibility = ViewGroup.GONE
            isProgressViewVisible = false
        }
    }
}