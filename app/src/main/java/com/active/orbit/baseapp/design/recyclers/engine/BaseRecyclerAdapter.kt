package com.active.orbit.baseapp.design.recyclers.engine

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.WorkerThread
import androidx.recyclerview.widget.RecyclerView
import com.active.orbit.baseapp.core.generics.BaseProtocol
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.ThreadHandler.backgroundThread
import com.active.orbit.baseapp.core.utils.ThreadHandler.mainThread
import com.active.orbit.baseapp.design.recyclers.listeners.RefreshListener
import com.active.orbit.baseapp.design.recyclers.models.SortedModels

/**
 * Base recyclerview adapter that should be extended from all the adapters
 *
 * @author omar.brugna
 */
abstract class BaseRecyclerAdapter<T : BaseProtocol> : RecyclerView.Adapter<BaseRecyclerCell<T>>() {

    @Suppress("LeakingThis")
    var models = SortedModels(this)

    private var recyclerView: BaseRecyclerView? = null

    abstract fun getViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerCell<T>

    @WorkerThread
    abstract fun dataSource(context: Context): List<T>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerCell<T> {
        return getViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseRecyclerCell<T>, position: Int) {
        @Suppress("UNCHECKED_CAST")
        holder.bind(models[position] as T)
    }

    override fun getItemCount(): Int {
        return models.size()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView as BaseRecyclerView
    }

    fun refresh(context: Context, listener: RefreshListener? = null) {
        backgroundThread {
            val sourceModels = dataSource(context)
            mainThread {
                models.replaceAll(sourceModels)
                listener?.onRefreshed(models.size())
            }
        }
    }

    private fun getItemIndex(model: T?): Int {
        var index = 0
        var found = false
        for (i in 0 until models.size()) {
            if (models[i].identifier() == model?.identifier()) {
                found = true
                break
            }
            index++
        }
        if (found) return index
        return Constants.INVALID
    }

    @CallSuper
    open fun replaceAll(items: ArrayList<T>) {
        models.replaceAll(items)
    }

    @CallSuper
    open fun addItem(model: T?) {
        if (model != null && model.isValid())
            models.add(model)
    }

    @CallSuper
    open fun updateItem(model: T?) {
        if (model != null && model.isValid()) {
            val index = getItemIndex(model)
            if (index != Constants.INVALID)
                models.updateItemAt(index, model)
        }
    }

    open fun removeItem(model: T?) {
        val index = getItemIndex(model)
        if (index != Constants.INVALID)
            models.removeItemAt(index)
    }
}