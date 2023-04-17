package com.active.orbit.baseapp.design.recyclers.engine.managers

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.active.orbit.baseapp.core.utils.Logger

class BaseLinearLayoutManager(context: Context?) : LinearLayoutManager(context) {

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: Exception) {
            Logger.e("Exception on recycler linear layout manager: ${e.message}")
        }
    }
}