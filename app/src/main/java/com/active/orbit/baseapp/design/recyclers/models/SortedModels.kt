package com.active.orbit.baseapp.design.recyclers.models

import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.active.orbit.baseapp.core.generics.BaseModel
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerAdapter

/**
 * Sorted list that manages adapter models sort logic
 */
class SortedModels<T : BaseModel>(adapter: BaseRecyclerAdapter<T>) : SortedList<BaseModel>(BaseModel::class.java, callback(adapter)) {

    companion object {

        fun callback(adapter: BaseRecyclerAdapter<out BaseModel>) =
            object : SortedListAdapterCallback<BaseModel>(adapter) {

                override fun areItemsTheSame(p0: BaseModel?, p1: BaseModel?): Boolean =
                    p0?.sameOf(p1) ?: false

                override fun areContentsTheSame(p0: BaseModel?, p1: BaseModel?): Boolean =
                    p0?.sameContentOf(p1) ?: false

                override fun compare(p0: BaseModel?, p1: BaseModel?): Int =
                    p0?.compareTo(p1) ?: Constants.PRIORITY_OTHER
            }
    }

    @Suppress("unused")
    fun replaceAll(items: ArrayList<BaseModel>) {
        replaceAll(items.toList())
    }

    override fun replaceAll(vararg items: BaseModel?) {
        super.replaceAll(validate(items.toMutableList()))
    }

    override fun replaceAll(items: Collection<BaseModel>) {
        super.replaceAll(validate(items.toMutableList()))
    }

    override fun replaceAll(items: Array<out BaseModel>, mayModifyInput: Boolean) {
        super.replaceAll(validate(items.toMutableList()).toTypedArray(), mayModifyInput)
    }

    private fun validate(items: MutableList<BaseModel?>): MutableList<BaseModel?> {
        val newItems = ArrayList<BaseModel?>()
        for (item in items) {
            if (item?.isValid() == true) {
                newItems.add(item)
            } else {
                Logger.w("Found invalid item in data source $item")
            }
        }
        return newItems
    }
}