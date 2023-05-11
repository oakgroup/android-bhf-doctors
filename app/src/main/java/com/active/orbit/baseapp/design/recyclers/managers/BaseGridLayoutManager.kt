package com.active.orbit.baseapp.design.recyclers.managers

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.active.orbit.baseapp.core.utils.Logger

class BaseGridLayoutManager(context: Context?, spanCount: Int) : GridLayoutManager(context, spanCount) {

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: Exception) {
            Logger.e("Exception on recycler grid layout manager: ${e.localizedMessage}")
        }
    }
}