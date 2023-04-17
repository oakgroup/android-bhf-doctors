package com.active.orbit.baseapp.demo.core.deserialization

import android.text.TextUtils
import com.active.orbit.baseapp.core.generics.BaseProtocol
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.demo.core.database.models.DBDemo
import com.google.gson.annotations.SerializedName

class DemoImageMap : BaseProtocol {

    @SerializedName("totalHits")
    var itemsCount = 0

    @SerializedName("hits")
    var items = ArrayList<DemoImageMapItem>()

    override fun identifier(): String {
        return Constants.EMPTY
    }

    override fun isValid(): Boolean {
        return itemsCount > 0 && items.size > 0
    }

    class DemoImageMapItem : BaseProtocol {

        @SerializedName("largeImageURL")
        private var imageUrl: String? = null

        override fun identifier(): String {
            return imageUrl ?: Constants.EMPTY
        }

        fun getImageUrl(): String? {
            return imageUrl
        }

        override fun isValid(): Boolean {
            return !TextUtils.isEmpty(imageUrl)
        }

        fun toDBModel(): DBDemo {
            val model = DBDemo()
            model.demoId = imageUrl!!
            model.demoVote = Constants.INVALID
            return model
        }
    }
}