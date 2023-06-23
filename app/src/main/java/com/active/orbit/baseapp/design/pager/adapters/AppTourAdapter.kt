package com.active.orbit.baseapp.design.pager.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.active.orbit.baseapp.core.utils.BaseException
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.pager.fragments.tour.AppTourInfoOneFragment
import com.active.orbit.baseapp.design.pager.fragments.tour.AppTourInfoThreeFragment
import com.active.orbit.baseapp.design.pager.fragments.tour.AppTourInfoTwoFragment

class AppTourAdapter(activity: BaseActivity, fragmentManager: FragmentManager) : BasePagerAdapter(fragmentManager) {

    companion object {

        const val POSITION_ONE = 0
        const val POSITION_TWO = 1
        const val POSITION_THREE = 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {

            POSITION_ONE -> AppTourInfoOneFragment()
            POSITION_TWO -> AppTourInfoTwoFragment()
            POSITION_THREE -> AppTourInfoThreeFragment()

            else -> throw BaseException("Requested page $position is out of bounds")
        }
    }

    override fun getCount(): Int {
        return 3
    }

}