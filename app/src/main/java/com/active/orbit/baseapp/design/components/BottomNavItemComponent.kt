package com.active.orbit.baseapp.design.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.enums.BottomNavItemType
import com.active.orbit.baseapp.databinding.ComponentBottomNavItemBinding
import com.active.orbit.baseapp.databinding.ComponentBottomNavigationBinding

class BottomNavItemComponent : FrameLayout {

    internal lateinit var binding: ComponentBottomNavItemBinding

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
        binding = ComponentBottomNavItemBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

    }

    fun setImage(image: Int) {
        binding.image.setImageResource(image)
    }
}