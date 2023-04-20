package com.active.orbit.baseapp.core.providers

import android.annotation.SuppressLint
import android.content.Context
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.design.recyclers.models.FaqModel

/**
 * Utility class that should be used to manage
 * questions and answers
 */
class FaqProvider private constructor(private val context: Context) {

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: FaqProvider? = null

        @Synchronized
        fun getInstance(context: Context): FaqProvider {
            if (instance == null) {
                synchronized(FaqProvider::class.java) {
                    // double check locking
                    if (instance == null)
                        instance = FaqProvider(context)
                }
            }
            instance?.initialize()
            return instance!!
        }
    }

    var faqs = ArrayList<FaqModel>()

    private fun initialize() {

        val category1 = FaqModel(context.resources.getString(R.string.faq_categories_1))
        val category1sub1 = FaqModel(context.resources.getString(R.string.faq_questions_general_1))
        val category1sub2 = FaqModel(context.resources.getString(R.string.faq_questions_general_2))
        val category1sub3 = FaqModel(context.resources.getString(R.string.faq_questions_general_3))
        val category1sub4 = FaqModel(context.resources.getString(R.string.faq_questions_general_4))

        val category2 = FaqModel(context.resources.getString(R.string.faq_categories_2))
        val category2sub1 = FaqModel(context.resources.getString(R.string.faq_questions_setup_1))
        val category2sub2 = FaqModel(context.resources.getString(R.string.faq_questions_setup_2))
        val category2sub3 = FaqModel(context.resources.getString(R.string.faq_questions_setup_3))

        val category3 = FaqModel(context.resources.getString(R.string.faq_categories_3))
        val category3sub1 = FaqModel(context.resources.getString(R.string.faq_questions_technical_1))
        val category3sub2 = FaqModel(context.resources.getString(R.string.faq_questions_technical_2))

        val category1sub1Response = FaqModel(category1sub1.category, context.resources.getString(R.string.faq_answers_1))
        val category1sub2Response = FaqModel(category1sub2.category, context.resources.getString(R.string.faq_answers_2))
        val category1sub3Response = FaqModel(category1sub3.category, context.resources.getString(R.string.faq_answers_3))
        val category1sub4Response = FaqModel(category1sub4.category, context.resources.getString(R.string.faq_answers_4))
        val category2sub1Response = FaqModel(category2sub1.category, context.resources.getString(R.string.faq_answers_5))
        val category2sub2Response = FaqModel(category2sub2.category, context.resources.getString(R.string.faq_answers_6))
        val category2sub3Response = FaqModel(category2sub3.category, context.resources.getString(R.string.faq_answers_7))
        val category3sub1Response = FaqModel(category3sub1.category, context.resources.getString(R.string.faq_answers_8))
        val category3sub2Response = FaqModel(category3sub2.category, context.resources.getString(R.string.faq_answers_9))

        category1.position = 1
        category2.position = 2
        category3.position = 3
        category1sub1.position = 4
        category1sub2.position = 5
        category1sub3.position = 6
        category1sub4.position = 7
        category2sub1.position = 8
        category2sub2.position = 9
        category2sub3.position = 10
        category3sub1.position = 11
        category3sub2.position = 12
        category1sub1Response.position = 13
        category1sub2Response.position = 14
        category1sub3Response.position = 15
        category1sub4Response.position = 16
        category2sub1Response.position = 17
        category2sub2Response.position = 18
        category2sub3Response.position = 19
        category3sub1Response.position = 20
        category3sub2Response.position = 21

        category1sub1.response = category1sub1Response
        category1sub2.response = category1sub2Response
        category1sub3.response = category1sub3Response
        category1sub4.response = category1sub4Response
        category2sub1.response = category2sub1Response
        category2sub2.response = category2sub2Response
        category2sub3.response = category2sub3Response
        category3sub1.response = category3sub1Response
        category3sub2.response = category3sub2Response

        category1.subCategories.add(category1sub1)
        category1.subCategories.add(category1sub2)
        category1.subCategories.add(category1sub3)
        category1.subCategories.add(category1sub4)
        category2.subCategories.add(category2sub1)
        category2.subCategories.add(category2sub2)
        category2.subCategories.add(category2sub3)
        category3.subCategories.add(category3sub1)
        category3.subCategories.add(category3sub2)

        val root = FaqModel(context.getString(R.string.frequently_asked_questions))
        root.position = 0
        root.subCategories.add(category1)
        root.subCategories.add(category2)
        root.subCategories.add(category3)
        faqs.add(root)
    }

    fun getById(position: Int): FaqModel {
        return getById(faqs, position)
    }

    private fun getById(models: ArrayList<FaqModel>, position: Int): FaqModel {
        for (model in models) {
            if (model.position == position) {
                return model
            }
            if (model.subCategories.isNotEmpty()) {
                val subModel = getById(model.subCategories, position)
                if (subModel.isValid()) {
                    return subModel
                }
            }
        }
        Logger.e("Faq with position $position not found")
        return FaqModel(Constants.EMPTY)
    }
}