package com.active.orbit.baseapp.design.recyclers.engine

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Custom base view holder that should be extended from all the view holders
 *
 * @author omar.brugna
 */
abstract class BaseRecyclerCell<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(model: T)
}