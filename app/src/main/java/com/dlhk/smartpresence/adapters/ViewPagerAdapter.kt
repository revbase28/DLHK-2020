package com.dlhk.smartpresence.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(
    val fm: FragmentManager,
    val behavior: Int,
    val fragments: ArrayList<Fragment>,
    val titles: ArrayList<String>
    ): FragmentPagerAdapter(fm, behavior) {


    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles.get(position)
    }
}