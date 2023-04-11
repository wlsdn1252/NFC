package com.example.fragmentpractice3.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.fragmentpractice3.MyInfoActivity
import com.example.fragmentpractice3.fragments.FirstFragment
import com.example.fragmentpractice3.fragments.MyInfoFragment
import com.example.fragmentpractice3.fragments.SecondFragment

class MainAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0-> "메인"
            else -> "키테고리"
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> FirstFragment()
            else -> SecondFragment()
        }
    }

}