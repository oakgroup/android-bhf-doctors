package com.active.orbit.baseapp.design.recyclers.models

import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.active.orbit.baseapp.core.generics.BaseProtocol
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerAdapter

/**
 * Sorted list that manages adapter models sort logic
 */
class SortedModels<T : BaseProtocol>(adapter: BaseRecyclerAdapter<T>) : SortedList<BaseProtocol>(BaseProtocol::class.java, callback(adapter)) {

    companion object {

        fun callback(adapter: BaseRecyclerAdapter<out BaseProtocol>) =
            object : SortedListAdapterCallback<BaseProtocol>(adapter) {

                override fun areItemsTheSame(p0: BaseProtocol?, p1: BaseProtocol?): Boolean =
                    p0?.sameOf(p1) ?: false

                override fun areContentsTheSame(p0: BaseProtocol?, p1: BaseProtocol?): Boolean =
                    p0?.sameContentOf(p1) ?: false

                override fun compare(p0: BaseProtocol?, p1: BaseProtocol?): Int =
                    p0?.compareTo(p1) ?: Constants.PRIORITY_OTHER
            }
    }

    @Suppress("unused")
    fun replaceAll(items: ArrayList<BaseProtocol>) {
        replaceAll(items.toList())
    }

    override fun replaceAll(vararg items: BaseProtocol?) {
        super.replaceAll(validate(items.toMutableList()))
    }

    override fun replaceAll(items: Collection<BaseProtocol>) {
        super.replaceAll(validate(items.toMutableList()))
    }

    override fun replaceAll(items: Array<out BaseProtocol>, mayModifyInput: Boolean) {
        super.replaceAll(validate(items.toMutableList()).toTypedArray(), mayModifyInput)
    }

    private fun validate(items: MutableList<BaseProtocol?>): MutableList<BaseProtocol?> {
        val newItems = ArrayList<BaseProtocol?>()
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