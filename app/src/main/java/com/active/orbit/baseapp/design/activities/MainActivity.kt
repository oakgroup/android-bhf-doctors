package com.active.orbit.baseapp.design.activities

import android.os.Bundle
import com.active.orbit.baseapp.databinding.ActivityMainBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showMenuComponent()
        showLogo()



    }
}