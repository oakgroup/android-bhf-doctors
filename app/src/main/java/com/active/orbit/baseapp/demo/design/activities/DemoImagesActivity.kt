package com.active.orbit.baseapp.demo.design.activities

import android.os.Bundle
import android.text.TextUtils
import com.active.orbit.baseapp.core.database.engine.Database
import com.active.orbit.baseapp.core.network.Api
import com.active.orbit.baseapp.core.network.Connection
import com.active.orbit.baseapp.core.network.ConnectionListener
import com.active.orbit.baseapp.core.network.WebService
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.ThreadHandler.backgroundThread
import com.active.orbit.baseapp.databinding.ActivityDemoImagesBinding
import com.active.orbit.baseapp.demo.core.database.models.DBDemo
import com.active.orbit.baseapp.demo.core.deserialization.DemoImageMap
import com.active.orbit.baseapp.demo.design.recyclers.adapters.DemoImageRecyclerAdapter
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.recyclers.engine.managers.BaseGridLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

class DemoImagesActivity : BaseActivity() {

    companion object {

        private const val WEB_IMAGES_PER_ROW = 4
    }

    private lateinit var binding: ActivityDemoImagesBinding
    private var adapter: DemoImageRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()

        prepare()
        loadImages()
    }

    private fun prepare() {
        val manager = BaseGridLayoutManager(this, WEB_IMAGES_PER_ROW)
        binding.demoImagesRecyclerView.layoutManager = manager

        adapter = DemoImageRecyclerAdapter(this)
        binding.demoImagesRecyclerView.adapter = adapter
    }

    private fun loadImages() {
        val webService = WebService(this, Api.IMAGES_GET)
        Connection(webService, object : ConnectionListener {

            override fun onConnectionStarted(tag: Int) {
                showProgressView()
            }

            override fun onConnectionSuccess(tag: Int, response: String) {
                if (!TextUtils.isEmpty(response)) {
                    val dbModels = arrayListOf<DBDemo>()
                    var model: DemoImageMap? = null
                    try {
                        model = Gson().fromJson<DemoImageMap>(response, object : TypeToken<DemoImageMap>() {}.type)
                    } catch (e: JsonSyntaxException) {
                        Logger.e("Error parsing picker web json response")
                    } catch (e: IllegalStateException) {
                        Logger.e("Error parsing picker web json response")
                    }

                    if (model?.isValid() == true) {
                        for (item in model.items) {
                            if (item.isValid()) {
                                adapter?.addItem(item)
                                dbModels.add(item.toDBModel())
                            } else {
                                Logger.w("Demo image model item is not valid")
                            }
                        }
                    } else {
                        Logger.w("Demo image model is not valid")
                    }

                    backgroundThread {
                        if (dbModels.isNotEmpty()) {
                            for (dbModel in dbModels) {
                                // remember the vote for existing models
                                val savedModel = Database.getInstance(this@DemoImagesActivity).getDemo().getById(dbModel.demoId)
                                if (savedModel != null) {
                                    dbModel.demoVote = savedModel.demoVote
                                }
                            }

                            // update database models
                            Database.getInstance(this@DemoImagesActivity).getDemo().upsert(dbModels)
                        }
                    }
                }
            }

            override fun onConnectionError(tag: Int) {
                Logger.e("Error getting images on ${javaClass.name}")
            }

            override fun onConnectionCompleted(tag: Int) {
                hideProgressView()
            }

        }).connect()
    }
}