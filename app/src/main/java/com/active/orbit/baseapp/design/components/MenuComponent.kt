package com.active.orbit.baseapp.design.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.databinding.ComponentMenuBinding

class MenuComponent : FrameLayout {

    internal lateinit var binding: ComponentMenuBinding

    constructor(context: Context) : super(context) {
        prepare(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        prepare(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        prepare(attrs)
    }

    private fun prepare(attrs: AttributeSet?) {
        binding = ComponentMenuBinding.inflate(LayoutInflater.from(context), this, true)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    }

    fun setIconLight() {
        binding.image.setImageResource(R.drawable.ic_menu_light)
    }

    fun setIconDark() {
        binding.image.setImageResource(R.drawable.ic_menu_dark)
    }

    fun setIconPrimary() {
        binding.image.setImageResource(R.drawable.ic_menu_primary)
    }



}