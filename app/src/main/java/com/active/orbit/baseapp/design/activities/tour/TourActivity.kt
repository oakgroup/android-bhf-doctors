package com.active.orbit.baseapp.design.activities.tour

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.active.orbit.baseapp.databinding.ActivityTourBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.pager.adapters.AppTourAdapter

class TourActivity : BaseActivity(), ViewPager.OnPageChangeListener, View.OnClickListener {

    private lateinit var binding: ActivityTourBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTourBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepare()
    }

    private fun prepare() {

        val pagerAdapter = AppTourAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.offscreenPageLimit = pagerAdapter.count - 1

        binding.viewPager.addOnPageChangeListener(this)
        binding.indicators.setupWithViewPager(binding.viewPager)
        binding.viewPager.setPagingEnabled(true)

        binding.viewPager.currentItem = AppTourAdapter.POSITION_ONE
        binding.viewPager.post {
            // forced call to this method for the first time
            onPageSelected(binding.viewPager.currentItem)
        }

        binding.btnBack.setOnClickListener(this)
        binding.btnNext.setOnClickListener(this)
        binding.btnClose.setOnClickListener(this)

        binding.btnBack.paintFlags = binding.btnBack.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.btnNext.paintFlags = binding.btnNext.paintFlags or Paint.UNDERLINE_TEXT_FLAG

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        // do nothing
    }

    override fun onPageSelected(position: Int) {
        updateButton(position)
    }

    override fun onPageScrollStateChanged(state: Int) {
        // do nothing
    }

    override fun getToolbarResource(): Int? {
        return null
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnNext -> {
                when (binding.viewPager.currentItem) {
                    AppTourAdapter.POSITION_ONE -> binding.viewPager.currentItem = AppTourAdapter.POSITION_TWO
                    AppTourAdapter.POSITION_TWO -> binding.viewPager.currentItem = AppTourAdapter.POSITION_THREE
                    AppTourAdapter.POSITION_THREE -> binding.viewPager.currentItem = AppTourAdapter.POSITION_FOUR
                    AppTourAdapter.POSITION_FOUR -> finish()
                }
            }

            binding.btnBack -> {
                when (binding.viewPager.currentItem) {
                    AppTourAdapter.POSITION_ONE -> binding.viewPager.currentItem = AppTourAdapter.POSITION_ONE
                    AppTourAdapter.POSITION_TWO -> binding.viewPager.currentItem = AppTourAdapter.POSITION_ONE
                    AppTourAdapter.POSITION_THREE -> binding.viewPager.currentItem = AppTourAdapter.POSITION_TWO
                    AppTourAdapter.POSITION_FOUR -> binding.viewPager.currentItem = AppTourAdapter.POSITION_THREE
                }
            }

            binding.btnClose -> {
                finish()
            }
        }
    }


    private fun updateButton(position: Int) {
        when (position) {
            AppTourAdapter.POSITION_ONE -> {
                binding.btnBack.visibility = View.INVISIBLE
                binding.btnNext.visibility = View.VISIBLE
            }
            AppTourAdapter.POSITION_TWO -> {
                binding.btnBack.visibility = View.VISIBLE
                binding.btnNext.visibility = View.VISIBLE
            }
            AppTourAdapter.POSITION_THREE -> {
                binding.btnBack.visibility = View.VISIBLE
                binding.btnNext.visibility = View.VISIBLE
            }
            AppTourAdapter.POSITION_FOUR -> {
                binding.btnBack.visibility = View.VISIBLE
                binding.btnNext.visibility = View.INVISIBLE
            }
            AppTourAdapter.POSITION_FIVE -> {
                binding.btnBack.visibility = View.VISIBLE
                binding.btnNext.visibility = View.VISIBLE
            }
            AppTourAdapter.POSITION_SIX -> {
                binding.btnBack.visibility = View.VISIBLE
                binding.btnNext.visibility = View.INVISIBLE
            }
        }
    }
}
