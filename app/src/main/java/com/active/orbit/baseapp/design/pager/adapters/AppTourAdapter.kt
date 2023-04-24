package com.active.orbit.baseapp.design.pager.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.active.orbit.baseapp.core.utils.BaseException
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.pager.fragments.*

class AppTourAdapter(activity: BaseActivity, fragmentManager: FragmentManager) : BasePagerAdapter(fragmentManager) {

    companion object {

        const val POSITION_ONE = 0
        const val POSITION_TWO = 1
        const val POSITION_THREE = 2
        const val POSITION_FOUR = 3
        const val POSITION_FIVE = 4
        const val POSITION_SIX = 5
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {

            //use these if all needed
            /*
              POSITION_ONE -> AppTourInfoOneFragment()
              POSITION_TWO -> AppTourInfoTwoFragment()
              POSITION_THREE -> AppTourInfoThreeFragment()
              POSITION_FOUR -> AppTourInfoFourFragment()
              POSITION_FIVE -> AppTourInfoFiveFragment()
              POSITION_SIX -> AppTourInfoSixFragment()

             */

            POSITION_ONE -> AppTourInfoOneFragment()
            POSITION_TWO -> AppTourInfoThreeFragment()
            POSITION_THREE -> AppTourInfoFiveFragment()
            POSITION_FOUR -> AppTourInfoSixFragment()

            else -> throw BaseException("Requested page $position is out of bounds")
        }
    }

    override fun getCount(): Int {
        return 4
    }

}