package com.active.orbit.baseapp.design.activities.menu

import android.os.Bundle
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.utils.Utils
import com.active.orbit.baseapp.databinding.ActivityAboutBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity

class AboutActivity : BaseActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()

        binding.aboutAppVersion.text = Utils.getVersionName(this)
    }
}