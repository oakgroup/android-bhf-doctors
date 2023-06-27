package com.active.orbit.baseapp.design.activities.menu

import android.os.Bundle
import com.active.orbit.baseapp.databinding.ActivityContactUsBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity

class ContactUsActivity : BaseActivity() {

    private lateinit var binding: ActivityContactUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()

    }
}