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
        val category1sub5 = FaqModel(context.resources.getString(R.string.faq_questions_general_5))
        val category1sub6 = FaqModel(context.resources.getString(R.string.faq_questions_general_6))
        val category1sub7 = FaqModel(context.resources.getString(R.string.faq_questions_general_7))
        val category1sub8 = FaqModel(context.resources.getString(R.string.faq_questions_general_8))
        val category1sub9 = FaqModel(context.resources.getString(R.string.faq_questions_general_9))

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
        val category1sub5Response = FaqModel(category1sub5.category, context.resources.getString(R.string.faq_answers_5))
        val category1sub6Response = FaqModel(category1sub6.category, context.resources.getString(R.string.faq_answers_6))
        val category1sub7Response = FaqModel(category1sub7.category, context.resources.getString(R.string.faq_answers_7))
        val category1sub8Response = FaqModel(category1sub8.category, context.resources.getString(R.string.faq_answers_8))
        val category1sub9Response = FaqModel(category1sub9.category, context.resources.getString(R.string.faq_answers_9))
        val category2sub1Response = FaqModel(category2sub1.category, context.resources.getString(R.string.faq_answers_10))
        val category2sub2Response = FaqModel(category2sub2.category, context.resources.getString(R.string.faq_answers_11))
        val category2sub3Response = FaqModel(category2sub3.category, context.resources.getString(R.string.faq_answers_12))
        val category3sub1Response = FaqModel(category3sub1.category, context.resources.getString(R.string.faq_answers_13))
        val category3sub2Response = FaqModel(category3sub2.category, context.resources.getString(R.string.faq_answers_14))

        category1.position = 1
        category2.position = 2
        category3.position = 3
        category1sub1.position = 4
        category1sub2.position = 5
        category1sub3.position = 6
        category1sub4.position = 7
        category1sub5.position = 8
        category1sub6.position = 9
        category1sub7.position = 10
        category1sub8.position = 11
        category1sub9.position = 12
        category2sub1.position = 13
        category2sub2.position = 14
        category2sub3.position = 15
        category3sub1.position = 16
        category3sub2.position = 17
        category1sub1Response.position = 18
        category1sub2Response.position = 19
        category1sub3Response.position = 20
        category1sub4Response.position = 21
        category1sub5Response.position = 22
        category1sub6Response.position = 23
        category1sub7Response.position = 24
        category1sub8Response.position = 25
        category1sub9Response.position = 26
        category2sub1Response.position = 27
        category2sub2Response.position = 28
        category2sub3Response.position = 29
        category3sub1Response.position = 30
        category3sub2Response.position = 31

        category1sub1.response = category1sub1Response
        category1sub2.response = category1sub2Response
        category1sub3.response = category1sub3Response
        category1sub4.response = category1sub4Response
        category1sub5.response = category1sub5Response
        category1sub6.response = category1sub6Response
        category1sub7.response = category1sub7Response
        category1sub8.response = category1sub8Response
        category1sub9.response = category1sub9Response
        category2sub1.response = category2sub1Response
        category2sub2.response = category2sub2Response
        category2sub3.response = category2sub3Response
        category3sub1.response = category3sub1Response
        category3sub2.response = category3sub2Response

        category1.subCategories.add(category1sub1)
        category1.subCategories.add(category1sub2)
        category1.subCategories.add(category1sub3)
        category1.subCategories.add(category1sub4)
        category1.subCategories.add(category1sub5)
        category1.subCategories.add(category1sub6)
        category1.subCategories.add(category1sub7)
        category1.subCategories.add(category1sub8)
        category1.subCategories.add(category1sub9)
        category2.subCategories.add(category2sub1)
        category2.subCategories.add(category2sub2)
        category2.subCategories.add(category2sub3)
        category3.subCategories.add(category3sub1)
        category3.subCategories.add(category3sub2)

        val root = FaqModel(context.getString(R.string.faqs))
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