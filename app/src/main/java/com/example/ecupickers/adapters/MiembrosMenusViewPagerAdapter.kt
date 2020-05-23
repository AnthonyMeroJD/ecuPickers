package com.example.ecupickers.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ecupickers.fragmentos.ContenidMenu

class MiembrosMenusViewPagerAdapter(fm:FragmentActivity,idMenus:ArrayList<String>):
    FragmentStateAdapter(fm) {
    private var idMenus=idMenus
    override fun getItemCount(): Int =idMenus.size

    override fun createFragment(position: Int): Fragment {
        val fragment:Fragment
        val buddle=Bundle()
        buddle.putString("idMenu",idMenus[position])
        fragment=ContenidMenu()
        return fragment
    }

}